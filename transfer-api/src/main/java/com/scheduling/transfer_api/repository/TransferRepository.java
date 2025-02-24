package com.scheduling.transfer_api.repository;

import com.scheduling.transfer_api.model.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    Page<Transfer> findAllByOrderByDateCreateDesc(Pageable pageable);

    @Query("SELECT t FROM Transfer t WHERE t.dateTransfer BETWEEN :startDate AND :endDate")
    Page<Transfer> findByDataTransferBetween(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate,
                                             Pageable pageable);
}