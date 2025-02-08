package cass.proposalapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProposalAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProposalAppApplication.class, args);
    }

}
