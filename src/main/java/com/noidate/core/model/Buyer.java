package com.noidate.core.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import it.ness.queryable.annotations.Q;
import it.ness.queryable.annotations.QLike;
import it.ness.queryable.annotations.QList;
import it.ness.queryable.annotations.QLogicalDelete;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "buyers")
public class Buyer extends PanacheEntityBase {

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", unique = true)
    @Id
    @QList
    public String uuid;
    @QLike
    public String name;
    @QLike
    public String surname;
    @QLike
    public String fiscalcode;
    @QLike
    public String address;
    @QLike
    public String city;
    @QLike
    public String province;
    @Q
    public LocalDateTime creation_date;
    @QLogicalDelete
    public boolean active;

    @Override
    public String toString() {
        return "Buyer{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", fiscalcode='" + fiscalcode + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", creation_date=" + creation_date +
                ", active=" + active +
                '}';
    }
}
