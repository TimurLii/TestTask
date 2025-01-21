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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    /***
     * Отправляются верные данные, приходит положительный ответ, сравнивается json ответ  с отправленным данными
     * @throws Exception
     */
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

    /***
     * Запрос на получение существующего объекта, приходит положительный ответ, сравнивается json ответ с отправленными данными
     * @throws Exception
     */
    @Test
    void getWalletResponseIsOk() throws Exception {

        UUID walletId = UUID.randomUUID();

        Wallet wallet = new Wallet(walletId, Wallet.OperationType.DEPOSIT, 1L);

        wallet = walletService.createWallet(wallet);


        mockMvc.perform(get("/api/v1/wallet/{id}", walletId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletUuid").value(wallet.getWalletUuid().toString()))
                .andExpect(jsonPath("$.operationType").value(wallet.getOperationType().toString()))
                .andExpect(jsonPath("$.amount").value(wallet.getAmount()));
    }

    /***
     * Отправляется неправильный json запрос, получаем 400 статус ошибки, json ответ подготовленный перехватчиком ошибок
     * @throws Exception
     */

    @Test
    void createWalletResponseBadRequest() throws Exception {

        String invalidJson = "{ " +
                "\"operationType\": \"DEPOSIT\" , " +
                "\"amount\": \"One dollars\"}";

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid JSON format."));
        ;

    }

    /***
     * Отправляем запрос на получение объекта по-несуществующему ID, получаем 400 статус ошибки и заготовленный ответ
     * @throws Exception
     */
    @Test
    void getWalletResponseBadRequest() throws Exception {
        UUID walletId = UUID.randomUUID();

        mockMvc.perform(get("/api/v1/wallet/{id}", walletId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Wallet not found."));
    }

    /***
     * Отправляем верный запрос на изменение данных, получаем положительный статус код и проверяем всё ли соответствует
     * @throws Exception
     */
    @Test
    void updateWalletDepositResponseIsOk() throws Exception {

        UUID walletId = UUID.randomUUID();

        Wallet wallet = new Wallet(walletId, Wallet.OperationType.DEPOSIT, 1000L);

        wallet = walletService.createWallet(wallet);

        Wallet newWallet = new Wallet(walletId, Wallet.OperationType.DEPOSIT, 123L);

        Long resultSum = wallet.getAmount() + newWallet.getAmount();

        mockMvc.perform(put("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newWallet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletUuid").value(walletId.toString()))
        .andExpect(jsonPath("$.operationType").value(wallet.getOperationType().toString()))
        .andExpect(jsonPath("$.amount").value(resultSum));

    }
    /***
     * Отправляем верный запрос на изменение данных, получаем положительный статус код и проверяем всё ли соответствует
     * @throws Exception
     */
    @Test
    void updateWalletWithdrawResponseIsOk() throws Exception {

        UUID walletId = UUID.randomUUID();

        Wallet wallet = new Wallet(walletId, Wallet.OperationType.WITHDRAW, 1000L);

        wallet = walletService.createWallet(wallet);

        Wallet newWallet = new Wallet(walletId, Wallet.OperationType.WITHDRAW, 123L);

        Long resultSum = wallet.getAmount() - newWallet.getAmount();

        mockMvc.perform(put("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newWallet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletUuid").value(walletId.toString()))
                .andExpect(jsonPath("$.operationType").value(wallet.getOperationType().toString()))
                .andExpect(jsonPath("$.amount").value(resultSum));

    }

    /***
     * Отправляем  неверный запрос на изменение данных, получает 400 статус ошибки и json ответ
     * @throws Exception
     */
    @Test
    void updateWalletWithdrawResponseIsBadRequest() throws Exception {

        UUID walletId = UUID.randomUUID();

        Wallet wallet = new Wallet(walletId, Wallet.OperationType.WITHDRAW, 1000L);

        wallet = walletService.createWallet(wallet);

        Wallet newWallet = new Wallet(walletId, Wallet.OperationType.WITHDRAW, 1100L);


        mockMvc.perform(put("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newWallet)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Wallet not found or underfunded"));

    }
}