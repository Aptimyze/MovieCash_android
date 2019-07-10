package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LottoHistoryResponse implements Serializable {


    @SerializedName("ResponseID")
    @Expose
    private int ResponseID;

    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;

    @SerializedName("ResponseData")
    @Expose
    private ArrayList<ResponseData> responseDataArrayList;

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

    public ArrayList<ResponseData> getResponseDataArrayList() {
        return responseDataArrayList;
    }

    public void setResponseDataArrayList(ArrayList<ResponseData> responseDataArrayList) {
        this.responseDataArrayList = responseDataArrayList;
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

        @SerializedName("MatchNo1")
        @Expose
        private String MatchNo1;

        @SerializedName("MatchNo2")
        @Expose
        private String MatchNo2;

        @SerializedName("MatchNo3")
        @Expose
        private String MatchNo3;

        @SerializedName("MatchNo4")
        @Expose
        private String MatchNo4;

        @SerializedName("MatchNo5")
        @Expose
        private String MatchNo5;

        @SerializedName("MatchLuckyNo")
        @Expose
        private String MatchLuckyNo;

        @SerializedName("Reward")
        @Expose
        private String Reward;

        @SerializedName("TotalMatchNo")
        @Expose
        private int TotalMatchNo;

        @SerializedName("CreatedDate")
        @Expose
        private String CreatedDate;

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

        public String getMatchNo1() {
            return MatchNo1;
        }

        public void setMatchNo1(String matchNo1) {
            MatchNo1 = matchNo1;
        }

        public String getMatchNo2() {
            return MatchNo2;
        }

        public void setMatchNo2(String matchNo2) {
            MatchNo2 = matchNo2;
        }

        public String getMatchNo3() {
            return MatchNo3;
        }

        public void setMatchNo3(String matchNo3) {
            MatchNo3 = matchNo3;
        }

        public String getMatchNo4() {
            return MatchNo4;
        }

        public void setMatchNo4(String matchNo4) {
            MatchNo4 = matchNo4;
        }

        public String getMatchNo5() {
            return MatchNo5;
        }

        public void setMatchNo5(String matchNo5) {
            MatchNo5 = matchNo5;
        }

        public String getMatchLuckyNo() {
            return MatchLuckyNo;
        }

        public void setMatchLuckyNo(String matchLuckyNo) {
            MatchLuckyNo = matchLuckyNo;
        }

        public String getReward() {
            return Reward;
        }

        public void setReward(String reward) {
            Reward = reward;
        }

        public int getTotalMatchNo() {
            return TotalMatchNo;
        }

        public void setTotalMatchNo(int totalMatchNo) {
            TotalMatchNo = totalMatchNo;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String createdDate) {
            CreatedDate = createdDate;
        }
    }


}
