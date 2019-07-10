package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChargePurchaseDiamondsPaymentResponse {

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

        @SerializedName("StripeCustomerID")
        @Expose
        private String StripeCustomerID;

        @SerializedName("Email")
        @Expose
        private String Email;

        @SerializedName("AvailableDiamonds")
        @Expose
        private int AvailableDiamonds;

        @SerializedName("ChargeID")
        @Expose
        private String ChargeID;

        @SerializedName("PaymentStatus")
        @Expose
        private String PaymentStatus;

        @SerializedName("FailureCode")
        @Expose
        private int FailureCode;

        @SerializedName("FailureMessage")
        @Expose
        private String FailureMessage;

        @SerializedName("SubscriptionStartDate")
        @Expose
        private String SubscriptionStartDate;

        @SerializedName("SubscriptionEndDate")
        @Expose
        private String SubscriptionEndDate;

        @SerializedName("ReceiptUrl")
        @Expose
        private String ReceiptUrl;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getStripeCustomerID() {
            return StripeCustomerID;
        }

        public void setStripeCustomerID(String stripeCustomerID) {
            StripeCustomerID = stripeCustomerID;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public int getAvailableDiamonds() {
            return AvailableDiamonds;
        }

        public void setAvailableDiamonds(int availableDiamonds) {
            AvailableDiamonds = availableDiamonds;
        }

        public String getChargeID() {
            return ChargeID;
        }

        public void setChargeID(String chargeID) {
            ChargeID = chargeID;
        }

        public String getPaymentStatus() {
            return PaymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            PaymentStatus = paymentStatus;
        }

        public int getFailureCode() {
            return FailureCode;
        }

        public void setFailureCode(int failureCode) {
            FailureCode = failureCode;
        }

        public String getFailureMessage() {
            return FailureMessage;
        }

        public void setFailureMessage(String failureMessage) {
            FailureMessage = failureMessage;
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

        public String getReceiptUrl() {
            return ReceiptUrl;
        }

        public void setReceiptUrl(String receiptUrl) {
            ReceiptUrl = receiptUrl;
        }
    }
}
