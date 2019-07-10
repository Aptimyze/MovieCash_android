package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import jonomoneta.juno.moviecash.Model.WinnerListItem;

public class WinnerResponse {

    @SerializedName("ResponseID")
    @Expose
    private int ResponseID;

    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;

    @SerializedName("ResponseData")
    @Expose
    private ResponseData ResponseData;

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

    public WinnerResponse.ResponseData getResponseData() {
        return ResponseData;
    }

    public void setResponseData(WinnerResponse.ResponseData responseData) {
        ResponseData = responseData;
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

        @SerializedName("TotalWinners")
        @Expose
        private int TotalWinners;

        @SerializedName("QuizGameWinnerList")
        @Expose
        private ArrayList<WinnerListItem> QuizGameWinnerList;

        public int getTotalWinners() {
            return TotalWinners;
        }

        public void setTotalWinners(int totalWinners) {
            TotalWinners = totalWinners;
        }

        public ArrayList<WinnerListItem> getQuizGameWinnerList() {
            return QuizGameWinnerList;
        }

        public void setQuizGameWinnerList(ArrayList<WinnerListItem> quizGameWinnerList) {
            QuizGameWinnerList = quizGameWinnerList;
        }

    }
}
