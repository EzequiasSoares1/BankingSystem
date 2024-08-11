package com.accenture.academico.bankingsystem.unit.mappers;

import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.dtos.agency.AgencyResponseDTO;
import com.accenture.academico.bankingsystem.mappers.agency.AgencyMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AgencyMapperTest {
    @Test
    void testConvertToAgencyResponseDTO(){
        Address address = new Address(UUID.randomUUID(), "58700010", "888", "Rua Teste", "Centro", LocalDateTime.now(), LocalDateTime.now());
        Agency agency = new Agency(UUID.randomUUID(), "Teste", "83988776655", "888", address, LocalDateTime.now(), LocalDateTime.now());

        AgencyResponseDTO agencyResponseDTO = AgencyMapper.convertToAgencyResponseDTO(agency);

        assertNotNull(agencyResponseDTO);
        assertEquals(agencyResponseDTO.id(), agency.getId());
        assertEquals(agencyResponseDTO.name(), agency.getName());
        assertEquals(agencyResponseDTO.telephone(), agency.getTelephone());
        assertEquals(agencyResponseDTO.number(), agency.getNumber());
        assertEquals(agencyResponseDTO.address_id(), agency.getAddress().getId());
    }

    @Test
    void testConvertToAgencyResponseDTOList(){
        Address address = new Address(UUID.randomUUID(), "58700010", "888", "Rua Teste", "Centro", LocalDateTime.now(), LocalDateTime.now());
        Agency agency1 = new Agency(UUID.randomUUID(), "Teste1", "83988776655", "888", address, LocalDateTime.now(), LocalDateTime.now());
        Agency agency2 = new Agency(UUID.randomUUID(), "Teste2", "83988776656", "888", address, LocalDateTime.now(), LocalDateTime.now());

        List<Agency> agencyList = List.of(agency1, agency2);

        List<AgencyResponseDTO> agencyResponseDTOList = AgencyMapper.convertToAgencyResponseDTOList(agencyList);

        assertNotEquals(agencyResponseDTOList.size(), 0);
        assertEquals(agencyResponseDTOList.get(0).id(), agencyList.get(0).getId());
        assertEquals(agencyResponseDTOList.get(1).id(), agencyList.get(1).getId());
        assertEquals(agencyResponseDTOList.get(0).name(), agencyList.get(0).getName());
        assertEquals(agencyResponseDTOList.get(1).name(), agencyList.get(1).getName());
        assertEquals(agencyResponseDTOList.get(0).telephone(), agencyList.get(0).getTelephone());
        assertEquals(agencyResponseDTOList.get(1).telephone(), agencyList.get(1).getTelephone());
        assertEquals(agencyResponseDTOList.get(0).number(), agencyList.get(0).getNumber());
        assertEquals(agencyResponseDTOList.get(1).number(), agencyList.get(1).getNumber());
        assertEquals(agencyResponseDTOList.get(0).address_id(), agencyList.get(0).getAddress().getId());
        assertEquals(agencyResponseDTOList.get(1).address_id(), agencyList.get(1).getAddress().getId());
    }
}
