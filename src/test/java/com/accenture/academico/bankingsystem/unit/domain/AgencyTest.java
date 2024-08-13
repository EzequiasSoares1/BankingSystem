package com.accenture.academico.bankingsystem.unit.domain;

import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.address.Address;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AgencyTest {

    @Test
    void testAgencyBuilder() {
        UUID id = UUID.randomUUID();
        String name = "Main Branch";
        String telephone = "123-456-7890";
        String number = "001";
        Address address = new Address();

        Agency agency = new Agency(id, name, telephone, number, address);

        assertEquals(id, agency.getId());
        assertEquals(name, agency.getName());
        assertEquals(telephone, agency.getTelephone());
        assertEquals(number, agency.getNumber());
        assertEquals(address, agency.getAddress());
    }

    @Test
    void testDefaultConstructor() {
        Agency agency = new Agency();

        assertNotNull(agency);
    }

    @Test
    void testSettersAndGetters() {
        Agency agency = new Agency();
        UUID id = UUID.randomUUID();
        String name = "Secondary Branch";
        String telephone = "987-654-3210";
        String number = "002";
        Address address = new Address();

        agency.setId(id);
        agency.setName(name);
        agency.setTelephone(telephone);
        agency.setNumber(number);
        agency.setAddress(address);

        assertEquals(id, agency.getId());
        assertEquals(name, agency.getName());
        assertEquals(telephone, agency.getTelephone());
        assertEquals(number, agency.getNumber());
        assertEquals(address, agency.getAddress());
    }

    @Test
    void testPrePersist() {
        Agency agency = new Agency();
        agency.prePersist();

        assertNotNull(agency.getCreatedDate());
        assertTrue(agency.getCreatedDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testPreUpdate() {
        Agency agency = new Agency();
        agency.prePersist();
        LocalDateTime initialUpdatedDate = agency.getUpdatedDate();

        agency.preUpdate();

        assertEquals(initialUpdatedDate, agency.getUpdatedDate());
        assertTrue(agency.getUpdatedDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
