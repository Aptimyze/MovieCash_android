package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizResponse implements Serializable {

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

        @SerializedName("QuizGameType")
        @Expose
        private String QuizGameType;

        @SerializedName("Name")
        @Expose
        private String Name;

        @SerializedName("VideoUrl")
        @Expose
        private String VideoUrl;

        @SerializedName("ReleaseDate")
        @Expose
        private String ReleaseDate;

        @SerializedName("QuizGameLevel")
        @Expose
        private int QuizGameLevel;

        @SerializedName("DividedByRule")
        @Expose
        private int DividedByRule;

        @SerializedName("QuestionList")
        @Expose
        private ArrayList<QuestionList> QuestionList;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getQuizGameType() {
            return QuizGameType;
        }

        public void setQuizGameType(String quizGameType) {
            QuizGameType = quizGameType;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getVideoUrl() {
            return VideoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            VideoUrl = videoUrl;
        }

        public String getReleaseDate() {
            return ReleaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            ReleaseDate = releaseDate;
        }

        public int getQuizGameLevel() {
            return QuizGameLevel;
        }

        public void setQuizGameLevel(int quizGameLevel) {
            QuizGameLevel = quizGameLevel;
        }

        public int getDividedByRule() {
            return DividedByRule;
        }

        public void setDividedByRule(int dividedByRule) {
            DividedByRule = dividedByRule;
        }

        public ArrayList<ResponseData.QuestionList> getQuestionList() {
            return QuestionList;
        }

        public void setQuestionList(ArrayList<ResponseData.QuestionList> questionList) {
            QuestionList = questionList;
        }

        public class QuestionList implements Serializable {

            @SerializedName("ID")
            @Expose
            private int ID;

            @SerializedName("QuizGameID")
            @Expose
            private int QuizGameID;

            @SerializedName("Question")
            @Expose
            private String Question;

            @SerializedName("AnswersList")
            @Expose
            private ArrayList<AnswersList> AnswersList;

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public int getQuizGameID() {
                return QuizGameID;
            }

            public void setQuizGameID(int quizGameID) {
                QuizGameID = quizGameID;
            }

            public String getQuestion() {
                return Question;
            }

            public void setQuestion(String question) {
                Question = question;
            }

            public ArrayList<AnswersList> getAnswersList() {
                return AnswersList;
            }

            public void setAnswersList(ArrayList<AnswersList> answersList) {
                AnswersList = answersList;
            }

            public class AnswersList implements Serializable {

                @SerializedName("ID")
                @Expose
                private int ID;

                @SerializedName("QuizGameID")
                @Expose
                private int QuizGameID;

                @SerializedName("QuizGameQuestionID")
                @Expose
                private int QuizGameQuestionID;

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

                public int getQuizGameID() {
                    return QuizGameID;
                }

                public void setQuizGameID(int quizGameID) {
                    QuizGameID = quizGameID;
                }

                public int getQuizGameQuestionID() {
                    return QuizGameQuestionID;
                }

                public void setQuizGameQuestionID(int quizGameQuestionID) {
                    QuizGameQuestionID = quizGameQuestionID;
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
}
