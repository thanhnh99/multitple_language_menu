package com.multiple_language_menu.models.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Data
public abstract class BaseEntity {
    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id = UUID.randomUUID().toString();
    private Date createdAT = new Date();
    private String createdBy;
    private Date updatedAt = new Date();
    private String updatedBy;
}
