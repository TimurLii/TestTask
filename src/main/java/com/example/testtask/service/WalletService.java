package com.example.testtask.service;

import com.example.testtask.entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;

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
}
