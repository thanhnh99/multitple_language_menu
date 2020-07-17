package com.multiple_language_menu.models.entities;

import com.multiple_language_menu.enums.EActionType;
import com.multiple_language_menu.enums.ETargetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Logs extends BaseEntity{
    @Id
    @GeneratedValue
    private UUID id = UUID.randomUUID();
    private String target;
    private ETargetType targetType ;
    private String action;
    private EActionType actionType;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shops shop;
}
