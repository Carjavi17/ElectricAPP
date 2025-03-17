package com.egg.electric_app.Repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.egg.electric_app.Entities.Factory;

@Repository
public interface FactoryRepository extends JpaRepository<Factory, UUID>{
    
}
