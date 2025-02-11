package cass.proposalapp.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Value("${rabbitmq.pending-proposal.exchange}")
    private String pendingProposalExchange;

    @Value("${rabbitmq.completed-proposal.exchange}")
    private String completedProposalExchange;

    private ConnectionFactory connectionFactory;

    public RabbitMQConfiguration(ConnectionFactory connectionFactory) {
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
    public FanoutExchange createFanoutPendingExchange() {
        return ExchangeBuilder.fanoutExchange(pendingProposalExchange).build();
    }

    @Bean
    public FanoutExchange createFanoutCompletedExchange() {
        return ExchangeBuilder.fanoutExchange(completedProposalExchange).build();
    }

    @Bean
    public Binding createPendingCreditAnalysisBinding() {
        return BindingBuilder
                .bind(createQueuePendingCreditAnalysisProposal())
                .to(createFanoutPendingExchange());
    }

    @Bean
    public Binding createPendingProposalNotificationBinding() {
        return BindingBuilder
                .bind(createQueuePendingProposalNotification())
                .to(createFanoutPendingExchange());
    }

    @Bean
    public Binding createCompletedProposalBinding() {
        return BindingBuilder
                .bind(createQueueCompletedProposal())
                .to(createFanoutCompletedExchange());
    }

    @Bean
    public Binding createCompletedProposalNotificationBinding() {
        return BindingBuilder
                .bind(createQueueCompletedProposalNotification())
                .to(createFanoutCompletedExchange());
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
