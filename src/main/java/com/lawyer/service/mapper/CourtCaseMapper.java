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
    @Mapping(target = "court", source = "court", qualifiedByName = "courtCourtName")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientClientName")
    @Mapping(target = "courtCaseType", source = "courtCaseType", qualifiedByName = "courtCaseTypeCaseTypeName")
    @Mapping(target = "caseStatus", source = "caseStatus", qualifiedByName = "caseStatusCaseStatusName")
    @Mapping(target = "opponentLawyerId", source = "opponentLawyerId", qualifiedByName = "lawyerLawyerName")
    CourtCaseDTO toDto(CourtCase s);

    @Named("courtCourtName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "courtName", source = "courtName")
    CourtDTO toDtoCourtCourtName(Court court);

    @Named("clientClientName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "clientName", source = "clientName")
    ClientDTO toDtoClientClientName(Client client);

    @Named("courtCaseTypeCaseTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "caseTypeName", source = "caseTypeName")
    CourtCaseTypeDTO toDtoCourtCaseTypeCaseTypeName(CourtCaseType courtCaseType);

    @Named("caseStatusCaseStatusName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "caseStatusName", source = "caseStatusName")
    CaseStatusDTO toDtoCaseStatusCaseStatusName(CaseStatus caseStatus);

    @Named("lawyerLawyerName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "lawyerName", source = "lawyerName")
    LawyerDTO toDtoLawyerLawyerName(Lawyer lawyer);
}
