package com.example.testtask.repository;

import com.example.testtask.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Wallet findByWalletUuid(UUID walletUuid);
}
