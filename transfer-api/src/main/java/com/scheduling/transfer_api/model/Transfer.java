package com.scheduling.transfer_api.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountOrigin;
    private String accountDestination;
    private Double transferValue;
    private Double rate;
    private String notice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateTransfer;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateScheduling;

    @Column(name = "data_criacao")
    private LocalDateTime dateCreate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountOrigin() {
        return accountOrigin;
    }

    public void setAccountOrigin(String accountOrigin) {
        this.accountOrigin = accountOrigin;
    }

    public String getAccountDestination() {
        return accountDestination;
    }

    public void setAccountDestination(String accountDestination) {
        this.accountDestination = accountDestination;
    }

    public Double getTransferValue() {
        return transferValue;
    }

    public void setTransferValue(Double transferValue) {
        this.transferValue = transferValue;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public LocalDate getDateTransfer() {
        return dateTransfer;
    }

    public void setDateTransfer(LocalDate dateTransfer) {
        this.dateTransfer = dateTransfer;
    }

    public LocalDate getDateScheduling() {
        return dateScheduling;
    }

    public void setDateScheduling(LocalDate dateScheduling) {
        this.dateScheduling = dateScheduling;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", accountOrigin='" + accountOrigin + '\'' +
                ", accountDestination='" + accountDestination + '\'' +
                ", transferValue=" + transferValue +
                ", rate=" + rate +
                ", notice='" + notice + '\'' +
                ", dateTransfer=" + dateTransfer +
                ", dateScheduling=" + dateScheduling +
                ", dateCreate=" + dateCreate +
                '}';
    }
}
