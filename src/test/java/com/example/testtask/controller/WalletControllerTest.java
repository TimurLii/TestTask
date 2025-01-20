package com.example.testtask.controller;

import com.example.testtask.entity.Wallet;
import com.example.testtask.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WalletService walletService;

    @Test
    void createWalletResponseIsOk() throws Exception {
        UUID walletId = UUID.randomUUID();

        Wallet wallet = new Wallet(walletId, Wallet.OperationType.DEPOSIT, 123321L);

        mockMvc.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wallet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(123321L))
                .andExpect(jsonPath("$.operationType").value(Wallet.OperationType.DEPOSIT.toString()))
                .andExpect(jsonPath("$.walletUuid").value(walletId.toString()));
    }

    @Test
    void getWalletResponseIsOk() throws Exception {

        UUID walletId = UUID.randomUUID();

        Wallet wallet = new Wallet(walletId, Wallet.OperationType.DEPOSIT, 1L);

        wallet = walletService.createWallet(wallet);


        mockMvc.perform(get("/api/v1/wallet/{id}", walletId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletUuid").value(walletId.toString()));
    }

    @Test
    void createWalletResponseBadRequest() throws Exception {
        UUID walletId = UUID.randomUUID();

        Wallet wallet = new Wallet(walletId, Wallet.OperationType.DEPOSIT, 123321L);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wallet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(123321L))
                .andExpect(jsonPath("$.operationType").value(Wallet.OperationType.DEPOSIT.toString()))
                .andExpect(jsonPath("$.walletUuid").value(walletId.toString()));
    }

    @Test
    void getWalletResponseBadRequest() throws Exception {
        UUID walletId = UUID.randomUUID();

        mockMvc.perform(get("/api/v1/wallet/{id}", walletId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Wallet not found."));
    }

}