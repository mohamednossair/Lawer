package com.lawyer.service.mapper;

import com.lawyer.domain.CaseStatus;
import com.lawyer.service.dto.CaseStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CaseStatus} and its DTO {@link CaseStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface CaseStatusMapper extends EntityMapper<CaseStatusDTO, CaseStatus> {}
