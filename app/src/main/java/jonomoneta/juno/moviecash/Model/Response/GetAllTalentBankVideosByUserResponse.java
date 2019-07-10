package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetAllTalentBankVideosByUserResponse implements Serializable {

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

        @SerializedName("UserID")
        @Expose
        private int UserID;

        @SerializedName("MobileNo")
        @Expose
        private String MobileNo;

        @SerializedName("UserName")
        @Expose
        private String UserName;

        @SerializedName("ProfilePicture")
        @Expose
        private String ProfilePicture;

        @SerializedName("OriginalVideoFileName")
        @Expose
        private String OriginalVideoFileName;

        @SerializedName("VideoUrl")
        @Expose
        private String VideoUrl;

        @SerializedName("ThumbUrl")
        @Expose
        private String ThumbUrl;

        @SerializedName("CreatedDate")
        @Expose
        private String CreatedDate;

        @SerializedName("TotalRecord")
        @Expose
        private long TotalRecord;

        @SerializedName("CategoryID")
        @Expose
        private int CategoryID;

        @SerializedName("CategoryName")
        @Expose
        private String CategoryName;

        @SerializedName("FormUrl")
        @Expose
        private String FormUrl;

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

        public String getOriginalVideoFileName() {
            return OriginalVideoFileName;
        }

        public void setOriginalVideoFileName(String originalVideoFileName) {
            OriginalVideoFileName = originalVideoFileName;
        }

        public String getVideoUrl() {
            return VideoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            VideoUrl = videoUrl;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String createdDate) {
            CreatedDate = createdDate;
        }

        public long getTotalRecord() {
            return TotalRecord;
        }

        public void setTotalRecord(long totalRecord) {
            TotalRecord = totalRecord;
        }

        public String getMobileNo() {
            return MobileNo;
        }

        public void setMobileNo(String mobileNo) {
            MobileNo = mobileNo;
        }

        public String getThumbUrl() {
            return ThumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            ThumbUrl = thumbUrl;
        }

        public int getCategoryID() {
            return CategoryID;
        }

        public void setCategoryID(int categoryID) {
            CategoryID = categoryID;
        }

        public String getCategoryName() {
            return CategoryName;
        }

        public void setCategoryName(String categoryName) {
            CategoryName = categoryName;
        }

        public String getFormUrl() {
            return FormUrl;
        }

        public void setFormUrl(String formUrl) {
            FormUrl = formUrl;
        }
    }
}
