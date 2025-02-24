package com.scheduling.transfer_api.service;

import com.scheduling.transfer_api.exception.RateNotApplicableException;
import com.scheduling.transfer_api.model.Transfer;
import com.scheduling.transfer_api.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    @InjectMocks
    private TransferService transferService;

    @Mock
    private TransferRepository transferRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testScheduleTransfer_WithValidDate() {
        Transfer transfer = new Transfer();
        transfer.setTransferValue(100.0);
        transfer.setDateTransfer(LocalDate.now().plusDays(5));

        when(transferRepository.save(any(Transfer.class))).thenReturn(transfer);

        Transfer result = transferService.scheduleTransfer(transfer);

        assertNotNull(result);
        assertEquals(0.0, result.getRate());
        assertEquals(100.0, result.getTransferValue());
        assertNotNull(result.getDateCreate());
        assertNotNull(result.getNotice());
        assertEquals("Transferência agendada com taxa de 0.0%", result.getNotice());
    }

    @Test
    void testScheduleTransfer_WithRateNotApplicableException() {
        Transfer transfer = new Transfer();
        transfer.setTransferValue(100.0);
        transfer.setDateTransfer(LocalDate.now().plusDays(60));
        Exception exception = assertThrows(RateNotApplicableException.class, () -> {
            transferService.scheduleTransfer(transfer);
        });

        assertEquals("Não há taxa aplicável para a data fornecida. Transferência não permitida.", exception.getMessage());
    }

    @Test
    void testScheduleTransfer_WithDifferentRates() {
        when(transferRepository.save(any(Transfer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Teste com uma data que deve retornar 2.5%
        Transfer transfer1 = new Transfer();
        transfer1.setTransferValue(100.0);
        transfer1.setDateTransfer(LocalDate.now()); // Hoje
        Transfer result1 = transferService.scheduleTransfer(transfer1);
        assertEquals(2.5, result1.getRate(), 0.0001);  // Adicionando um delta

        // Teste com uma data que deve retornar 0.0%
        Transfer transfer2 = new Transfer();
        transfer2.setTransferValue(100.0);
        transfer2.setDateTransfer(LocalDate.now().plusDays(5)); // 5 dias no futuro
        Transfer result2 = transferService.scheduleTransfer(transfer2);
        assertEquals(0.0, result2.getRate(), 0.0001);

        // Teste com uma data que deve retornar 8.2%
        Transfer transfer3 = new Transfer();
        transfer3.setTransferValue(100.0);
        transfer3.setDateTransfer(LocalDate.now().plusDays(15)); // 15 dias no futuro
        Transfer result3 = transferService.scheduleTransfer(transfer3);
        assertEquals(8.2, result3.getRate(), 0.0001);

        // Teste com uma data que deve retornar 6.9%
        Transfer transfer4 = new Transfer();
        transfer4.setTransferValue(100.0);
        transfer4.setDateTransfer(LocalDate.now().plusDays(25)); // 25 dias no futuro
        Transfer result4 = transferService.scheduleTransfer(transfer4);
        assertEquals(6.9, result4.getRate(), 0.0001);

        // Teste com uma data que deve retornar 4.7%
        Transfer transfer5 = new Transfer();
        transfer5.setTransferValue(100.0);
        transfer5.setDateTransfer(LocalDate.now().plusDays(35)); // 35 dias no futuro
        Transfer result5 = transferService.scheduleTransfer(transfer5);
        assertEquals(4.7, result5.getRate(), 0.0001);

        // Teste com uma data que deve retornar 1.7%
        Transfer transfer6 = new Transfer();
        transfer6.setTransferValue(100.0);
        transfer6.setDateTransfer(LocalDate.now().plusDays(45)); // 45 dias no futuro
        Transfer result6 = transferService.scheduleTransfer(transfer6);
        assertEquals(1.7, result6.getRate(), 0.0001);

        // Teste com uma data que não deve ter taxa
        Transfer transfer7 = new Transfer();
        transfer7.setTransferValue(100.0);
        transfer7.setDateTransfer(LocalDate.now().plusDays(60)); // 60 dias no futuro
        Exception exception7 = assertThrows(RateNotApplicableException.class, () -> {
            transferService.scheduleTransfer(transfer7);
        });
        assertEquals("Não há taxa aplicável para a data fornecida. Transferência não permitida.", exception7.getMessage());
    }

    @Test
    void testListTransfers() {
        Pageable pageable = mock(Pageable.class);
        Page<Transfer> transferPage = mock(Page.class);
        when(transferRepository.findAllByOrderByDateCreateDesc(pageable)).thenReturn(transferPage);

        Page<Transfer> result = transferService.listTransfers(pageable);

        assertNotNull(result);
        verify(transferRepository, times(1)).findAllByOrderByDateCreateDesc(pageable);
    }

    @Test
    void testListTransfersByDate() {
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();
        Pageable pageable = mock(Pageable.class);
        Page<Transfer> transferPage = mock(Page.class);
        when(transferRepository.findByDataTransferBetween(startDate, endDate, pageable)).thenReturn(transferPage);

        Page<Transfer> result = transferService.listTransfersByDate(startDate, endDate, pageable);

        assertNotNull(result);
        verify(transferRepository, times(1)).findByDataTransferBetween(startDate, endDate, pageable);
    }
}