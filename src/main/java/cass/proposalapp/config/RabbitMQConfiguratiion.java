package cass.proposalapp.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguratiion {

    private ConnectionFactory connectionFactory;

    public RabbitMQConfiguratiion(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Bean
    public Queue createQueuePendingCreditAnalysisProposal() {
        return QueueBuilder.durable("pending-proposal.credit-analysis").build();
    }

    @Bean
    public Queue createQueuePendingProposalNotification() {
        return QueueBuilder.durable("pending-proposal.notification").build();
    }

    @Bean
    public Queue createQueueCompletedProposal() {
        return QueueBuilder.durable("completed-proposal.proposal").build();
    }

    @Bean
    public Queue createQueueCompletedProposalNotification() {
        return QueueBuilder.durable("completed-proposal.notification").build();
    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public FanoutExchange createFanoutExchange() {
        return new FanoutExchange("pending-proposal.exchange");
    }

    @Bean
    public Binding createPendingCreditAnalysisBinding() {
        return BindingBuilder
                .bind(createQueuePendingCreditAnalysisProposal())
                .to(createFanoutExchange());
    }

    @Bean
    public Binding createPendingProposalNotificationBinding() {
        return BindingBuilder
                .bind(createQueuePendingProposalNotification())
                .to(createFanoutExchange());
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();

        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
