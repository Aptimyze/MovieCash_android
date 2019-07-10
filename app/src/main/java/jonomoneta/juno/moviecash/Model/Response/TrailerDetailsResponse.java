package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TrailerDetailsResponse implements Serializable {

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

    public static class ResponseData implements Serializable {

        @SerializedName("ID")
        @Expose
        private int ID;

        @SerializedName("MovieName")
        @Expose
        private String MovieName;

        @SerializedName("MovieID")
        @Expose
        private int MovieID;

        @SerializedName("VideoUrl")
        @Expose
        private String VideoUrl;

        @SerializedName("CurrentTrailerWatchCount")
        @Expose
        private int CurrentTrailerWatchCount;

        @SerializedName("UserCommentList")
        @Expose
        private ArrayList<UserCommentList> userCommentList;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getMovieName() {
            return MovieName;
        }

        public void setMovieName(String movieName) {
            MovieName = movieName;
        }

        public int getMovieID() {
            return MovieID;
        }

        public void setMovieID(int movieID) {
            MovieID = movieID;
        }

        public String getVideoUrl() {
            return VideoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            VideoUrl = videoUrl;
        }

        public ArrayList<UserCommentList> getUserCommentList() {
            return userCommentList;
        }

        public void setUserCommentList(ArrayList<UserCommentList> userCommentList) {
            this.userCommentList = userCommentList;
        }

        public int getCurrentTrailerWatchCount() {
            return CurrentTrailerWatchCount;
        }

        public void setCurrentTrailerWatchCount(int currentTrailerWatchCount) {
            CurrentTrailerWatchCount = currentTrailerWatchCount;
        }

        public static class UserCommentList implements Serializable {

            @SerializedName("CommentID")
            @Expose
            private int CommentID;

            @SerializedName("Comments")
            @Expose
            private String Comments;

            @SerializedName("UserID")
            @Expose
            private int UserID;

            @SerializedName("UserName")
            @Expose
            private String UserName;

            @SerializedName("ProfilePicture")
            @Expose
            private String ProfilePicture;

            @SerializedName("CommentDate")
            @Expose
            private String CommentDate;

            @SerializedName("CreatedDate")
            @Expose
            private String CreatedDate;


            public int getCommentID() {
                return CommentID;
            }

            public void setCommentID(int commentID) {
                CommentID = commentID;
            }

            public String getComments() {
                return Comments;
            }

            public void setComments(String comments) {
                Comments = comments;
            }

            public int getUserID() {
                return UserID;
            }

            public void setUserID(int userID) {
                UserID = userID;
            }

            public String getUserName() {
                return UserName;
            }

            public void setUserName(String userName) {
                UserName = userName;
            }

            public String getProfilePicture() {
                return ProfilePicture;
            }

            public void setProfilePicture(String profilePicture) {
                ProfilePicture = profilePicture;
            }

            public String getCreatedDate() {
                return CreatedDate;
            }

            public void setCreatedDate(String createdDate) {
                CreatedDate = createdDate;
            }

            public String getCommentDate() {
                return CommentDate;
            }

            public void setCommentDate(String commentDate) {
                CommentDate = commentDate;
            }
        }
    }
}
