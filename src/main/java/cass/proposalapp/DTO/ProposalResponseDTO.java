package cass.proposalapp.DTO;

public record ProposalResponseDTO(
        Long id,
        String name,
        String lastName,
        String phone,
        String cpf,
        Double income,
        Double requestedAmount,
        int paymentDeadline,
        Boolean approved,
        String note
) {
}
