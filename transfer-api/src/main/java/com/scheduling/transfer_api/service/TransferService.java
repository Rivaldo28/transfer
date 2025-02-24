package com.scheduling.transfer_api.service;

import com.scheduling.transfer_api.exception.RateNotApplicableException;
import com.scheduling.transfer_api.model.Transfer;
import com.scheduling.transfer_api.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TransferService {

    @Autowired
    private TransferRepository repository;

    public Page<Transfer> listTransfers(Pageable pageable) {
        return repository.findAllByOrderByDateCreateDesc(pageable);
    }

    public Page<Transfer> listTransfersByDate(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return repository.findByDataTransferBetween(startDate, endDate, pageable);
    }

    public Transfer scheduleTransfer(Transfer transfer) {
        double ratePercentage = calculatePercentageRate(transfer.getDateTransfer());

        if (ratePercentage < 0) {
            throw new RateNotApplicableException("Não há taxa aplicável para a data fornecida. Transferência não permitida.");
        }

        double taxa = (ratePercentage / 100) * transfer.getTransferValue();
        transfer.setRate(taxa);
        transfer.setDateCreate(LocalDateTime.now());
        transfer.setNotice("Transferência agendada com taxa de " + ratePercentage + "%");

        transfer.setDateScheduling(LocalDate.now());
        return repository.save(transfer);
    }

    private double calculatePercentageRate(LocalDate dateTransfer) {
        LocalDate today = LocalDate.now();
        long days = java.time.temporal.ChronoUnit.DAYS.between(today, dateTransfer);

        if (days == 0) return 2.5;
        if (days >= 1 && days <= 10) return 0.0;
        if (days >= 11 && days <= 20) return 8.2;
        if (days >= 21 && days <= 30) return 6.9;
        if (days >= 31 && days <= 40) return 4.7;
        if (days >= 41 && days <= 50) return 1.7;
        return -1;
    }
}