package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDetailsResponse implements Serializable {

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

        @SerializedName("Email")
        @Expose
        private String Email;

        @SerializedName("MobileNo")
        @Expose
        private String MobileNo;

        @SerializedName("BirthDate")
        @Expose
        private String BirthDate;

        @SerializedName("MovieTypeIDs")
        @Expose
        private String MovieTypeIDs;

        @SerializedName("ProfilePicture")
        @Expose
        private String ProfilePicture;

        @SerializedName("IsActive")
        @Expose
        private String IsActive;

        @SerializedName("MoviesWatch")
        @Expose
        private String MoviesWatch;

        @SerializedName("TrailerWatch")
        @Expose
        private String TrailerWatch;

        @SerializedName("QuizWinner")
        @Expose
        private String QuizWinner;

        @SerializedName("TotalEarning")
        @Expose
        private String TotalEarning;

        @SerializedName("ReferCode")
        @Expose
        private String ReferCode;

        @SerializedName("ReferFriend")
        @Expose
        private String ReferFriend;

        @SerializedName("Gender")
        @Expose
        private String Gender;

        @SerializedName("CreatedDate")
        @Expose
        private String CreatedDate;

        @SerializedName("ModifiedDate")
        @Expose
        private String ModifiedDate;

        @SerializedName("QRCode")
        @Expose
        private String QRCode;

        @SerializedName("StripeCustomerID")
        @Expose
        private String StripeCustomerID;

        @SerializedName("AvailableDiamonds")
        @Expose
        private int AvailableDiamonds;

        @SerializedName("SubscriptionStartDate")
        @Expose
        private String SubscriptionStartDate;

        @SerializedName("SubscriptionEndDate")
        @Expose
        private String SubscriptionEndDate;

        @SerializedName("WorldsBestSubscriptionStartDate")
        @Expose
        private String WorldsBestSubscriptionStartDate;

        @SerializedName("WorldsBestSubscriptionEndDate")
        @Expose
        private String WorldsBestSubscriptionEndDate;

        @SerializedName("CardHolderName")
        @Expose
        private String CardHolderName;

        @SerializedName("CardNumberLast4")
        @Expose
        private String CardNumberLast4;

        @SerializedName("ExpiryMonth")
        @Expose
        private int ExpiryMonth;

        @SerializedName("ExpiryYear")
        @Expose
        private int ExpiryYear;

        @SerializedName("WalletAmount")
        @Expose
        private double WalletAmount;

        @SerializedName("WalletClaimStartDate")
        @Expose
        private String WalletClaimStartDate;

        @SerializedName("WalletClaimEndDate")
        @Expose
        private String WalletClaimEndDate;

        @SerializedName("EndorseCount")
        @Expose
        private long EndorseCount;

        @SerializedName("IsEnableEndorse")
        @Expose
        private boolean IsEnableEndorse;

        @SerializedName("BuzzerRefrralCount")
        @Expose
        private long BuzzerRefrralCount;

        @SerializedName("MegaWalletAmount")
        @Expose
        private double MegaWalletAmount;

        @SerializedName("IsWorldsBestRegister")
        @Expose
        private boolean IsWorldsBestRegister;

        @SerializedName("IsWorldsBestJudge")
        @Expose
        private boolean IsWorldsBestJudge;

        @SerializedName("LottoClubSubscriptionStartDate")
        @Expose
        private String LottoClubSubscriptionStartDate;

        @SerializedName("LottoClubSubscriptionEndDate")
        @Expose
        private String LottoClubSubscriptionEndDate;

        @SerializedName("IsPaidSubscription")
        @Expose
        private boolean IsPaidSubscription;

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

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public String getMobileNo() {
            return MobileNo;
        }

        public void setMobileNo(String mobileNo) {
            MobileNo = mobileNo;
        }

        public String getBirthDate() {
            return BirthDate;
        }

        public void setBirthDate(String birthDate) {
            BirthDate = birthDate;
        }

        public String getMovieTypeIDs() {
            return MovieTypeIDs;
        }

        public void setMovieTypeIDs(String movieTypeIDs) {
            MovieTypeIDs = movieTypeIDs;
        }

        public String getProfilePicture() {
            return ProfilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            ProfilePicture = profilePicture;
        }

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String isActive) {
            IsActive = isActive;
        }

        public String getMoviesWatch() {
            return MoviesWatch;
        }

        public void setMoviesWatch(String moviesWatch) {
            MoviesWatch = moviesWatch;
        }

        public String getTrailerWatch() {
            return TrailerWatch;
        }

        public void setTrailerWatch(String trailerWatch) {
            TrailerWatch = trailerWatch;
        }

        public String getTotalEarning() {
            return TotalEarning;
        }

        public void setTotalEarning(String totalEarning) {
            TotalEarning = totalEarning;
        }

        public String getReferCode() {
            return ReferCode;
        }

        public void setReferCode(String referCode) {
            ReferCode = referCode;
        }

        public String getReferFriend() {
            return ReferFriend;
        }

        public void setReferFriend(String referFriend) {
            ReferFriend = referFriend;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String createdDate) {
            CreatedDate = createdDate;
        }

        public String getModifiedDate() {
            return ModifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            ModifiedDate = modifiedDate;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String gender) {
            Gender = gender;
        }

        public String getQRCode() {
            return QRCode;
        }

        public void setQRCode(String QRCode) {
            this.QRCode = QRCode;
        }

        public String getQuizWinner() {
            return QuizWinner;
        }

        public void setQuizWinner(String quizWinner) {
            QuizWinner = quizWinner;
        }

        public String getStripeCustomerID() {
            return StripeCustomerID;
        }

        public void setStripeCustomerID(String stripeCustomerID) {
            StripeCustomerID = stripeCustomerID;
        }

        public int getAvailableDiamonds() {
            return AvailableDiamonds;
        }

        public void setAvailableDiamonds(int availableDiamonds) {
            AvailableDiamonds = availableDiamonds;
        }

        public String getSubscriptionStartDate() {
            return SubscriptionStartDate;
        }

        public void setSubscriptionStartDate(String subscriptionStartDate) {
            SubscriptionStartDate = subscriptionStartDate;
        }

        public String getSubscriptionEndDate() {
            return SubscriptionEndDate;
        }

        public void setSubscriptionEndDate(String subscriptionEndDate) {
            SubscriptionEndDate = subscriptionEndDate;
        }

        public String getCardHolderName() {
            return CardHolderName;
        }

        public void setCardHolderName(String cardHolderName) {
            CardHolderName = cardHolderName;
        }

        public String getCardNumberLast4() {
            return CardNumberLast4;
        }

        public void setCardNumberLast4(String cardNumberLast4) {
            CardNumberLast4 = cardNumberLast4;
        }

        public int getExpiryMonth() {
            return ExpiryMonth;
        }

        public void setExpiryMonth(int expiryMonth) {
            ExpiryMonth = expiryMonth;
        }

        public int getExpiryYear() {
            return ExpiryYear;
        }

        public void setExpiryYear(int expiryYear) {
            ExpiryYear = expiryYear;
        }

        public double getWalletAmount() {
            return WalletAmount;
        }

        public void setWalletAmount(double walletAmount) {
            WalletAmount = walletAmount;
        }

        public String getWalletClaimStartDate() {
            return WalletClaimStartDate;
        }

        public void setWalletClaimStartDate(String walletClaimStartDate) {
            WalletClaimStartDate = walletClaimStartDate;
        }

        public String getWalletClaimEndDate() {
            return WalletClaimEndDate;
        }

        public void setWalletClaimEndDate(String walletClaimEndDate) {
            WalletClaimEndDate = walletClaimEndDate;
        }

        public long getEndorseCount() {
            return EndorseCount;
        }

        public void setEndorseCount(long endorseCount) {
            EndorseCount = endorseCount;
        }

        public boolean isEnableEndorse() {
            return IsEnableEndorse;
        }

        public void setEnableEndorse(boolean enableEndorse) {
            IsEnableEndorse = enableEndorse;
        }

        public long getBuzzerRefrralCount() {
            return BuzzerRefrralCount;
        }

        public void setBuzzerRefrralCount(long buzzerRefrralCount) {
            BuzzerRefrralCount = buzzerRefrralCount;
        }

        public double getMegaWalletAmount() {
            return MegaWalletAmount;
        }

        public void setMegaWalletAmount(double megaWalletAmount) {
            MegaWalletAmount = megaWalletAmount;
        }

        public String getWorldsBestSubscriptionStartDate() {
            return WorldsBestSubscriptionStartDate;
        }

        public void setWorldsBestSubscriptionStartDate(String worldsBestSubscriptionStartDate) {
            WorldsBestSubscriptionStartDate = worldsBestSubscriptionStartDate;
        }

        public String getWorldsBestSubscriptionEndDate() {
            return WorldsBestSubscriptionEndDate;
        }

        public void setWorldsBestSubscriptionEndDate(String worldsBestSubscriptionEndDate) {
            WorldsBestSubscriptionEndDate = worldsBestSubscriptionEndDate;
        }

        public boolean isWorldsBestRegister() {
            return IsWorldsBestRegister;
        }

        public void setWorldsBestRegister(boolean worldsBestRegister) {
            IsWorldsBestRegister = worldsBestRegister;
        }

        public boolean isWorldsBestJudge() {
            return IsWorldsBestJudge;
        }

        public void setWorldsBestJudge(boolean worldsBestJudge) {
            IsWorldsBestJudge = worldsBestJudge;
        }

        public String getLottoClubSubscriptionStartDate() {
            return LottoClubSubscriptionStartDate;
        }

        public void setLottoClubSubscriptionStartDate(String lottoClubSubscriptionStartDate) {
            LottoClubSubscriptionStartDate = lottoClubSubscriptionStartDate;
        }

        public String getLottoClubSubscriptionEndDate() {
            return LottoClubSubscriptionEndDate;
        }

        public void setLottoClubSubscriptionEndDate(String lottoClubSubscriptionEndDate) {
            LottoClubSubscriptionEndDate = lottoClubSubscriptionEndDate;
        }

        public boolean isPaidSubscription() {
            return IsPaidSubscription;
        }

        public void setPaidSubscription(boolean paidSubscription) {
            IsPaidSubscription = paidSubscription;
        }
    }

}
