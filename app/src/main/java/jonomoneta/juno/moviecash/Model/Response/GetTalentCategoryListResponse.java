package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetTalentCategoryListResponse implements Serializable {

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

    public class ResponseData implements Serializable{

        @SerializedName("ID")
        @Expose
        private int ID;

        @SerializedName("Name")
        @Expose
        private String Name;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }
}
