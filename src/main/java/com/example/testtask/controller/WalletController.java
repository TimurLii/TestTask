package com.example.testtask.controller;

import com.example.testtask.entity.Wallet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.testtask.service.WalletService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping
    public ResponseEntity<Wallet>  createWallet( @RequestBody Wallet wallet){
        log.info("Creating wallet: {}", wallet);
        Wallet createdWallet = walletService.createWallet(wallet);
        return ResponseEntity.ok(createdWallet);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wallet> getWallet(@PathVariable UUID id) {
        log.info("Fetching wallet with ID: {}", id);
        Wallet wallet = walletService.getById(id);
        if (wallet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(wallet);
    }
    @PutMapping
    public ResponseEntity<Wallet> updateWallet(@RequestBody Wallet updateWallet) {
        log.info("Updating wallet: {}", updateWallet);
        return walletService.updateWallet(updateWallet);
    }


}
