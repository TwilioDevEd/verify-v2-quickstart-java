package com.twilio.verify_quickstart.services;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

public class PhoneVerification {

    private static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    private static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    private static final String VERIFICATION_SID = System.getenv("VERIFICATION_SID");

    public PhoneVerification() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public Verification startVerification(String phone, String channel) {
        return Verification.creator(VERIFICATION_SID, phone, channel).create();
    }

    public VerificationCheck checkVerification(String phone, String code) {
        try {
            return VerificationCheck.creator(VERIFICATION_SID, code).setTo(phone).create();
        } catch (ApiException e) {
            return null;
        }
    }
}
