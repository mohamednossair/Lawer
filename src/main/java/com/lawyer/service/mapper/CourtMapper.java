package com.lawyer.service.mapper;

import com.lawyer.domain.Court;
import com.lawyer.service.dto.CourtDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Court} and its DTO {@link CourtDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourtMapper extends EntityMapper<CourtDTO, Court> {}
