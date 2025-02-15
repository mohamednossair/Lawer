package com.lawyer.service.mapper;

import com.lawyer.domain.CaseSession;
import com.lawyer.domain.Client;
import com.lawyer.domain.CourtCase;
import com.lawyer.service.dto.CaseSessionDTO;
import com.lawyer.service.dto.ClientDTO;
import com.lawyer.service.dto.CourtCaseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CaseSession} and its DTO {@link CaseSessionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CaseSessionMapper extends EntityMapper<CaseSessionDTO, CaseSession> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientClientName")
    @Mapping(target = "courtCase", source = "courtCase", qualifiedByName = "courtCaseCaseNumber")
    CaseSessionDTO toDto(CaseSession s);

    @Named("clientClientName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "clientName", source = "clientName")
    ClientDTO toDtoClientClientName(Client client);

    @Named("courtCaseCaseNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "caseNumber", source = "caseNumber")
    CourtCaseDTO toDtoCourtCaseCaseNumber(CourtCase courtCase);
}
