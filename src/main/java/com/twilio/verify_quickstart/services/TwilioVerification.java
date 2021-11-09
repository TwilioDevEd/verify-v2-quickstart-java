package com.twilio.verify_quickstart.services;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import io.github.cdimascio.dotenv.Dotenv;


public class TwilioVerification implements VerificationService {
    private static Dotenv env = Dotenv.configure().ignoreIfMissing().load();
    private static final String ACCOUNT_SID = env.get("TWILIO_ACCOUNT_SID");
    private static final String AUTH_TOKEN = env.get("TWILIO_AUTH_TOKEN");
    private static final String VERIFICATION_SID = env.get("VERIFICATION_SID");

    public TwilioVerification() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public VerificationResult startVerification(String phone, String channel) {
        try {
            Verification verification = Verification.creator(VERIFICATION_SID, phone, channel).create();
            return new VerificationResult(verification.getSid());
        } catch (ApiException exception) {
            return new VerificationResult(new String[] {exception.getMessage()});
        }
    }

    public VerificationResult checkVerification(String phone, String code) {
        try {
            VerificationCheck verification = VerificationCheck.creator(VERIFICATION_SID, code).setTo(phone).create();
            if("approved".equals(verification.getStatus())) {
                return new VerificationResult(verification.getSid());
            }
            return new VerificationResult(new String[]{"Invalid code."});
        } catch (ApiException exception) {
            return new VerificationResult(new String[]{exception.getMessage()});
        }
    }
}
