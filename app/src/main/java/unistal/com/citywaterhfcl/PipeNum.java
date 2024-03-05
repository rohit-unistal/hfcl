package unistal.com.citywaterhfcl;

public class PipeNum {

    private String pipeNumber;
    private PipeData pipeData;
    PipeNum(String pipeNumber,PipeData pipeData)
    {
        this.pipeNumber = pipeNumber;
        this.pipeData = pipeData;
    }
    public String getPipeNumber() {
        return pipeNumber;
    }

    public void setPipeNumber(String pipeNumber) {
        this.pipeNumber = pipeNumber;
    }

    public PipeData getPipeData() {
        return pipeData;
    }

    public void setPipeData(PipeData pipeData) {
        this.pipeData = pipeData;
    }
    public static class PipeData {

        private String startNode;
        private String stopNode;
        private String scope;
        private String pipeType;
        private String subType;
        private String pipeDiameter;
        PipeData( String startNode, String stopNode, String scope,String pipeType, String subType,String pipeDiameter)
        {
            this.startNode = startNode;
            this.stopNode = stopNode;
            this.scope = scope;
            this.pipeType = pipeType;
            this.subType = subType;
            this.pipeDiameter = pipeDiameter;
        }

        public String getStartNode() {
            return startNode;
        }

        public void setStartNode(String startNode) {
            this.startNode = startNode;
        }

        public String getStopNode() {
            return stopNode;
        }

        public void setStopNode(String stopNode) {
            this.stopNode = stopNode;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getPipeType() {
            return pipeType;
        }

        public void setPipeType(String pipeType) {
            this.pipeType = pipeType;
        }

        public String getSubType() {
            return subType;
        }

        public void setSubType(String subType) {
            this.subType = subType;
        }

        public String getPipeDiameter() {
            return pipeDiameter;
        }

        public void setPipeDiameter(String pipeDiameter) {
            this.pipeDiameter = pipeDiameter;
        }

    }
    @Override
    public String toString() {
        return pipeNumber;
    }
}
