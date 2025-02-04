package cass.proposalapp.entity;

import jakarta.persistence.*;

@Entity
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private double requestedAmount;
    private int paymentDeadline;
    private Boolean approved;
    private boolean integrated;
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
