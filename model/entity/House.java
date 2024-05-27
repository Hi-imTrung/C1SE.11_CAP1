package com.example.iHome.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "house")
public class House extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Account owner;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "address")
    private String address;

    @Column(name = "districts")
    private String districts;

    @Column(name = "description")
    private String description;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "banking")
    private String banking;

    @Column(name = "price")
    private double price;

    @Column(name = "number-guest")
    private int numberGuest;

    @Column(name = "size-space")
    private int sizeSpace;

    @Column(name = "room")
    private int room;

    @Column(name = "status")
    private boolean status;

}
