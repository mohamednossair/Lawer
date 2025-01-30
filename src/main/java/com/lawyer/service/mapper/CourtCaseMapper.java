package com.lawyer.service.mapper;

import com.lawyer.domain.CaseStatus;
import com.lawyer.domain.Client;
import com.lawyer.domain.Court;
import com.lawyer.domain.CourtCase;
import com.lawyer.domain.CourtCaseType;
import com.lawyer.domain.Lawyer;
import com.lawyer.service.dto.CaseStatusDTO;
import com.lawyer.service.dto.ClientDTO;
import com.lawyer.service.dto.CourtCaseDTO;
import com.lawyer.service.dto.CourtCaseTypeDTO;
import com.lawyer.service.dto.CourtDTO;
import com.lawyer.service.dto.LawyerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CourtCase} and its DTO {@link CourtCaseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourtCaseMapper extends EntityMapper<CourtCaseDTO, CourtCase> {
    @Mapping(target = "court", source = "court", qualifiedByName = "courtId")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "courtCaseType", source = "courtCaseType", qualifiedByName = "courtCaseTypeId")
    @Mapping(target = "caseStatus", source = "caseStatus", qualifiedByName = "caseStatusId")
    @Mapping(target = "opponentLawyerId", source = "opponentLawyerId", qualifiedByName = "lawyerId")
    CourtCaseDTO toDto(CourtCase s);

    @Named("courtId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourtDTO toDtoCourtId(Court court);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    @Named("courtCaseTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourtCaseTypeDTO toDtoCourtCaseTypeId(CourtCaseType courtCaseType);

    @Named("caseStatusId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CaseStatusDTO toDtoCaseStatusId(CaseStatus caseStatus);

    @Named("lawyerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LawyerDTO toDtoLawyerId(Lawyer lawyer);
}
