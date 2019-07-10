package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class AnswerCountResponse implements Serializable {

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

        @SerializedName("QuizGameQuestionID")
        @Expose
        private int QuizGameQuestionID;

        @SerializedName("TotalRightAnswer")
        @Expose
        private long TotalRightAnswer;

        @SerializedName("TotalWrongAnswer")
        @Expose
        private long TotalWrongAnswer;

        public int getQuizGameQuestionID() {
            return QuizGameQuestionID;
        }

        public void setQuizGameQuestionID(int quizGameQuestionID) {
            QuizGameQuestionID = quizGameQuestionID;
        }

        public long getTotalRightAnswer() {
            return TotalRightAnswer;
        }

        public void setTotalRightAnswer(long totalRightAnswer) {
            TotalRightAnswer = totalRightAnswer;
        }

        public long getTotalWrongAnswer() {
            return TotalWrongAnswer;
        }

        public void setTotalWrongAnswer(long totalWrongAnswer) {
            TotalWrongAnswer = totalWrongAnswer;
        }
    }


}
