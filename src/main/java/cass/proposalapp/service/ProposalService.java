package cass.proposalapp.service;

import cass.proposalapp.DTO.ProposalRequestDTO;
import cass.proposalapp.DTO.ProposalResponseDTO;
import cass.proposalapp.entity.Proposal;
import cass.proposalapp.mapper.ProposalMapper;
import cass.proposalapp.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final NotificationService notificationService;

    public ProposalResponseDTO createProposal(ProposalRequestDTO request) {
        Proposal proposal = ProposalMapper.INSTANCE.convertDtoToEntity(request);
        proposalRepository.save(proposal);

        ProposalResponseDTO response = ProposalMapper.INSTANCE.convertEntityToDto(proposal);
        notificationService.sendNotification(response, "pending-proposal.exchange");

        return ProposalMapper.INSTANCE.convertEntityToDto(proposal);
    }

    public List<ProposalResponseDTO> getAllProposals() {

        Iterable<Proposal> proposals = proposalRepository.findAll();

        return ProposalMapper.INSTANCE.convertEntityListToDtoList(proposals);
    }
}
