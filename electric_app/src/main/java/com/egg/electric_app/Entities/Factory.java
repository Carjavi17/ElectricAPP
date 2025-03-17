package com.egg.electric_app.Entities;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "factories")
@Data
@AllArgsConstructor
public class Factory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idFactory;

    private String name;    

    
}
