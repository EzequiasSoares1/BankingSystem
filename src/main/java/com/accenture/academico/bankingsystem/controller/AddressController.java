package com.accenture.academico.bankingsystem.controller;

import com.accenture.academico.bankingsystem.dto.address.AddressRequestDTO;
import com.accenture.academico.bankingsystem.dto.address.AddressResponseDTO;
import com.accenture.academico.bankingsystem.services.general.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAllAddress(){
        return ResponseEntity.ok(this.addressService.getAllAddress());
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> create(@RequestBody @Valid AddressRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        AddressResponseDTO addressResponseDTO = this.addressService.create(body);

        var uri = uriComponentsBuilder.path("/address/{id}").buildAndExpand(addressResponseDTO.id()).toUri();

        return ResponseEntity.created(uri).body(addressResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid AddressRequestDTO body){
        return ResponseEntity.ok(this.addressService.update(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id){
        this.addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
