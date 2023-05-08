package com.pvsrg.covidapi.web.cases.mapper;

import com.pvsrg.covidapi.model.entities.MaxMinCasesVO;
import com.pvsrg.covidapi.web.cases.dto.MaxMinCasesResponseDTO;
import org.mapstruct.Mapper;

@Mapper
public abstract class CasesMapper {
    public abstract MaxMinCasesResponseDTO voToDTO(MaxMinCasesVO vo);
}
