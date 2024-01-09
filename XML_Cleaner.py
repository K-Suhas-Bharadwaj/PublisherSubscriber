path_for_required_attributes = ['{http://abnamro/ssf}SSF.Counterparty.LocalId', 
'{http://abnamro/ssf}SSF.Counterparty.CounterpartyName']

import xml.etree.ElementTree as ET

ET.register_namespace('ssf', 'http://abnamro/ssf')

tree = ET.parse('//solon.prd/files/Q/Global/_ApplicationData/AxiomInterface/MQ/data/processing/DCZ/C/XML_Loader_Testing/EntraCard_CP_2022012510010823_PAYLOAD.xml')

root = tree.getroot()

root_copy = tree.getroot()


class Node:
    
    def __init__(self, node, path, parent):
        self.node = node
        self.path = path
        if len(list(node)) > 0:
            self.has_children = True
        else:
            self.has_children = False
        self.are_children_processed = False
        self.parent = parent
        
    def get_children(self):
        children = list(self.node)
        children.reverse()
        return children


# In[8]:


def clean_xml():
    stack = list()
    root_node = Node(root, root.tag, None)
    stack.append(root_node)
    while len(stack) > 0:
        processing_node = stack[-1]
        if processing_node.has_children and not(processing_node.are_children_processed):
            stack[-1].are_children_processed = True
            parent = processing_node.node
            parent_path = processing_node.path
            children = processing_node.get_children()
            for child in children:
                child_node = Node(child, parent_path + '.' + child.tag, parent)
                stack.append(child_node)
        else:
            processing_node = stack.pop()
            parent = processing_node.parent
            if processing_node.path not in path_for_required_attributes and not(processing_node.has_children) and parent != None:
                parent.remove(processing_node.node)
            if processing_node.are_children_processed and len(list(processing_node.node)) == 0:
                parent.remove(processing_node.node)


clean_xml()

#ET.dump(root_copy)

output_file = open('//solon.prd/files/Q/Global/_ApplicationData/AxiomInterface/MQ/data/processing/DCZ/C/XML_Loader_Testing/Generated.xml', 'wb')
output_file.write(b'<?xml version="1.0" encoding="UTF-8" standalone = "no"?>\n')
tree.write(output_file,encoding='utf-8', xml_declaration=False)