package com.ChatBulat.BulatChatDemo.repository;

import com.ChatBulat.BulatChatDemo.model.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface DialogRepository extends JpaRepository<Dialog, Long> {
}
