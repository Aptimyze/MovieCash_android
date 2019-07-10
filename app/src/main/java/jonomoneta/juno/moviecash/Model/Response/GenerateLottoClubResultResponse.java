package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GenerateLottoClubResultResponse implements Serializable {

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

    public class ResponseData implements Serializable{

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

        @SerializedName("MatchNo1")
        @Expose
        private boolean MatchNo1;

        @SerializedName("MatchNo2")
        @Expose
        private boolean MatchNo2;

        @SerializedName("MatchNo3")
        @Expose
        private boolean MatchNo3;

        @SerializedName("MatchNo4")
        @Expose
        private boolean MatchNo4;

        @SerializedName("MatchNo5")
        @Expose
        private boolean MatchNo5;

        @SerializedName("MatchLuckyNo")
        @Expose
        private boolean MatchLuckyNo;

        @SerializedName("TotalMatchNoCount")
        @Expose
        private int TotalMatchNoCount;

        @SerializedName("Reward")
        @Expose
        private String Reward;

        @SerializedName("DeclareResultNo")
        @Expose
        private String DeclareResultNo;

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

        public boolean isMatchNo1() {
            return MatchNo1;
        }

        public void setMatchNo1(boolean matchNo1) {
            MatchNo1 = matchNo1;
        }

        public boolean isMatchNo2() {
            return MatchNo2;
        }

        public void setMatchNo2(boolean matchNo2) {
            MatchNo2 = matchNo2;
        }

        public boolean isMatchNo3() {
            return MatchNo3;
        }

        public void setMatchNo3(boolean matchNo3) {
            MatchNo3 = matchNo3;
        }

        public boolean isMatchNo4() {
            return MatchNo4;
        }

        public void setMatchNo4(boolean matchNo4) {
            MatchNo4 = matchNo4;
        }

        public boolean isMatchNo5() {
            return MatchNo5;
        }

        public void setMatchNo5(boolean matchNo5) {
            MatchNo5 = matchNo5;
        }

        public boolean isMatchLuckyNo() {
            return MatchLuckyNo;
        }

        public void setMatchLuckyNo(boolean matchLuckyNo) {
            MatchLuckyNo = matchLuckyNo;
        }

        public int getTotalMatchNoCount() {
            return TotalMatchNoCount;
        }

        public void setTotalMatchNoCount(int totalMatchNoCount) {
            TotalMatchNoCount = totalMatchNoCount;
        }

        public String getReward() {
            return Reward;
        }

        public void setReward(String reward) {
            Reward = reward;
        }

        public String getDeclareResultNo() {
            return DeclareResultNo;
        }

        public void setDeclareResultNo(String declareResultNo) {
            DeclareResultNo = declareResultNo;
        }
    }

}
