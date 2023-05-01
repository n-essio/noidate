package com.noidate.core.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "offeredservices")
public class OfferedService extends PanacheEntityBase {
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", unique = true)
    @Id
    public String uuid;
    public String name;
    public String servicecategory_uuid;
    public String servicesubcategory_uuid;
    public String vendor_uuid;
    public String pricelist_uuid;
    public String description;
    public String tags;

}
