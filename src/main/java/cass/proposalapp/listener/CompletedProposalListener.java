package cass.proposalapp.listener;

import cass.proposalapp.entity.Proposal;
import cass.proposalapp.repository.ProposalRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CompletedProposalListener {

    private final ProposalRepository proposalRepository;

    public CompletedProposalListener(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.entity-proposal}")
    public void completedProposal(Proposal proposal) {
        proposalRepository.save(proposal);
    }
}
