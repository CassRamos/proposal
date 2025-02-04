package cass.proposalapp.DTO;

public record ProposalRequestDTO(
        String name,
        String lastName,
        String phone,
        String cpf,
        Double income,
        Double requestedAmount,
        int paymentDeadline
) {
}
