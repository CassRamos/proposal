package cass.proposalapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@EnableScheduling
@SpringBootApplication
@EnableWebSocketMessageBroker
public class ProposalAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProposalAppApplication.class, args);
    }

}
