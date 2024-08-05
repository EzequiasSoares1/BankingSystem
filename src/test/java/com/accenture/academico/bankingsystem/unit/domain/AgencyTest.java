package com.accenture.academico.bankingsystem.unit.domain;

import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.address.Address;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
