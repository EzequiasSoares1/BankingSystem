package com.accenture.academico.bankingsystem.mappers.pix_key;

import com.accenture.academico.bankingsystem.domain.pix_key.PixKey;
import com.accenture.academico.bankingsystem.dtos.pix_key.PixKeyResponseDTO;
import java.util.List;

public class PixKeyMapper {
    public static PixKeyResponseDTO convertToPixKeyResponseDTO(PixKey pixKey) {
        return new PixKeyResponseDTO(
                pixKey.getId(),
                pixKey.getKeyType(),
                pixKey.getKeyValue(),
                pixKey.getAccount().getId(),
                pixKey.getCreatedDate()
        );
    }

    public static List<PixKeyResponseDTO> convertToPixKeyResponseDTOList(List<PixKey> pixKeyList) {
        return pixKeyList.stream()
                .map(PixKeyMapper::convertToPixKeyResponseDTO)
                .toList();
    }
}
