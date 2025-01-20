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
    @Enumerated(EnumType.STRING) // Теперь это корректно, так как тип - это enum
    private OperationType operationType; // Изменено на тип OperationType

    @Column(name = "amount")
    private Long amount;

    @PrePersist
    public void generateId() {
        if (this.walletUuid == null) {
            this.walletUuid = UUID.randomUUID();
        }
    }
    public enum OperationType {
        DEPOSIT,WITHDRAW;
    }
}

