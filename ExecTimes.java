private int[] getTotalExecutionTime(int n, String[] logs) {
        int[] execTimes = new int[n];
        int currFuncId = -1;
        int currFuncStart = -1;
        int lastFuncId = -1;
        int lastFuncStart = -1;
        for(String log : logs) {
            StringTokenizer st = new StringTokenizer(log, ":");
            int id = Integer.parseInt(st.nextToken());
            String action = st.nextToken();
            int timeStamp = Integer.parseInt(st.nextToken());
            if(action.equals("start")) {
                if(currFuncId != -1) execTimes[currFuncId] += timeStamp - currFuncStart;
                lastFuncId = currFuncId;
                currFuncId = id;
                currFuncStart = timeStamp;
            } else {
                execTimes[currFuncId] += timeStamp - currFuncStart + 1;
                currFuncId = lastFuncId;
                currFuncStart = timeStamp + 1;
            }
        }
        return execTime;
    }
