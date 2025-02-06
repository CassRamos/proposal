package cass.proposalapp.controller;

import cass.proposalapp.DTO.ProposalRequestDTO;
import cass.proposalapp.DTO.ProposalResponseDTO;
import cass.proposalapp.service.ProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/proposals")
@RequiredArgsConstructor
public class ProposalController {

    private final ProposalService proposalService;

    @PostMapping
    public ResponseEntity<ProposalResponseDTO> createProposal(@RequestBody ProposalRequestDTO request) {
        ProposalResponseDTO response = proposalService.createProposal(request);

        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri()).build();
    }

    @GetMapping
    public ResponseEntity<List<ProposalResponseDTO>> getAllProposals() {
        return ResponseEntity.ok(proposalService.getAllProposals());
    }
}
