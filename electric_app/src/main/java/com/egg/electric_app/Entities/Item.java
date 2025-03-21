package com.egg.electric_app.Entities;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
public class Item {

    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idItem;

    private Integer code;
    private String name;
    private String description;
    private double price;
    private int stock;

    @OneToOne
    private ImageItem image;

    @ManyToOne
    private Factory factory;
    
    public Item(String name, String description, double price, int stock, ImageItem image, Factory factory) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.factory = factory;
        this.code = atomicInteger.incrementAndGet();
    }
   
    
}
