package com.ServerApi.MessengerServerAPI.repository;

import com.ServerApi.MessengerServerAPI.model.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface DialogRepository extends JpaRepository<Dialog, Long> {
}
