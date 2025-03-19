package com.egg.electric_app.Entities;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    private String mime;
    
    private String name;
    
    @Lob @Basic(fetch = FetchType.LAZY)
    private byte[] content;
    
}
