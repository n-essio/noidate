package com.noidate.core.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

@Entity
@Table(name = "customers")

@FilterDef(name = "Customer.obj.code", parameters = @ParamDef(name = "code", type = String.class))
@Filter(name = "Customer.obj.code", condition = "code = :code")

@FilterDef(name = "Customer.like.name", parameters = @ParamDef(name = "name", type = String.class))
@Filter(name = "Customer.like.name", condition = "lower(name) LIKE :name")

@FilterDef(name = "Customer.obj.active", parameters = @ParamDef(name = "active", type = Boolean.class))
@Filter(name = "Customer.obj.active", condition = "active = :active")

public class Customer extends PanacheEntityBase {

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", unique = true)
    @Id
    public String uuid;
    public String code;
    public String name;
    public String postalnumber;
    public String vatnumber;

    public String email;
    public String phonenumber;
}
