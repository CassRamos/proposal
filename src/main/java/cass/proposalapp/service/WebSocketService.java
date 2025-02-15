package cass.proposalapp.service;

import cass.proposalapp.DTO.ProposalResponseDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void notify(ProposalResponseDTO proposalResponseDTO) {
        simpMessagingTemplate.convertAndSend("/proposals", proposalResponseDTO);
    }
}
