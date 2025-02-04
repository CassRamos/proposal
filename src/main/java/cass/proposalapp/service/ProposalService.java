package cass.proposalapp.service;

import cass.proposalapp.DTO.ProposalRequestDTO;
import cass.proposalapp.DTO.ProposalResponseDTO;
import cass.proposalapp.entity.Proposal;
import cass.proposalapp.mapper.ProposalMapper;
import cass.proposalapp.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository proposalRepository;

    public ProposalResponseDTO createProposal(ProposalRequestDTO request) {
        Proposal proposal = ProposalMapper.INSTANCE.convertDtoToEntity(request);
        proposalRepository.save(proposal);

        return ProposalMapper.INSTANCE.convertEntityToDto(proposal);
    }
}
