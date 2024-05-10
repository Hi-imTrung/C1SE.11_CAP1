package com.example.iHome.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "bill")
public class Bill extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private String type;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "new_value")
    private double newValue;

    @Column(name = "old_value")
    private double oldValue;

    @Column(name = "price")
    private double price;

    @Column(name = "status")
    private boolean status;

}
