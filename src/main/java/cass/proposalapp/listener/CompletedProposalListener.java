package cass.proposalapp.listener;

import cass.proposalapp.DTO.ProposalResponseDTO;
import cass.proposalapp.entity.Proposal;
import cass.proposalapp.mapper.ProposalMapper;
import cass.proposalapp.repository.ProposalRepository;
import cass.proposalapp.service.WebSocketService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CompletedProposalListener {

    private final ProposalRepository proposalRepository;
    private final WebSocketService webSocketService;

    public CompletedProposalListener(ProposalRepository proposalRepository,
                                     WebSocketService webSocketService) {
        this.proposalRepository = proposalRepository;
        this.webSocketService = webSocketService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.entity-proposal}")
    public void completedProposal(Proposal proposal) {
        proposalRepository.save(proposal);
        ProposalResponseDTO proposalResponseDTO = ProposalMapper.INSTANCE.convertEntityToDto(proposal);
        webSocketService.notify(proposalResponseDTO);
    }

    private void updateProposal(Proposal proposal) {
        proposalRepository.updateProposal(proposal.getId(), proposal.getApproved(), proposal.getNote());
    }
}
