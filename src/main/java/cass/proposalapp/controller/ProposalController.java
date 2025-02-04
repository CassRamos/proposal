package cass.proposalapp.controller;

import cass.proposalapp.DTO.ProposalRequestDTO;
import cass.proposalapp.DTO.ProposalResponseDTO;
import cass.proposalapp.service.ProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proposals")
@RequiredArgsConstructor
public class ProposalController {

    private final ProposalService proposalService;

    @PostMapping
    public ResponseEntity<ProposalResponseDTO> createProposal(@RequestBody ProposalRequestDTO request) {
        return ResponseEntity.ok(proposalService.createProposal(request));
    }
}
