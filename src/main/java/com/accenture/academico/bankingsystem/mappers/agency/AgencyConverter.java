package com.accenture.academico.bankingsystem.mappers.agency;

import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.dtos.agency.AgencyDTO;
import com.accenture.academico.bankingsystem.dtos.agency.AgencyRequestDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AgencyConverter {

    public static Agency convertToAgency(AgencyRequestDTO agencyDTO) {
        Agency agency = new Agency();
        agency.setName(agencyDTO.name());
        agency.setTelephone(agencyDTO.telephone());
        agency.setNumber(agencyDTO.number());
        agency.setAddress(agency.getAddress());
        return agency;
    }

    public static AgencyDTO convertToAgencyResponseDTO(Agency agency) {
        return new AgencyDTO(
                agency.getId(),
                agency.getName(),
                agency.getTelephone(),
                agency.getNumber(),
                agency.getAddress().getId()
        );
    }

    public static List<AgencyDTO> convertToAgencyResponseDTOList(List<Agency> agencies) {
        return agencies.stream()
                .map(AgencyConverter::convertToAgencyResponseDTO)
                .collect(Collectors.toList());
    }

}
