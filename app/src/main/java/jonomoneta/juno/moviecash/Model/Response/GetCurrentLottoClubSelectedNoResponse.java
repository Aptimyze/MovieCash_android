package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetCurrentLottoClubSelectedNoResponse implements Serializable {

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

        @SerializedName("UserID")
        @Expose
        private int UserID;

        @SerializedName("No1")
        @Expose
        private String No1;

        @SerializedName("No2")
        @Expose
        private String No2;

        @SerializedName("No3")
        @Expose
        private String No3;

        @SerializedName("No4")
        @Expose
        private String No4;

        @SerializedName("No5")
        @Expose
        private String No5;

        @SerializedName("LuckyNo")
        @Expose
        private String LuckyNo;

        @SerializedName("IsResultDeclare")
        @Expose
        private boolean IsResultDeclare;

        @SerializedName("IsResultChecked")
        @Expose
        private boolean IsResultChecked;

        @SerializedName("CurrentUTCDateTime")
        @Expose
        private String CurrentUTCDateTime;

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

        public String getNo1() {
            return No1;
        }

        public void setNo1(String no1) {
            No1 = no1;
        }

        public String getNo2() {
            return No2;
        }

        public void setNo2(String no2) {
            No2 = no2;
        }

        public String getNo3() {
            return No3;
        }

        public void setNo3(String no3) {
            No3 = no3;
        }

        public String getNo4() {
            return No4;
        }

        public void setNo4(String no4) {
            No4 = no4;
        }

        public String getNo5() {
            return No5;
        }

        public void setNo5(String no5) {
            No5 = no5;
        }

        public String getLuckyNo() {
            return LuckyNo;
        }

        public void setLuckyNo(String luckyNo) {
            LuckyNo = luckyNo;
        }

        public boolean isResultDeclare() {
            return IsResultDeclare;
        }

        public void setResultDeclare(boolean resultDeclare) {
            IsResultDeclare = resultDeclare;
        }

        public boolean isResultChecked() {
            return IsResultChecked;
        }

        public void setResultChecked(boolean resultChecked) {
            IsResultChecked = resultChecked;
        }

        public String getCurrentUTCDateTime() {
            return CurrentUTCDateTime;
        }

        public void setCurrentUTCDateTime(String currentUTCDateTime) {
            CurrentUTCDateTime = currentUTCDateTime;
        }
    }
}
