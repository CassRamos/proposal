package cass.proposalapp.mapper;

import cass.proposalapp.DTO.ProposalRequestDTO;
import cass.proposalapp.DTO.ProposalResponseDTO;
import cass.proposalapp.entity.Proposal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProposalMapper {

    ProposalMapper INSTANCE = Mappers.getMapper(ProposalMapper.class);

    @Mapping(target = "user.name", source = "name")
    @Mapping(target = "user.lastName", source = "lastName")
    @Mapping(target = "user.cpf", source = "cpf")
    @Mapping(target = "user.phone", source = "phone")
    @Mapping(target = "user.income", source = "income")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "approved", ignore = true)
    @Mapping(target = "integrated", ignore = true)
    @Mapping(target = "note", ignore = true)
    Proposal convertDtoToEntity(ProposalRequestDTO proposalRequestDTO);

    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "cpf", source = "user.cpf")
    @Mapping(target = "income", source = "user.income")
    ProposalResponseDTO convertEntityToDto(Proposal proposal);

}
