package com.accenture.academico.bankingsystem.mappers.agency;

import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.dtos.agency.AgencyResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AgencyMapper {
    public static AgencyResponseDTO convertToAgencyResponseDTO(Agency agency) {
        return new AgencyResponseDTO(
                agency.getId(),
                agency.getName(),
                agency.getTelephone(),
                agency.getNumber(),
                agency.getAddress().getId()
        );
    }
    public static List<AgencyResponseDTO> convertToAgencyResponseDTOList(List<Agency> agencies) {
        return agencies.stream()
                .map(AgencyMapper::convertToAgencyResponseDTO)
                .collect(Collectors.toList());
    }
}