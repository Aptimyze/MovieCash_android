package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Prediction implements Serializable {

    @SerializedName("ResponseID")
    @Expose
    private int ResponseID;

    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;

    @SerializedName("ResponseData")
    @Expose
    private ArrayList<ResponseData> responseDataList;

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

    public ArrayList<ResponseData> getResponseDataList() {
        return responseDataList;
    }

    public void setResponseDataList(ArrayList<ResponseData> responseDataList) {
        this.responseDataList = responseDataList;
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

        @SerializedName("Question")
        @Expose
        private String Question;

        @SerializedName("QuestionType")
        @Expose
        private String QuestionType;

        @SerializedName("AnswerList")
        @Expose
        private ArrayList<AnswerList> answerLists;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getQuestion() {
            return Question;
        }

        public void setQuestion(String question) {
            Question = question;
        }

        public String getQuestionType() {
            return QuestionType;
        }

        public void setQuestionType(String questionType) {
            QuestionType = questionType;
        }

        public ArrayList<AnswerList> getAnswerLists() {
            return answerLists;
        }

        public void setAnswerLists(ArrayList<AnswerList> answerLists) {
            this.answerLists = answerLists;
        }

        public class AnswerList implements Serializable {

            @SerializedName("ID")
            @Expose
            private int ID;

            @SerializedName("PredictionQuestionID")
            @Expose
            private int PredictionQuestionID;

            @SerializedName("Answer")
            @Expose
            private String Answer;

            @SerializedName("IsRight")
            @Expose
            private boolean IsRight;

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public int getPredictionQuestionID() {
                return PredictionQuestionID;
            }

            public void setPredictionQuestionID(int predictionQuestionID) {
                PredictionQuestionID = predictionQuestionID;
            }

            public String getAnswer() {
                return Answer;
            }

            public void setAnswer(String answer) {
                Answer = answer;
            }

            public boolean isRight() {
                return IsRight;
            }

            public void setRight(boolean right) {
                IsRight = right;
            }
        }
    }
}
