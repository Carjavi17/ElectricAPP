package com.egg.electric_app.Entities;

import java.util.UUID;
import com.egg.electric_app.Enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idUser;

    private String email;
    private String name;
    private String lastName;
    private String password;

    @OneToOne
    private ImageUser image;

    private Rol rol;

    
    
}