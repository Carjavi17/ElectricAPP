package com.egg.electric_app.Repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.egg.electric_app.Entities.UserElectric;
@Repository
public interface UserRepository extends JpaRepository<UserElectric, UUID>{

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    public UserElectric findByEmail(@Param("email") String email);
    
}
