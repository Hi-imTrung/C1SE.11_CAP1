package com.example.iHome.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "house_image")
public class HouseImage extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "status")
    private boolean status;

}
