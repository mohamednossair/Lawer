package com.lawyer.service.mapper;

import com.lawyer.domain.CaseDocument;
import com.lawyer.domain.Client;
import com.lawyer.domain.CourtCase;
import com.lawyer.domain.User;
import com.lawyer.service.dto.CaseDocumentDTO;
import com.lawyer.service.dto.ClientDTO;
import com.lawyer.service.dto.CourtCaseDTO;
import com.lawyer.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CaseDocument} and its DTO {@link CaseDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CaseDocumentMapper extends EntityMapper<CaseDocumentDTO, CaseDocument> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientClientName")
    @Mapping(target = "courtCase", source = "courtCase", qualifiedByName = "courtCaseCaseNumber")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    CaseDocumentDTO toDto(CaseDocument s);

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

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
