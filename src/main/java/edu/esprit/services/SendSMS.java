package edu.esprit.services;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.verify.v2.Service;
import com.twilio.rest.verify.v2.service.Verification;



public class SendSMS {
    // Find your Account SID and Auth Token at twilio.com/console
    // and set the environment variables. See http://twil.io/secure
    public static final String ACCOUNT_SID = "AC8bcc5a26e3a2915207c26a524b056e35";
    public static final String AUTH_TOKEN = "b1dcaf3e664c8d1ab5ff7759d1242bb8";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+21693007457"),
                        new com.twilio.type.PhoneNumber("+13346001421"),
                        "Votre otp est ")
                .create();

        System.out.println(message.getSid());
    }
}


//public class SendSMS {
//
//
//
//    // Find your Account Sid and Token at twilio.com/console
//    public static final String ACCOUNT_SID = "AC77f3dd603cffa1ea5f3051642badb4d8";
//    public static final String AUTH_TOKEN = "9d36cbafbe8f3a7e75f46e9a3acda130";
//
//    public void sms () {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        Message message = Message.creator(
//                        new com.twilio.type.PhoneNumber("+21693007457"),
//                        new com.twilio.type.PhoneNumber("+17244006401"),
//                        "Vous etes connecté maintenant ")
//                .create();
//
//        System.out.println(message.getSid());
//    }
//
//}
