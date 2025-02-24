package com.scheduling.transfer_api.controller;

import com.scheduling.transfer_api.exception.RateNotApplicableException;
import com.scheduling.transfer_api.model.Transfer;
import com.scheduling.transfer_api.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
@CrossOrigin(origins = "http://localhost:4200")
public class TransferController {

    @Autowired
    private TransferService service;

    @PostMapping
    public ResponseEntity<?> scheduleTransfer(@RequestBody Transfer transfer) {
        try {
            System.out.println("Data de Transferência: " + transfer.getDateTransfer());
            System.out.println("Data de Agendamento: " + transfer.getDateScheduling());

            if (transfer.getDateTransfer() == null || transfer.getDateScheduling() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campos de data não podem ser nulos.");
            }

            Transfer scheduled = service.scheduleTransfer(transfer);
            return ResponseEntity.ok(scheduled);
        } catch (RateNotApplicableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro interno. Por favor, tente novamente.");
        }
    }

    @ExceptionHandler(RateNotApplicableException.class)
    public ResponseEntity<String> handleRateNotApplicableException(RateNotApplicableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @GetMapping
    public Page<Transfer> getTransfer(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return service.listTransfers(PageRequest.of(page, size));
    }

}