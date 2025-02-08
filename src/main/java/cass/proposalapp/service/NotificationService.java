package cass.proposalapp.service;

import cass.proposalapp.DTO.ProposalResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {

    private final RabbitTemplate rabbitTemplate;

    public void sendNotification(ProposalResponseDTO proposal, String exchange) {
        rabbitTemplate.convertAndSend(exchange, "", proposal);
    }
}
