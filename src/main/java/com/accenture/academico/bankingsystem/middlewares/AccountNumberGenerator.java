package com.accenture.academico.bankingsystem.middlewares;

import java.security.SecureRandom;
import java.util.Random;

public class AccountNumberGenerator {

    private static final int ACCOUNT_NUMBER_LENGTH = 10;
    private static final Random RANDOM = new SecureRandom();

    public static String generateAccountNumber() {
        StringBuilder accountNumber = new StringBuilder(ACCOUNT_NUMBER_LENGTH);

        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            accountNumber.append(RANDOM.nextInt(10));
        }

        return accountNumber.toString();
    }
}
