# Following are built in libraries
import sys
import os
import shutil
import logging
import json
import hashlib
from datetime import date

# Following are custom python files
from generate_metadata import generate_metadata_file
from zip_files import zip_files
import file_encryption
import generate_mime

# Define the root directory of encryption application
application_directory = "C:/Users/C83526/Documents/logius_mime_generation_application/"

# Get log object
logging.basicConfig(format = '%(asctime)s %(levelname)8s: %(message)s', level = logging.DEBUG)
log = logging.getLogger(__name__)

# Load the parameters from parameters.json file
log.info('Getting parameters from parameters.json')
with open(os.path.join(application_directory, 'parameters.json'), 'r') as params:
    parameters = json.load(params)

# Initialize basic configuration details
log.info('Initializing parameters')
reporting_entity = parameters['reporting_entity']
reporting_agent = parameters['reporting_agent']
reporting_reference_date = parameters['reporting_reference_date']
input_zip_file_name = parameters['input_zip_file_name']
sequence_number = parameters['sequence_number']

# Load the configuration details from config.json file
log.info('Getting configuration details')
with open(os.path.join(application_directory, 'config.json'), 'r') as f:
    config = json.load(f)

# Initialize basic configuration details
log.info('Initializing basic configuration details')
input_files_path = os.path.join(application_directory, config['input_files_path'])
templates_path = os.path.join(application_directory, config['templates_path'])
processing_directory = os.path.join(application_directory, config['processing_directory'])

# Clean the processing directory
log.info('Cleaning the processing directory')
for file in os.listdir(processing_directory):
    os.remove(os.path.join(processing_directory, file))

# Copy the zip file from input directory to processing directory
zip_file_path = os.path.join(input_files_path, input_zip_file_name)
shutil.copy(zip_file_path, processing_directory)
zip_file_path = os.path.join(processing_directory, input_zip_file_name)

#Start encryption process   
log.info('Starting encryption process for reporting_agent %s with reporting reference date %s' % (reporting_agent, reporting_reference_date))

# Step 1. ENCRYPT ZIP FILE
log.info("Encrypting ZIP container")
encrypted_zip_file_path = zip_file_path + '.pgp'
AESencryptor = file_encryption.AES_encryption()
AESencryptor.encrypt(zip_file_path, encrypted_zip_file_path)

# Step 2. SAVE KEY AND IV TO SLEUTEL.XML FILE
log.info("Saving key and iv to sleutel.xml file")
key_template_path = os.path.join(templates_path, config['key_template_name'])
key_file_path = os.path.join(processing_directory, 'sleutel.xml')

AESencryptor.key_to_xml(key_template_path, key_file_path)

# Step 3. ENCRYPT SLEUTEL.XML FILE
public_key_certificate_path = os.path.join(application_directory, config['public_key_certificate_path'])
RSAencryptor = file_encryption.RSA_encryption(public_key_certificate_path)
encrypted_key_file_path = key_file_path + '.pgp'

RSAencryptor.encrypt(key_file_path, encrypted_key_file_path)

# Step 4. CREATE ZIP OF ENCRYPTED FILES 
files_to_be_zipped = [os.path.join(processing_directory, f) for f in ['sleutel.xml.pgp', input_zip_file_name + '.pgp']]
data_delivery_code = config['data_delivery_code'][reporting_entity]
output_zip_file_name = reporting_agent + '_' + reporting_reference_date + '_' + data_delivery_code + '_' + date.today().strftime("%Y-%m-%d") + '_' + str(sequence_number).zfill(2) + '.zip'
output_zip_file_path = os.path.join(processing_directory, output_zip_file_name)
zip_files(output_zip_file_path, files_to_be_zipped)

# Step 5. GENERATE MIME FILE 
SHA1_DNB_Code = reporting_agent + ';' + reporting_reference_date + ';' + data_delivery_code + ';'
SHA1_DNB_Code = hashlib.sha1(SHA1_DNB_Code.encode('utf-8')).hexdigest()

mime_file_name = output_zip_file_name.replace('.zip', '.mime')
output_directory = os.path.join(application_directory, config['output_directory'])
mime_file_path = os.path.join(output_directory, mime_file_name)

mime_template_path = os.path.join(templates_path, config['mime_file_template_name'])
mime_header_template_path = os.path.join(templates_path, config['mime_header_template_name'])

kvk_number = config['kvk_number'][reporting_agent]

mime_generator = generate_mime.generate_mime(SHA1_DNB_Code, kvk_number, output_zip_file_name, mime_file_path)
mime_generator.generate_mime_file(output_zip_file_path, mime_template_path, mime_header_template_path)