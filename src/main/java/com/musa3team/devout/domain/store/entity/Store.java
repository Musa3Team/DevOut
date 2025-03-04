package com.musa3team.devout.domain.store.entity;

import com.musa3team.devout.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Time;

@Getter
@Entity
@Table(name = "store")
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int ownerId;
    private String telephoneNumber;
    private String address;
    private String contents;
    private String name;
    private Time openTime;
    private Time closeTime;
    private String status;
    private int minimumPrice;
    private String category;
}
