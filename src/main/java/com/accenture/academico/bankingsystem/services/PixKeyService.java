package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.domain.pix_key.PixKey;
import com.accenture.academico.bankingsystem.dtos.pix_key.PixKeyRequestDTO;
import com.accenture.academico.bankingsystem.dtos.pix_key.PixKeyResponseDTO;
import com.accenture.academico.bankingsystem.exceptions.NotFoundException;
import com.accenture.academico.bankingsystem.mappers.pix_key.PixKeyMapper;
import com.accenture.academico.bankingsystem.repositories.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PixKeyService {
    private final PixKeyRepository pixKeyRepository;
    private final AccountService accountService;
    public List<PixKeyResponseDTO> getAllPixKeyByAccountId(UUID accountId){
        return PixKeyMapper.convertToPixKeyResponseDTOList(this.pixKeyRepository.findByAccountId(accountId));
    }
    public PixKeyResponseDTO getPixKeyById(UUID pixKeyId){
        return PixKeyMapper.convertToPixKeyResponseDTO(this.findById(pixKeyId));
    }
    public PixKeyResponseDTO createPixKey(UUID accountId, PixKeyRequestDTO pixKeyDTO){
        PixKey pixKey = new PixKey();

        pixKey.setKeyType(pixKeyDTO.keyType());
        pixKey.setKeyValue(pixKeyDTO.keyValue());
        pixKey.setAccount(this.accountService.getById(accountId));

        pixKeyRepository.save(pixKey);
        return PixKeyMapper.convertToPixKeyResponseDTO(pixKey);
    }
    public void deletePixKey(UUID pixKeyId){
        pixKeyRepository.delete(this.findById(pixKeyId));
    }
    private PixKey findById(UUID id){
        return this.pixKeyRepository.findById(id).orElseThrow(() -> new NotFoundException("PixKey not found with ID:" + id));
    }
}
