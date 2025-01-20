package com.example.testtask.service;

import com.example.testtask.entity.Wallet;
import com.example.testtask.repository.WalletRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
class WalletServiceRequestProcessingTest {
    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void updateWalletDepositTest() throws InterruptedException {
        UUID walletId = UUID.randomUUID();

        Wallet wallet = walletRepository.save(new Wallet(walletId, Wallet.OperationType.DEPOSIT, 1000L));

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    wallet.setAmount(1L);
                    walletService.updateWallet(wallet);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Wallet updatedWallet = walletRepository.findById(walletId).orElseThrow();
        Assertions.assertEquals(2000L, updatedWallet.getAmount());
    }
    @Test
    void updateWalletWithdrawTest() throws InterruptedException {
        UUID walletId = UUID.randomUUID();

        Wallet wallet = walletRepository.save(new Wallet(walletId, Wallet.OperationType.WITHDRAW, 1000L));

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    wallet.setAmount(1L);
                    walletService.updateWallet(wallet);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Wallet updatedWallet = walletRepository.findById(walletId).orElseThrow();
        Assertions.assertEquals(0, updatedWallet.getAmount());
    }


}