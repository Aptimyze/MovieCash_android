package jonomoneta.juno.moviecash.Model.Request;

public class VerifyOtpRequest {

    String mobileno, otpcode;

    public VerifyOtpRequest(String mobileno, String otpcode) {
        this.mobileno = mobileno;
        this.otpcode = otpcode;
    }
}
