package jonomoneta.juno.moviecash.Model;

import java.util.ArrayList;

public class BroadcastResult {

    ArrayList<Result> resultList;

    public ArrayList<Result> getResultList() {
        return resultList;
    }

    public void setResultList(ArrayList<Result> resultList) {
        this.resultList = resultList;
    }

    public static class Result{

        String id;

        String resourceUri;

        String type;

        public Result(String id, String resourceUri, String type) {
            this.id = id;
            this.resourceUri = resourceUri;
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getResourceUri() {
            return resourceUri;
        }

        public void setResourceUri(String resourceUri) {
            this.resourceUri = resourceUri;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
