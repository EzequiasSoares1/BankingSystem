package com.accenture.academico.bankingsystem.mappers.client;

import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.dtos.client.ClientRequestDTO;
import com.accenture.academico.bankingsystem.dtos.client.ClientResponseDTO;
import java.util.List;
import java.util.stream.Collectors;

public class ClientMapper {
    public static ClientResponseDTO convertToClientResponseDTO(Client client){
        return new ClientResponseDTO(
                client.getId(),
                client.getName(),
                client.getCpf(),
                client.getTelephone(),
                client.getAddress().getId(),
                client.getUser().getId()
        );
    }
    public static List<ClientResponseDTO> convertToClientResponseDTOList(List<Client> clients) {
        return clients.stream()
                .map(ClientMapper::convertToClientResponseDTO)
                .collect(Collectors.toList());
    }
}