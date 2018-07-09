package com.ChatBulat.BulatChatDemo.repository;

import com.ChatBulat.BulatChatDemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
