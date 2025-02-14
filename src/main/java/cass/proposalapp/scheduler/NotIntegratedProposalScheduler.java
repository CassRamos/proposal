package cass.proposalapp.scheduler;

import cass.proposalapp.entity.Proposal;
import cass.proposalapp.repository.ProposalRepository;
import cass.proposalapp.service.RabbitMQNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NotIntegratedProposalScheduler {

    private final ProposalRepository proposalRepository;
    private final RabbitMQNotificationService rabbitMQNotificationService;
    private final String exchangeName;

    private final Logger logger = LoggerFactory.getLogger(NotIntegratedProposalScheduler.class);

    public NotIntegratedProposalScheduler(ProposalRepository proposalRepository,
                                          RabbitMQNotificationService rabbitMQNotificationService,
                                          @Value("${rabbitmq.pending-proposal.exchange}") String exchangeName) {
        this.proposalRepository = proposalRepository;
        this.rabbitMQNotificationService = rabbitMQNotificationService;
        this.exchangeName = exchangeName;
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void getNotIntegratedProposals() {
        proposalRepository.findAllByIntegratedIsFalse()
                .forEach(proposal -> {
                    try {
                        rabbitMQNotificationService.sendNotification(proposal, exchangeName);
                        updateProposal(proposal);
                    } catch (RuntimeException exception) {
                        logger.error(exception.getMessage());
                    }
                });
    }

    private void updateProposal(Proposal proposal) {
        proposal.setIntegrated(true);
        proposalRepository.save(proposal);
    }
}
