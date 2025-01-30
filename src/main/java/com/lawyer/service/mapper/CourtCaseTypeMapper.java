package com.lawyer.service.mapper;

import com.lawyer.domain.CourtCaseType;
import com.lawyer.service.dto.CourtCaseTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CourtCaseType} and its DTO {@link CourtCaseTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourtCaseTypeMapper extends EntityMapper<CourtCaseTypeDTO, CourtCaseType> {}
