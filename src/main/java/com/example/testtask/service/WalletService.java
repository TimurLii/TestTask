package com.example.testtask.service;

import com.example.testtask.entity.Wallet;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.testtask.repository.WalletRepository;

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
    public ResponseEntity<Wallet> updateWallet(Wallet updateWallet) {

        Wallet wallet = walletRepository.findByWalletUuidForUpdate(updateWallet.getWalletUuid());

        if (wallet == null) {
            return ResponseEntity.badRequest().build();
        }

        wallet.setWalletUuid(updateWallet.getWalletUuid());
        wallet.setOperationType(updateWallet.getOperationType());

        if (depositOperationType(updateWallet) ) {
            wallet.setAmount(updateWallet.getAmount() + wallet.getAmount());
        }
        else {
            if(!checkBalance(wallet, updateWallet)){
                return ResponseEntity.badRequest().build();
            }
            wallet.setAmount(wallet.getAmount() - updateWallet.getAmount());
        }
        walletRepository.save(wallet);
        return ResponseEntity.ok(wallet);
    }

    private boolean depositOperationType(Wallet updateWallet) {
        return updateWallet.getOperationType().equals(Wallet.OperationType.DEPOSIT);
    }

    private boolean checkBalance(Wallet wallet, Wallet updateWallet) {
        return wallet.getAmount() >= updateWallet.getAmount();
    }

}
