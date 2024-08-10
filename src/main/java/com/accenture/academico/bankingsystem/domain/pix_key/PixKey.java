package com.accenture.academico.bankingsystem.domain.pix_key;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.enums.PixKeyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pix_key")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PixKey {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(name = "key_type", nullable = false)
    private PixKeyType keyType;
    @Column(name = "key_value", nullable = false)
    private String keyValue;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
    }
}
