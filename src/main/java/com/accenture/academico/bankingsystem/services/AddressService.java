package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.dtos.address.AddressRequestDTO;
import com.accenture.academico.bankingsystem.dtos.address.AddressResponseDTO;
import com.accenture.academico.bankingsystem.exceptions.NotFoundException;
import com.accenture.academico.bankingsystem.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<AddressResponseDTO> getAllAddress() {
        List<Address> addressList = this.addressRepository.findAll();

        return addressList.stream().map(address -> {
            return new AddressResponseDTO(
                    address.getId(),
                    address.getCep(),
                    address.getNumber(),
                    address.getStreet(),
                    address.getDistrict()
            );
        }).toList();
    }

    public AddressResponseDTO getAddressById(UUID id){
        Address address = this.findById(id);

        return new AddressResponseDTO(
                address.getId(),
                address.getCep(),
                address.getNumber(),
                address.getStreet(),
                address.getDistrict()
        );
    }
    public AddressResponseDTO create(AddressRequestDTO addressDTO){
        Address address = new Address(addressDTO);

        this.addressRepository.save(address);
        return new AddressResponseDTO(
                address.getId(),
                address.getCep(),
                address.getNumber(),
                address.getStreet(),
                address.getDistrict()
        );
    }

    public AddressResponseDTO update(UUID id, AddressRequestDTO addressDTO){
        Address address = this.findById(id);

        address.setCep(addressDTO.cep());
        address.setNumber(addressDTO.number());
        address.setStreet(addressDTO.street());
        address.setDistrict(addressDTO.district());

        this.addressRepository.save(address);

        return new AddressResponseDTO(
                address.getId(),
                address.getCep(),
                address.getNumber(),
                address.getStreet(),
                address.getDistrict()
        );
    }

    public void delete(UUID addressId){
        this.addressRepository.delete(this.findById(addressId));
    }

    private Address findById(UUID id){
        return this.addressRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Address not found with ID:" + id));
    }
}
