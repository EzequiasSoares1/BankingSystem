package com.accenture.academico.bankingsystem.controllers;
import com.accenture.academico.bankingsystem.domain.address.Address;
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
    public ResponseEntity<List<Address>> getAllAddress(){
        log.info("Fetching all address");
        return ResponseEntity.ok(this.addressService.getAllAddress());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable UUID id){
        log.info("Fetching address with id: {}", id);
        return ResponseEntity.ok(this.addressService.getAddressById(id));
    }

    @PostMapping
    public ResponseEntity<Address> create(@RequestBody @Valid Address body, UriComponentsBuilder uriComponentsBuilder){
        log.info("Creating address with DTO: {}", body.toString());
        Address address = this.addressService.create(body);

        var uri = uriComponentsBuilder.path("/address/{id}").buildAndExpand(address.getId()).toUri();

        return ResponseEntity.created(uri).body(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> update(@PathVariable UUID id, @RequestBody @Valid Address body){
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
