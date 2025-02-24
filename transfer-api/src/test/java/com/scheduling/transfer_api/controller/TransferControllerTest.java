package com.scheduling.transfer_api.controller;

import com.scheduling.transfer_api.exception.RateNotApplicableException;
import com.scheduling.transfer_api.model.Transfer;
import com.scheduling.transfer_api.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TransferControllerTest {

    @InjectMocks
    private TransferController transferController;

    @Mock
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testScheduleTransfer_Success() {
        Transfer transfer = new Transfer();
        transfer.setTransferValue(100.0);
        transfer.setDateTransfer(LocalDate.now().plusDays(5));
        transfer.setDateScheduling(LocalDate.now());

        when(transferService.scheduleTransfer(transfer)).thenReturn(transfer);

        ResponseEntity<?> response = transferController.scheduleTransfer(transfer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transfer, response.getBody());
    }

    @Test
    void testScheduleTransfer_RateNotApplicableException() {
        Transfer transfer = new Transfer();
        transfer.setTransferValue(100.0);
        transfer.setDateTransfer(LocalDate.now().plusDays(60));
        transfer.setDateScheduling(LocalDate.now());

        when(transferService.scheduleTransfer(transfer)).thenThrow(new RateNotApplicableException("Não há taxa aplicável."));

        ResponseEntity<?> response = transferController.scheduleTransfer(transfer);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Não há taxa aplicável.", response.getBody());
    }

    @Test
    void testScheduleTransfer_NullDateFields() {
        Transfer transfer = new Transfer();
        transfer.setTransferValue(100.0);
        transfer.setDateTransfer(null);
        transfer.setDateScheduling(LocalDate.now());
        ResponseEntity<?> response = transferController.scheduleTransfer(transfer);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Campos de data não podem ser nulos.", response.getBody());
    }

    @Test
    void testGetTransfer() {
        Page<Transfer> transferPage = mock(Page.class);
        when(transferService.listTransfers(PageRequest.of(0, 5))).thenReturn(transferPage);

        Page<Transfer> response = transferController.getTransfer(0, 5);

        assertNotNull(response);
        verify(transferService, times(1)).listTransfers(PageRequest.of(0, 5));
    }
}