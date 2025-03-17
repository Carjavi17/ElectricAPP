package com.egg.electric_app.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.egg.electric_app.Entities.ImageUser;

@Repository
public interface ImageUserRepository extends JpaRepository<ImageUser, UUID> {

    
} 

