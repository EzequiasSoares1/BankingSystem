package com.accenture.academico.bankingsystem.rabbitMQ.consumers;
import com.accenture.academico.bankingsystem.email.dto.EmailMessageDTO;
import com.accenture.academico.bankingsystem.email.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailConsumer {
    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void listenEmail(@Payload EmailMessageDTO emailMessage) {
        log.info("Processing Receipt email for queue to: {}", emailMessage.getTo());
        emailService.sendReceiptEmail(emailMessage.getTo(),emailMessage.getSubject(),emailMessage.getHtmlContent());
    }
}
