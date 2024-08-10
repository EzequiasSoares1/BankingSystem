package com.accenture.academico.bankingsystem.controllers;

import com.accenture.academico.bankingsystem.dtos.pix_key.PixKeyRequestDTO;
import com.accenture.academico.bankingsystem.dtos.pix_key.PixKeyResponseDTO;
import com.accenture.academico.bankingsystem.services.PixKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pix-key")
@RequiredArgsConstructor
@Slf4j
public class PixKeyController {
    private final PixKeyService pixKeyService;

    @GetMapping("/account/{id}")
    public ResponseEntity<List<PixKeyResponseDTO>> getAllPixKeyByAccountId(@PathVariable UUID id){
        log.info("Fetching all pix keys by account with id: {}", id);
        return ResponseEntity.ok(this.pixKeyService.getAllPixKeyByAccountId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PixKeyResponseDTO> getPixKeyById(@PathVariable UUID id){
        log.info("Fetching pix key with id: {}", id);
        return ResponseEntity.ok(this.pixKeyService.getPixKeyById(id));
    }

    @PostMapping("/account/{id}")
    public ResponseEntity<PixKeyResponseDTO> createPixKey(@PathVariable UUID id, @RequestBody @Valid PixKeyRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        log.info("Creating pix key with DTO: {}", body.toString());
        PixKeyResponseDTO response = this.pixKeyService.createPixKey(id, body);

        var uri = uriComponentsBuilder.path("/pix-key/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePixKey(@PathVariable UUID id){
        log.info("Deleting pix key with id: {}", id);
        this.pixKeyService.deletePixKey(id);
        return ResponseEntity.noContent().build();
    }
}
