package com.example.testtask.entity;


import jakarta.persistence.*;
import lombok.Data;


import java.util.UUID;

@Entity
@Data
public class Wallet {
    @Id
    @Column(name = "wallet_uuid", nullable = false)
    private UUID walletUuid;

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Column(name = "amount")
    private Long amount;

    @PrePersist
    public void generateId() {
        if (this.walletUuid == null) {
            this.walletUuid = UUID.randomUUID();
        }
    }



    public enum OperationType {
        DEPOSIT("+"),
        WITHDRAW("-");

        private final String displayName;

        OperationType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}

