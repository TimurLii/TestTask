package com.example.testtask.service;

import com.example.testtask.entity.Wallet;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.testtask.repository.WalletRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;


    public Wallet createWallet(Wallet wallet) {

        return walletRepository.save(wallet);
    }

    public Wallet getById(UUID id) {
        return walletRepository.findByWalletUuid(id);
    }

    @Transactional
    public ResponseEntity<?> updateWallet(Wallet updateWallet) {

        Wallet wallet = walletRepository.findByWalletUuidForUpdate(updateWallet.getWalletUuid());

        if (wallet == null) {
            return ResponseEntity.badRequest().body("Wallet not found");
        }

        updateWalletAmount(wallet, updateWallet);
        walletRepository.save(wallet);

        return ResponseEntity.ok(wallet);
    }


    private void updateWalletAmount(Wallet wallet, Wallet updateWallet) {
        switch (updateWallet.getOperationType()) {
            case DEPOSIT -> wallet.setAmount(wallet.getAmount() + updateWallet.getAmount());
            case WITHDRAW -> {
                if (!checkBalance(wallet, updateWallet)) {
                    throw new IllegalArgumentException("Wallet not found or underfunded");
                }
                wallet.setAmount(wallet.getAmount() - updateWallet.getAmount());
            }
            default -> throw new IllegalArgumentException("Unsupported operation type: " + updateWallet.getOperationType());
        }
    }


    private boolean checkBalance(Wallet wallet, Wallet updateWallet) {
        return wallet.getAmount() >= updateWallet.getAmount();
    }

}
