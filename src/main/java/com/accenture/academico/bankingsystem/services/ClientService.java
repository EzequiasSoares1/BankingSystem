package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.dtos.client.ClientRequestDTO;
import com.accenture.academico.bankingsystem.dtos.client.ClientResponseDTO;
import com.accenture.academico.bankingsystem.exceptions.ConflictException;
import com.accenture.academico.bankingsystem.exceptions.NotFoundException;
import com.accenture.academico.bankingsystem.mappers.client.ClientMapper;
import com.accenture.academico.bankingsystem.middlewares.UserTools;
import com.accenture.academico.bankingsystem.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final AddressService addressService;
    private final UserService userService;

    public List<ClientResponseDTO> getAllClients(){
        return ClientMapper.convertToClientResponseDTOList(this.clientRepository.findAll());
    }

    public ClientResponseDTO getClientById(UUID id){
        Client client = this.findById(id);
        UserTools.isAutorizate(client.getUser().getId());
        return ClientMapper.convertToClientResponseDTO(client);
    }

    public ClientResponseDTO createClient(ClientRequestDTO clientDTO){
        if(this.clientRepository.existsClientByCpf(clientDTO.cpf()))
            throw new ConflictException("Client already exists ");

        Client client = new Client();
        client.setName(clientDTO.name());
        client.setCpf(clientDTO.cpf());
        client.setTelephone(clientDTO.telephone());
        client.setAddress(this.addressService.getAddressById(clientDTO.address_id()));
        client.setUser(userService.getUserInternalById(UserTools.getUserContextId()));

        this.clientRepository.save(client);

        return ClientMapper.convertToClientResponseDTO(client);
    }

    public ClientResponseDTO updateClient(UUID id, ClientRequestDTO clientDTO){
        Client client = findById(id);

        UserTools.isAutorizate(client.getUser().getId());

        if(clientDTO.name() != null) client.setName(clientDTO.name());
        if(clientDTO.cpf() != null) client.setCpf(clientDTO.cpf());
        if(clientDTO.telephone() != null) client.setTelephone(clientDTO.telephone());
        if(clientDTO.address_id() != null) client.setAddress(this.addressService.getAddressById(clientDTO.address_id()));

        clientRepository.save(client);

        return ClientMapper.convertToClientResponseDTO(client);
    }

    public void deleteClient(UUID id){
        Client client = this.findById(id);
        UserTools.isAutorizate(client.getUser().getId());
        this.clientRepository.delete(client);
    }

    private Client findById(UUID id) {
        return this.clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with ID: " + id));
    }
    public Client findByUser(UUID id){
        return this.clientRepository.findByUserId(id)
                .orElseThrow(() -> new NotFoundException("Client not found with ID: " + id));
    }
}
