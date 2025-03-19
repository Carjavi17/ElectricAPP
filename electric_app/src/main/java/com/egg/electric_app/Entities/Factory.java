package com.egg.electric_app.Entities;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "factories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Factory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idFactory;

    private String name;    

    
}
