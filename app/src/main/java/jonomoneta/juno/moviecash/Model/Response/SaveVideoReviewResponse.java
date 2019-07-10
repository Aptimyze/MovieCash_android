package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SaveVideoReviewResponse implements Serializable {

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

    public class ResponseData implements Serializable{

        @SerializedName("TotalReview")
        @Expose
        private double TotalReview;

        @SerializedName("TotalRightReview")
        @Expose
        private double TotalRightReview;

        @SerializedName("TotalWrongReview")
        @Expose
        private double TotalWrongReview;

        public double getTotalReview() {
            return TotalReview;
        }

        public void setTotalReview(double totalReview) {
            TotalReview = totalReview;
        }

        public double getTotalRightReview() {
            return TotalRightReview;
        }

        public void setTotalRightReview(double totalRightReview) {
            TotalRightReview = totalRightReview;
        }

        public double getTotalWrongReview() {
            return TotalWrongReview;
        }

        public void setTotalWrongReview(double totalWrongReview) {
            TotalWrongReview = totalWrongReview;
        }
    }

}
