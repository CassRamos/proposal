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
        return QueueBuilder.durable("pending-proposal.credit-analysis")
                .deadLetterExchange("pending-proposal.dlx")
                .build();
    }

    @Bean Queue createDeadLetterQueuePendingProposal() {
        return QueueBuilder.durable("pending-proposal.dlq").build();
    }

    @Bean
    public Queue createQueuePendingProposalNotification() {
        return QueueBuilder.durable("pending-proposal.notification").build();
    }

    @Bean
    public Queue createQueueCompletedProposalAnalysis() {
        return QueueBuilder.durable("completed-proposal.finished-analysis").build();
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

    @Bean FanoutExchange createFanoutPendingDeadLetterExchange() {
        return ExchangeBuilder.fanoutExchange("pending-proposal.dlx").build();
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
    public Binding createCompletedProposalAnalysisBinding() {
        return BindingBuilder
                .bind(createQueueCompletedProposalAnalysis())
                .to(createFanoutCompletedExchange());
    }

    @Bean
    public Binding createCompletedProposalNotificationBinding() {
        return BindingBuilder
                .bind(createQueueCompletedProposalNotification())
                .to(createFanoutCompletedExchange());
    }

    @Bean
    public Binding pendingProposalDeadLetterQueueBinding() {
        return BindingBuilder
                .bind(createDeadLetterQueuePendingProposal())
                .to(createFanoutPendingDeadLetterExchange());
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
