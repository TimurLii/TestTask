package com.example.testtask.controller;

import com.example.testtask.entity.Wallet;
import com.example.testtask.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/wallet")
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
    public ResponseEntity<?> getWallet(@PathVariable UUID id) {
        log.info("Fetching wallet with ID: {}", id);
        Wallet wallet = walletService.getById(id);
        if (wallet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Wallet not found.");
        }
        return ResponseEntity.ok(wallet);
    }
    @PutMapping
    public ResponseEntity<?> updateWallet(@RequestBody Wallet updateWallet) {
        log.info("Updating wallet: {}", updateWallet);
        ResponseEntity<?> responseEntity = walletService.updateWallet(updateWallet);
        if(responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Wallet not found or underfunded");
        }
        return responseEntity;
    }


}
