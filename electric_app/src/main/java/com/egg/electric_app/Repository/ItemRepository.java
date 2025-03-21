package com.egg.electric_app.Repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.egg.electric_app.Entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    @Query("SELECT i FROM Item i WHERE i.name = :name")
    public Item findByName(@Param("name") String nombre);

    @Query("SELECT i FROM Item i WHERE i.code = :code")
    public Item findByCode(@Param("code") Integer code);

    
} 
