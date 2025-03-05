package com.wipro.controller;

import com.wipro.dto.TransactionDto;
import com.wipro.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "*")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

//    @PostMapping("/createTransaction/{appointmentId}")
//    public ResponseEntity<Map<String,Object>> createTransaction(@PathVariable Long appointmentId) {
//       return transactionService.createTransaction(appointmentId);
//    }

    @PostMapping("/createTransaction/{appointmentId}")
    public ResponseEntity<TransactionDto> createTransaction(@PathVariable Long appointmentId) {
        return transactionService.createTransaction(appointmentId);
    }

    @PostMapping("/closeTransaction/{transactionId}")
    public ResponseEntity<Void> closeTransaction(@PathVariable Long transactionId) {
        return transactionService.closeTransaction(transactionId);
    }

    @GetMapping("/getTransaction")
    public List<TransactionDto> getTransactions(){
        return transactionService.getTransactions();
    }

}
