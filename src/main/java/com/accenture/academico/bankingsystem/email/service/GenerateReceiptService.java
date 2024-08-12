package com.accenture.academico.bankingsystem.email.service;
import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Service
@Slf4j
public class GenerateReceiptService {
    @Autowired
    private ResourceLoader resourceLoader;

    private String generateHtml(Map<String, String> dados, String templatePath) {

        try {
            Resource resource = resourceLoader.getResource(templatePath);
            log.debug("Loading template from path: {}", templatePath);

            String template;
            try (Scanner scanner = new Scanner(resource.getInputStream(), StandardCharsets.UTF_8.name())) {
                template = scanner.useDelimiter("\\A").next();
            }

            log.debug("Template loaded successfully, proceeding to replace placeholders");

            for (Map.Entry<String, String> entry : dados.entrySet()) {
                template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }

            log.debug("Generate Receipt");

            return template;
        } catch (Exception e) {
            log.error("Error Generate Receipt: " + e.getMessage());
            return null;
        }
    }

    private String generateDepositAndSac(String name, String agency, String numbAccount,String accountType,
       String amount, String typeTransaction, String datePayment) {

        Map<String, String> data = new HashMap<>();
        data.put("name", name);
        data.put("numbAgency", agency);
        data.put("numbAccount", numbAccount);
        data.put("accountType", accountType);
        data.put("amount", amount);
        data.put("typeTransaction", typeTransaction);
        data.put("datePayment", datePayment);

        return generateHtml(data, "classpath:templates/proofDepositAndSac.html");
    }

    private String generateTransferAndPix(String namePayer, String numbAgencyPayer, String numbAccountPayer, String accountTypePayer,
    String nameRecipient, String numbAgencyRecipient, String numbAccountRecipient, String accountTypeRecipient, String amount, String typeTransaction, String datePayment) {

        Map<String, String> data = new HashMap<>();
        data.put("namePayer", namePayer);
        data.put("numbAgencyPayer", numbAgencyPayer);
        data.put("numbAccountPayer", numbAccountPayer);
        data.put("accountTypePayer", accountTypePayer);
        data.put("nameRecipient", nameRecipient);
        data.put("numbAgencyRecipient", numbAgencyRecipient);
        data.put("numbAccountRecipient", numbAccountRecipient);
        data.put("accountTypeRecipient", accountTypeRecipient);
        data.put("amount", amount);
        data.put("typeTransaction", typeTransaction);
        data.put("datePayment", datePayment);
        return generateHtml(data,"classpath:templates/proofTransferAndPix.html");
    }

    public String generateReceiptTransferAndPix(Account accountPayer, BigDecimal amount,  Account accountRecipient, TransactionType transactionType, LocalDate dataPayment){

        return generateTransferAndPix(
                accountPayer.getClient().getName(),
                accountPayer.getAgency().getNumber(),
                accountPayer.getNumber(),
                accountPayer.getAccountType().toString(),
                accountRecipient.getClient().getName(),
                accountRecipient.getAgency().getNumber(),
                accountRecipient.getNumber(),
                accountRecipient.getAccountType().toString(),
                String.valueOf(amount.floatValue()),
                transactionType.toString(),
                dataPayment.toString()
        );
    }

    public String generateReceiptDepositAndSac(Account account, BigDecimal amount, TransactionType transactionType, LocalDate dataPayment){
        return generateDepositAndSac(
                account.getClient().getName(),
                account.getAgency().getNumber(),
                account.getNumber(),
                account.getAccountType().toString(),
                String.valueOf(amount.floatValue()),
                transactionType.toString(),
                dataPayment.toString()
        );
    }


}

