package com.lawyer.service.mapper;

import com.lawyer.domain.CaseSession;
import com.lawyer.domain.CourtCase;
import com.lawyer.service.dto.CaseSessionDTO;
import com.lawyer.service.dto.CourtCaseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CaseSession} and its DTO {@link CaseSessionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CaseSessionMapper extends EntityMapper<CaseSessionDTO, CaseSession> {
    @Mapping(target = "courtCase", source = "courtCase", qualifiedByName = "courtCaseId")
    CaseSessionDTO toDto(CaseSession s);

    @Named("courtCaseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourtCaseDTO toDtoCourtCaseId(CourtCase courtCase);
}
