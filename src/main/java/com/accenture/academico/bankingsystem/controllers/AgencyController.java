package com.accenture.academico.bankingsystem.controllers;

import com.accenture.academico.bankingsystem.dtos.agency.AgencyRequestDTO;
import com.accenture.academico.bankingsystem.dtos.agency.AgencyDTO;
import com.accenture.academico.bankingsystem.services.AgencyService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/agency")
@Slf4j
public class AgencyController {

    @Autowired
    private AgencyService agencyService;

    @GetMapping
    public ResponseEntity<List<AgencyDTO>> getAllAgencies() {
        log.info("Fetching all agencies");
        return ResponseEntity.ok().body(agencyService.getAllAgencies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgencyDTO> getAgencyById(@PathVariable UUID id) {
        log.info("Fetching agency with id: {}", id);
        return ResponseEntity.ok().body(agencyService.getAgencyById(id));
    }

    @PostMapping
    public ResponseEntity<AgencyDTO> createAgency(@RequestBody @Valid AgencyRequestDTO agencyDTO) {
        log.info("Creating agency with DTO: {}", agencyDTO.toString());
        return ResponseEntity.ok().body(agencyService.createAgency(agencyDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgencyDTO> updateAgency(@PathVariable UUID id, @RequestBody @Valid AgencyDTO agencyDTO) {
        log.info("Updating agency with id {} and DTO: {}", id, agencyDTO);
        return ResponseEntity.ok().body(agencyService.updateAgency(id, agencyDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgency(@PathVariable UUID id) {
        log.info("Deleting agency with id: {}", id);
        agencyService.deleteAgency(id);
        return ResponseEntity.noContent().build();
    }
}
