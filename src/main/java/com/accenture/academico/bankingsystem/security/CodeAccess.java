package com.accenture.academico.bankingsystem.security;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CodeAccess {
    private static final UUID CONSTANT_UUID = UUID.randomUUID();

    public static UUID genCode(UUID uuid) {
        return new UUID(uuid.getMostSignificantBits() + CONSTANT_UUID.getMostSignificantBits(),
                uuid.getLeastSignificantBits() + CONSTANT_UUID.getLeastSignificantBits());
    }

    public static UUID reverseCode(UUID result) {
        return new UUID(result.getMostSignificantBits() - CONSTANT_UUID.getMostSignificantBits(),
                result.getLeastSignificantBits() - CONSTANT_UUID.getLeastSignificantBits());
    }
}