package com.elphie.savings.models;

// =============================================================================
// File Name: model/Savings.java
// File Description:
// This file contains the Savings Entity Class
// =============================================================================

// =============================================================================
// Entity Imports
// =============================================================================
import java.sql.Timestamp;
import java.sql.Date;

import jakarta.persistence.*;

// =============================================================================
// Entity Class
// =============================================================================
@Entity
@Table(name = "savings")
public class Savings {
    
    // PROPERTIES ////////////////
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "account_id")
    private long accountId;

    @Column(name = "name")
    private String name;

    @Column(name = "target_amount")
    private Integer targetAmount;

    @Column(name = "target_date")
    private Date targetDate;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    // DEFAULT CONSTRUCTOR ////////////////
    public Savings() {}

    // CONSTRUCTORS ////////////////

    public Savings(long userId, long accountId, String name, Integer targetAmount, Date targetDate) {
        this.userId = userId;
        this.accountId = accountId;
        this.name = name;
        this.targetAmount = targetAmount;
        this.targetDate = targetDate;
    }

    // GETTERS & SETTERS ////////////////
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTargetAmount() {
        return this.targetAmount;
    }

    public void setTargetAmount(Integer targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Date getTargetDate() {
        return this.targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public Timestamp getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getUpdatedOn() {
        return this.updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }
}
