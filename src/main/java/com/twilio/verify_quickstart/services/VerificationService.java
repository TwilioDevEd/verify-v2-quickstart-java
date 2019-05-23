package com.twilio.verify_quickstart.services;

public interface VerificationService {

    VerificationResult startVerification(String phoneNumber, String channel);

    VerificationResult checkVerification(String phoneNumber, String code);
}
