package cass.proposalapp.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String name;
    private String lastName;
    private String CPF;
    private String phone;
    private double income;

    @OneToMany(mappedBy = "user")
    private List<Proposal> proposalList;
}
