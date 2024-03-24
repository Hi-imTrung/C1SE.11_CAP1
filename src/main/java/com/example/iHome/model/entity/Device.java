package com.example.iHome.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "device")
public class Device extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "house_id")
    private House house;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "progress")
    private String progress;

    @Column(name = "image")
    private String image;

    @Column(name = "amount")
    private int amount;

    @Column(name = "status")
    private boolean status;

}
