package com.lawyer.service.mapper;

import com.lawyer.domain.CaseDocument;
import com.lawyer.domain.CourtCase;
import com.lawyer.domain.User;
import com.lawyer.service.dto.CaseDocumentDTO;
import com.lawyer.service.dto.CourtCaseDTO;
import com.lawyer.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CaseDocument} and its DTO {@link CaseDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CaseDocumentMapper extends EntityMapper<CaseDocumentDTO, CaseDocument> {
    @Mapping(target = "courtCase", source = "courtCase", qualifiedByName = "courtCaseId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    CaseDocumentDTO toDto(CaseDocument s);

    @Named("courtCaseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourtCaseDTO toDtoCourtCaseId(CourtCase courtCase);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
