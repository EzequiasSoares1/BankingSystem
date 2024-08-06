package com.accenture.academico.bankingsystem.unit.domain;

import com.accenture.academico.bankingsystem.domain.address.Address;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AddressTest {

    @Test
    void testAddressBuilder() {
        UUID id = UUID.randomUUID();
        String cep = "12345678";
        String number = "123";
        String street = "Main St";
        String district = "Downtown";

        Address address = new Address(id, cep, number, street, district);

        assertEquals(id, address.getId());
        assertEquals(cep, address.getCep());
        assertEquals(number, address.getNumber());
        assertEquals(street, address.getStreet());
        assertEquals(district, address.getDistrict());
    }

    @Test
    void testDefaultConstructor() {
        Address address = new Address();

        assertNotNull(address);
    }

    @Test
    void testSettersAndGetters() {
        Address address = new Address();
        UUID id = UUID.randomUUID();
        String cep = "87654321";
        String number = "456";
        String street = "Second St";
        String district = "Uptown";

        address.setId(id);
        address.setCep(cep);
        address.setNumber(number);
        address.setStreet(street);
        address.setDistrict(district);

        assertEquals(id, address.getId());
        assertEquals(cep, address.getCep());
        assertEquals(number, address.getNumber());
        assertEquals(street, address.getStreet());
        assertEquals(district, address.getDistrict());
    }

}
