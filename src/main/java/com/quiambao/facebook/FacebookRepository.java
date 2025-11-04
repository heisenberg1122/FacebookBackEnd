package com.quiambao.facebook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// JpaRepository<Entity Class, Data Type of the ID>
public interface FacebookRepository extends JpaRepository<FacebookPost, Long> {
    // Basic CRUD operations (save, findAll, findById, delete) are inherited automatically.
}