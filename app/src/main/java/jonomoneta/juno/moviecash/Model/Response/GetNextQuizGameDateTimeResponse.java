package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetNextQuizGameDateTimeResponse implements Serializable {

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

        @SerializedName("ID")
        @Expose
        private int ID;

        @SerializedName("Name")
        @Expose
        private String Name;

        @SerializedName("ReleaseDateUTCString")
        @Expose
        private String ReleaseDateUTCString;

        @SerializedName("QuizGameType")
        @Expose
        private String QuizGameType;

        @SerializedName("IsMegaQuiz")
        @Expose
        private boolean IsMegaQuiz;

        @SerializedName("QuizGamePrize")
        @Expose
        private double QuizGamePrize;

        @SerializedName("RoundNo")
        @Expose
        private int RoundNo;

        @SerializedName("DividedByRule")
        @Expose
        private int DividedByRule;

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

        public String getReleaseDateUTCString() {
            return ReleaseDateUTCString;
        }

        public void setReleaseDateUTCString(String releaseDateUTCString) {
            ReleaseDateUTCString = releaseDateUTCString;
        }

        public String getQuizGameType() {
            return QuizGameType;
        }

        public void setQuizGameType(String quizGameType) {
            QuizGameType = quizGameType;
        }

        public boolean isMegaQuiz() {
            return IsMegaQuiz;
        }

        public void setMegaQuiz(boolean megaQuiz) {
            IsMegaQuiz = megaQuiz;
        }

        public double getQuizGamePrize() {
            return QuizGamePrize;
        }

        public void setQuizGamePrize(double quizGamePrize) {
            QuizGamePrize = quizGamePrize;
        }

        public int getRoundNo() {
            return RoundNo;
        }

        public void setRoundNo(int roundNo) {
            RoundNo = roundNo;
        }

        public int getDividedByRule() {
            return DividedByRule;
        }

        public void setDividedByRule(int dividedByRule) {
            DividedByRule = dividedByRule;
        }
    }


}
