package com.accenture.academico.bankingsystem.unit.mappers;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.PixKeyType;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.pix_key.PixKey;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.pix_key.PixKeyResponseDTO;
import com.accenture.academico.bankingsystem.mappers.pix_key.PixKeyMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PixKeyMapperTest {
    @Test
    void testConvertToPixKeyResponseDTO(){
        User user = new User(UUID.randomUUID(), "test@example.com", Role.ADMIN, "password");
        Address address = new Address(UUID.randomUUID(), "58700010", "888", "Rua Teste", "Centro", LocalDateTime.now(), LocalDateTime.now());
        Agency agency = new Agency(UUID.randomUUID(), "Teste", "83988776655", "888", address, LocalDateTime.now(), LocalDateTime.now());
        Client client = new Client(UUID.randomUUID(), "Teste", "11111", "8399888", address, user, LocalDateTime.now(), LocalDateTime.now());
        Account account = new Account(UUID.randomUUID(), "1111", AccountType.CURRENT, agency, BigDecimal.ZERO, client, LocalDateTime.now(), LocalDateTime.now());

        PixKey pixKey = new PixKey(UUID.randomUUID(), PixKeyType.EMAIL, "teste@gmail.com", account, LocalDateTime.now());

        PixKeyResponseDTO pixKeyResponseDTO = PixKeyMapper.convertToPixKeyResponseDTO(pixKey);

        assertNotNull(pixKeyResponseDTO);
        assertEquals(pixKeyResponseDTO.id(), pixKey.getId());
        assertEquals(pixKeyResponseDTO.keyType(), pixKey.getKeyType());
        assertEquals(pixKeyResponseDTO.keyValue(), pixKey.getKeyValue());
        assertEquals(pixKeyResponseDTO.accountId(), pixKey.getAccount().getId());
        assertEquals(pixKeyResponseDTO.createdDate(), pixKey.getCreatedDate());
    }

    @Test
    void testConvertToPixKeyResponseDTOList(){
        User user = new User(UUID.randomUUID(), "test@example.com", Role.ADMIN, "password");
        Address address = new Address(UUID.randomUUID(), "58700010", "888", "Rua Teste", "Centro", LocalDateTime.now(), LocalDateTime.now());
        Agency agency = new Agency(UUID.randomUUID(), "Teste", "83988776655", "888", address, LocalDateTime.now(), LocalDateTime.now());
        Client client = new Client(UUID.randomUUID(), "Teste", "11111", "8399888", address, user, LocalDateTime.now(), LocalDateTime.now());
        Account account = new Account(UUID.randomUUID(), "1111", AccountType.CURRENT, agency, BigDecimal.ZERO, client, LocalDateTime.now(), LocalDateTime.now());

        PixKey pixKey1 = new PixKey(UUID.randomUUID(), PixKeyType.EMAIL, "teste@gmail.com", account, LocalDateTime.now());
        PixKey pixKey2 = new PixKey(UUID.randomUUID(), PixKeyType.CPF, "111111", account, LocalDateTime.now());

        List<PixKey> pixKeyList = List.of(pixKey1, pixKey2);
        List<PixKeyResponseDTO> pixKeyResponseDTOList = PixKeyMapper.convertToPixKeyResponseDTOList(pixKeyList);

        assertNotEquals(pixKeyResponseDTOList.size(), 0);
        assertEquals(pixKeyResponseDTOList.get(0).id(), pixKeyList.get(0).getId());
        assertEquals(pixKeyResponseDTOList.get(1).id(), pixKeyList.get(1).getId());
        assertEquals(pixKeyResponseDTOList.get(0).keyType(), pixKeyList.get(0).getKeyType());
        assertEquals(pixKeyResponseDTOList.get(1).keyType(), pixKeyList.get(1).getKeyType());
        assertEquals(pixKeyResponseDTOList.get(0).keyValue(), pixKeyList.get(0).getKeyValue());
        assertEquals(pixKeyResponseDTOList.get(1).keyValue(), pixKeyList.get(1).getKeyValue());
        assertEquals(pixKeyResponseDTOList.get(0).accountId(), pixKeyList.get(0).getAccount().getId());
        assertEquals(pixKeyResponseDTOList.get(1).accountId(), pixKeyList.get(1).getAccount().getId());
        assertEquals(pixKeyResponseDTOList.get(0).createdDate(), pixKeyList.get(0).getCreatedDate());
        assertEquals(pixKeyResponseDTOList.get(1).createdDate(), pixKeyList.get(1).getCreatedDate());
    }
}
