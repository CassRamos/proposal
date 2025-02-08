package cass.proposalapp.service;

import cass.proposalapp.DTO.ProposalRequestDTO;
import cass.proposalapp.DTO.ProposalResponseDTO;
import cass.proposalapp.entity.Proposal;
import cass.proposalapp.mapper.ProposalMapper;
import cass.proposalapp.repository.ProposalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final RabbitMQNotificationService rabbitMQNotificationService;
    private final String exchangeName;

    public ProposalService(ProposalRepository proposalRepository,
                           RabbitMQNotificationService rabbitMQNotificationService,
                           @Value("${rabbitmq.pending-proposal.exchange}") String exchangeName) {
        this.proposalRepository = proposalRepository;
        this.rabbitMQNotificationService = rabbitMQNotificationService;
        this.exchangeName = exchangeName;
    }

    public ProposalResponseDTO createProposal(ProposalRequestDTO request) {
        Proposal proposal = ProposalMapper.INSTANCE.convertDtoToEntity(request);
        proposalRepository.save(proposal);

        notifyRabbitMQ(proposal);

        return ProposalMapper.INSTANCE.convertEntityToDto(proposal);
    }

    private void notifyRabbitMQ(Proposal proposal) {
        try {
            rabbitMQNotificationService.sendNotification(proposal, exchangeName);
        } catch (RuntimeException exception) {
            proposal.setIntegrated(false);
            proposalRepository.save(proposal);
        }
    }

    public List<ProposalResponseDTO> getAllProposals() {

        Iterable<Proposal> proposals = proposalRepository.findAll();

        return ProposalMapper.INSTANCE.convertEntityListToDtoList(proposals);
    }
}
