package com.accenture.academico.bankingsystem.unit.email;

import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.email.service.GenerateReceiptService;
import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GenerateReceiptServiceTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Resource resource;

    @InjectMocks
    private GenerateReceiptService generateReceiptService;

    @BeforeEach
    void setUp() throws Exception {
        // Mock template content
        String templateContent = "<html><body>Test {{name}} - {{amount}}</body></html>";
        when(resource.getInputStream()).thenReturn(new java.io.ByteArrayInputStream(templateContent.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void testGenerateReceiptDepositAndSac() {
        when(resourceLoader.getResource("classpath:templates/proofDepositAndSac.html")).thenReturn(resource);

        Account account = mock(Account.class);
        Client client = mock(Client.class);
        Agency agency = mock(Agency.class);

        when(account.getClient()).thenReturn(client);
        when(client.getName()).thenReturn("John Doe");
        when(account.getAgency()).thenReturn(agency);
        when(agency.getNumber()).thenReturn("123");
        when(account.getNumber()).thenReturn("456");
        when(account.getAccountType()).thenReturn(AccountType.SAVINGS);

        String result = generateReceiptService.generateReceiptDepositAndSac(account, BigDecimal.valueOf(100.00), TransactionType.DEPOSIT, LocalDate.now());

        String expected = "<html><body>Test John Doe - 100.0</body></html>";
        assertEquals(expected, result);
    }

    @Test
    void testGenerateReceiptTransferAndPix() {
        when(resourceLoader.getResource("classpath:templates/proofTransferAndPix.html")).thenReturn(resource);

        Account accountPayer = mock(Account.class);
        Account accountRecipient = mock(Account.class);
        Agency agencyPayer = mock(Agency.class);
        Agency agencyRecipient = mock(Agency.class);

        Client clientPayer = mock(Client.class);
        Client clientRecipient = mock(Client.class);

        when(accountPayer.getClient()).thenReturn(clientPayer);
        when(clientPayer.getName()).thenReturn("John Doe");
        when(accountPayer.getAgency()).thenReturn(agencyPayer);
        when(agencyPayer.getNumber()).thenReturn("123");
        when(accountPayer.getNumber()).thenReturn("456");
        when(accountPayer.getAccountType()).thenReturn(AccountType.CURRENT);

        when(accountRecipient.getClient()).thenReturn(clientRecipient);
        when(clientRecipient.getName()).thenReturn("Jane Smith");
        when(accountRecipient.getAgency()).thenReturn(agencyRecipient);
        when(agencyRecipient.getNumber()).thenReturn("789");
        when(accountRecipient.getNumber()).thenReturn("012");
        when(accountRecipient.getAccountType()).thenReturn(AccountType.CURRENT);

        String result = generateReceiptService.generateReceiptTransferAndPix(accountPayer, BigDecimal.valueOf(100.00), accountRecipient, TransactionType.TRANSFER, LocalDate.now());

        String expected = "<html><body>Test {{name}} - 100.0</body></html>";
        assertEquals(expected, result);
    }

}
