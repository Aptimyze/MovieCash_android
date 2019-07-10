package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuizGameFreeSubscribeUserResponse {

    @SerializedName("ResponseID")
    @Expose
    private int ResponseID;

    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;

    @SerializedName("ResponseData")
    @Expose
    private ResponseData responseData;

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

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
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

    public class ResponseData implements Serializable {

        @SerializedName("SubscriptionStartDate")
        @Expose
        private String SubscriptionStartDate;

        @SerializedName("SubscriptionEndDate")
        @Expose
        private String SubscriptionEndDate;

        public String getSubscriptionStartDate() {
            return SubscriptionStartDate;
        }

        public void setSubscriptionStartDate(String subscriptionStartDate) {
            SubscriptionStartDate = subscriptionStartDate;
        }

        public String getSubscriptionEndDate() {
            return SubscriptionEndDate;
        }

        public void setSubscriptionEndDate(String subscriptionEndDate) {
            SubscriptionEndDate = subscriptionEndDate;
        }
    }

}
