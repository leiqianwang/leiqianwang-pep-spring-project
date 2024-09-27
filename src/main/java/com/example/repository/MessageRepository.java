package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

   // List<Message> findAll();
   //BY extending JpaRepository, Spring Data JPA automatically provides the implementation of findAll(), save(), findById()
   //and other CRUD methods
}
