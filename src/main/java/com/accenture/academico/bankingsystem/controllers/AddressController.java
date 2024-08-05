package com.accenture.academico.bankingsystem.controllers;

import com.accenture.academico.bankingsystem.dtos.address.AddressRequestDTO;
import com.accenture.academico.bankingsystem.dtos.address.AddressResponseDTO;
import com.accenture.academico.bankingsystem.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@Slf4j
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAllAddress(){
        log.info("Fetching all address");
        return ResponseEntity.ok(this.addressService.getAllAddress());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> getAddressById(@PathVariable UUID id){
        log.info("Fetching address with id: {}", id);
        return ResponseEntity.ok(this.addressService.getAddressById(id));
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> create(@RequestBody @Valid AddressRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        log.info("Creating address with DTO: {}", body.toString());
        AddressResponseDTO addressResponseDTO = this.addressService.create(body);

        var uri = uriComponentsBuilder.path("/address/{id}").buildAndExpand(addressResponseDTO.id()).toUri();

        return ResponseEntity.created(uri).body(addressResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid AddressRequestDTO body){
        log.info("Updating address with id {} and DTO: {}", id, body);
        return ResponseEntity.ok(this.addressService.update(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id){
        log.info("Deleting address with id: {}", id);
        this.addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
