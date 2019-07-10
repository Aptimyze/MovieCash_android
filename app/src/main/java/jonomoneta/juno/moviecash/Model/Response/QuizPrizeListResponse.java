package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizPrizeListResponse implements Serializable {

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

    public class ResponseData implements Serializable {

        @SerializedName("ID")
        @Expose
        private int ID;

        @SerializedName("UserID")
        @Expose
        private int UserID;

        @SerializedName("QuizGameID")
        @Expose
        private int QuizGameID;

        @SerializedName("QuizGameName")
        @Expose
        private String QuizGameName;

        @SerializedName("GameLevel")
        @Expose
        private int GameLevel;

        @SerializedName("IsClaim")
        @Expose
        private boolean IsClaim;

        @SerializedName("TotalQuestions")
        @Expose
        private int TotalQuestions;

        @SerializedName("TotalRightAnswer")
        @Expose
        private int TotalRightAnswer;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getUserID() {
            return UserID;
        }

        public void setUserID(int userID) {
            UserID = userID;
        }

        public int getQuizGameID() {
            return QuizGameID;
        }

        public void setQuizGameID(int quizGameID) {
            QuizGameID = quizGameID;
        }

        public String getQuizGameName() {
            return QuizGameName;
        }

        public void setQuizGameName(String quizGameName) {
            QuizGameName = quizGameName;
        }

        public int getGameLevel() {
            return GameLevel;
        }

        public void setGameLevel(int gameLevel) {
            GameLevel = gameLevel;
        }

        public boolean isClaim() {
            return IsClaim;
        }

        public void setClaim(boolean claim) {
            IsClaim = claim;
        }

        public int getTotalQuestions() {
            return TotalQuestions;
        }

        public void setTotalQuestions(int totalQuestions) {
            TotalQuestions = totalQuestions;
        }

        public int getTotalRightAnswer() {
            return TotalRightAnswer;
        }

        public void setTotalRightAnswer(int totalRightAnswer) {
            TotalRightAnswer = totalRightAnswer;
        }
    }
}
