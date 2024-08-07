package com.accenture.academico.bankingsystem.controllers;

import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.dtos.client.ClientRequestDTO;
import com.accenture.academico.bankingsystem.dtos.client.ClientResponseDTO;
import com.accenture.academico.bankingsystem.services.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Slf4j
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getAllClients(){
        log.info("Fetching all clients");
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable UUID id){
        log.info("Fetching address with id: {}", id);
        return ResponseEntity.ok(this.clientService.getClientById(id));
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody @Valid ClientRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        log.info("Creating client with DTO: {}", body.toString());
        ClientResponseDTO clientDTO = this.clientService.createClient(body);

        var uri = uriComponentsBuilder.path("/client/{id}").buildAndExpand(clientDTO.id()).toUri();

        return ResponseEntity.created(uri).body(clientDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable UUID id, @RequestBody @Valid ClientRequestDTO body){
        log.info("Updating client with id {} and DTO: {}", id, body);
        return ResponseEntity.ok(this.clientService.updateClient(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(@PathVariable UUID id){
        log.info("Deleting client with id: {}", id);
        this.clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }


}
