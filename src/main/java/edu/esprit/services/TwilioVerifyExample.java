package edu.esprit.services;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

public class TwilioVerifyExample {
    // Set your Twilio account SID, auth token, and service SID
    private static final String ACCOUNT_SID = "ACab50d6d39f63561827af3e91aee3e4d6";
    private static final String AUTH_TOKEN = "9fd34f3d4c8e4fadbc1b88330c61bdf9";
    private static final String VERIFY_SID = "VAb727e402f62b5a65a16fa24763c2d3fc";
    private static final String VERIFIED_NUMBER = "+21693007457";

    public static void main(String[] args) {
        // Initialize the Twilio client
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Send verification code
        Verification verification = Verification.creator(VERIFY_SID, VERIFIED_NUMBER, "sms").create();
        System.out.println(verification.getStatus());

        // Prompt user to enter OTP
        System.out.print("Please enter the OTP: ");
        String otpCode = System.console().readLine();

        // Check the entered OTP
        VerificationCheck verificationCheck = VerificationCheck.creator(VERIFY_SID, otpCode)
                .setTo(VERIFIED_NUMBER)
                .create();

        System.out.println(verificationCheck.getStatus());
    }
}
