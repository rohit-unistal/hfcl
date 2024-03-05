package unistal.com.citywaterhfcl;

import java.util.ArrayList;

public class PipeModel {

    private String typeId;
    private String pipeType;
    private ArrayList<PipeSubType> pipeSubType ;
    PipeModel(String typeId,String pipeType,ArrayList<PipeSubType> pipeSubType){

         this.typeId= typeId;
        this.pipeType= pipeType;
        this.pipeSubType = pipeSubType;
    }
    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getPipeType() {
        return pipeType;
    }

    public void setPipeType(String pipeType) {
        this.pipeType = pipeType;
    }

    public ArrayList<PipeSubType> getPipeSubType() {
        return pipeSubType;
    }

    public void setPipeSubType(ArrayList<PipeSubType> pipeSubType) {
        this.pipeSubType = pipeSubType;
    }
    public static class PipeSubType {

        private String id;
        private String name;
        private ArrayList<PipeDium> pipeDia;
        PipeSubType(String id,String name,ArrayList<PipeDium> pipeDia){

            this.id= id;
            this.name= name;
            this.pipeDia = pipeDia;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<PipeDium> getPipeDia() {
            return pipeDia;
        }

        public void setPipeDia(ArrayList<PipeDium> pipeDia) {
            this.pipeDia = pipeDia;
        }
        public static class PipeDium {

            private String diaid;
            private String dianame;
            PipeDium(String id,String name){
                this.diaid= id;
                this.dianame= name;

            }

            public String getId() {
                return diaid;
            }

            public void setId(String id) {
                this.diaid = id;
            }

            public String getName() {
                return dianame;
            }

            public void setName(String name) {
                this.dianame = name;
            }
            @Override
            public String toString() {
                return dianame;
            }
        }
        @Override
        public String toString() {
            return name;
        }
    }
    @Override
    public String toString() {
        return pipeType;
    }
}