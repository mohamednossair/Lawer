package com.lawyer.service.mapper;

import com.lawyer.domain.Lawyer;
import com.lawyer.service.dto.LawyerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lawyer} and its DTO {@link LawyerDTO}.
 */
@Mapper(componentModel = "spring")
public interface LawyerMapper extends EntityMapper<LawyerDTO, Lawyer> {}
