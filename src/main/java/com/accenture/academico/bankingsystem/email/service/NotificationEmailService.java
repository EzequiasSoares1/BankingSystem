package com.accenture.academico.bankingsystem.email.service;
import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransferResponseDTO;
import com.accenture.academico.bankingsystem.email.dto.EmailMessageDTO;
import com.accenture.academico.bankingsystem.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class NotificationEmailService {

    private final RabbitTemplate rabbitTemplate;
    private final GenerateReceiptService generateReceiptService;
    private final AccountRepository accountRepository;

    @Value("${spring.rabbitmq.queue}")
    private String queueName;

    public NotificationEmailService(RabbitTemplate rabbitTemplate, GenerateReceiptService generateReceiptService,AccountRepository accountRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.generateReceiptService = generateReceiptService;
        this.accountRepository = accountRepository;
    }

    public void sendReceipt(TransactionResponseDTO transactionDTO) {
        Account account = accountRepository.getReferenceById(transactionDTO.accountId());
        String userEmail = account.getClient().getUser().getEmail();

        log.debug("Generating receipt for account ID: {} with email: {}", account.getId(), userEmail);

        String receiptHtml = generateReceiptService.generateReceiptDepositAndSac(
                account,
                transactionDTO.valueTransaction(),
                transactionDTO.transactionType(),
                transactionDTO.dataTransaction().toLocalDate()
        );

        log.debug("Generated receipt HTML content: {}", receiptHtml);

        toSendEmail(userEmail,"Comprovante de Movimentação Bancaria", receiptHtml);
    }

    public void sendReceiptTransfer(TransferResponseDTO transactionDTO) {
        Account accountPayer = accountRepository.getReferenceById(transactionDTO.senderId());
        Account accountRecevied = accountRepository.getReferenceById(transactionDTO.receiverId());

        String userEmail = accountPayer.getClient().getUser().getEmail();

        log.debug("Generating transfer receipt for payer account ID: {} with email: {}", accountPayer.getId(), userEmail);

        String receiptHtml = generateReceiptService.generateReceiptTransferAndPix(
                accountPayer,
                transactionDTO.valueTransaction(),
                accountRecevied,
                transactionDTO.transactionType(),
                transactionDTO.dataTransaction().toLocalDate()
        );

        log.debug("Generated transfer receipt HTML content: {}", receiptHtml);

        toSendEmail(userEmail,"Comprovante de Transferecia Bancaria", receiptHtml);
    }

    private void sendToQueue(EmailMessageDTO emailMessageDTO) {
        log.debug("Sending email message to queue: {}", emailMessageDTO);

        rabbitTemplate.convertAndSend(queueName, emailMessageDTO);
    }

    private void toSendEmail(String email, String subject, String htmlContent ){
        log.debug("Preparing to send email to: {} with subject: {}", email, subject);

        try {
            sendToQueue(new EmailMessageDTO(email, subject, htmlContent));
            log.info("Email from {} sent to queue successfully ", email);
        } catch (Exception e) {
            log.error("Failed to send email from {} to queue: {}", email, e);
        }
    }
}
