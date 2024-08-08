package com.accenture.academico.bankingsystem.unit.middlewares;
import com.accenture.academico.bankingsystem.middlewares.AccountNumberGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountNumberGeneratorTest {

    @Test
    void generateAccountNumber_ShouldReturnNumberWithCorrectLength() {
        String accountNumber = AccountNumberGenerator.generateAccountNumber();
        assertEquals(10, accountNumber.length(), "Account number should be 10 digits long");
    }

    @Test
    void generateAccountNumber_ShouldReturnNumericString() {
        String accountNumber = AccountNumberGenerator.generateAccountNumber();
        assertTrue(accountNumber.matches("\\d+"), "Account number should contain only digits");
    }

    @Test
    void generateAccountNumber_ShouldGenerateUniqueNumbers() {
        // Generate a set of account numbers
        final int numberOfAccounts = 1000;
        final String[] accountNumbers = new String[numberOfAccounts];

        for (int i = 0; i < numberOfAccounts; i++) {
            accountNumbers[i] = AccountNumberGenerator.generateAccountNumber();
        }

        // Check if all account numbers are unique
        for (int i = 0; i < numberOfAccounts; i++) {
            for (int j = i + 1; j < numberOfAccounts; j++) {
                assertTrue(!accountNumbers[i].equals(accountNumbers[j]), "Account numbers should be unique");
            }
        }
    }
}
