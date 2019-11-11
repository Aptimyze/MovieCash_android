package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetGoogleSearchLocationResponse implements Serializable {

    @SerializedName("ResponseID")
    @Expose
    private int ResponseID;

    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;

    @SerializedName("ResponseData")
    @Expose
    private ArrayList<ResponseData> responseData;

    @SerializedName("ResponseMessage")
    @Expose
    private String ResponseMessage;

    @SerializedName("ResponseJSON")
    @Expose
    private String ResponseJSON;

    public int getResponseID() {
        return ResponseID;
    }

    public void setResponseID(int responseID) {
        ResponseID = responseID;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public ArrayList<ResponseData> getResponseData() {
        return responseData;
    }

    public void setResponseData(ArrayList<ResponseData> responseData) {
        this.responseData = responseData;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public String getResponseJSON() {
        return ResponseJSON;
    }

    public void setResponseJSON(String responseJSON) {
        ResponseJSON = responseJSON;
    }

    public static class ResponseData implements Serializable {

        @SerializedName("ID")
        @Expose
        private int ID;

        @SerializedName("place_id")
        @Expose
        private String place_id;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("latitude")
        @Expose
        private String latitude;

        @SerializedName("longitude")
        @Expose
        private String longitude;

        @SerializedName("type")
        @Expose
        private String type;

        @SerializedName("vicinity")
        @Expose
        private String vicinity;

        @SerializedName("icon")
        @Expose
        private String icon;

        @SerializedName("searchtype")
        @Expose
        private String searchtype;

        @SerializedName("Distance")
        @Expose
        private String Distance;

        @SerializedName("LastUsedInMinuite")
        @Expose
        private int LastUsedInMinuite;

        @SerializedName("LastUsedInSecond")
        @Expose
        private int LastUsedInSecond;

        public ResponseData(int ID, String place_id, String name, String latitude, String longitude, String type,
                            String vicinity, String icon, String searchtype, String distance, int lastUsedInMinuite,
                            int lastUsedInSecond) {
            this.ID = ID;
            this.place_id = place_id;
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
            this.type = type;
            this.vicinity = vicinity;
            this.icon = icon;
            this.searchtype = searchtype;
            Distance = distance;
            LastUsedInMinuite = lastUsedInMinuite;
            LastUsedInSecond = lastUsedInSecond;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getSearchtype() {
            return searchtype;
        }

        public void setSearchtype(String searchtype) {
            this.searchtype = searchtype;
        }

        public String getDistance() {
            return Distance;
        }

        public void setDistance(String distance) {
            Distance = distance;
        }

        public int getLastUsedInMinuite() {
            return LastUsedInMinuite;
        }

        public void setLastUsedInMinuite(int lastUsedInMinuite) {
            LastUsedInMinuite = lastUsedInMinuite;
        }

        public int getLastUsedInSecond() {
            return LastUsedInSecond;
        }

        public void setLastUsedInSecond(int lastUsedInSecond) {
            LastUsedInSecond = lastUsedInSecond;
        }
    }
}
