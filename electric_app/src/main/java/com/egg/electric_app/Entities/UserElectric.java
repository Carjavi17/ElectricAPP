package com.egg.electric_app.Entities;

import java.util.UUID;
import com.egg.electric_app.Enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserElectric {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idUser;

    private String email;
    private String name;
    private String lastName;
    private String password;

    @OneToOne
    private ImageUser image;
    
    @Enumerated(EnumType.STRING)
    private Rol rol;

    
    
}