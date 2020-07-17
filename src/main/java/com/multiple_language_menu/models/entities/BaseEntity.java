package com.multiple_language_menu.models.entities;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity {
    private Date created_at = new Date();
    private String created_by;
    private Date updated_at = new Date();
    private String updated_by;
}
