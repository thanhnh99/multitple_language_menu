package com.multiple_language_menu.models.entities;

import com.multiple_language_menu.enums.EActionType;
import com.multiple_language_menu.enums.ETargetType;
import com.multiple_language_menu.models.request.ReqCreateLog;
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
    private String target;
    private ETargetType targetType ;
    private String action;
    private EActionType actionType;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shops shop;

    public Logs(ReqCreateLog requestData)
    {
        this.target = requestData.getTarget();
        this.targetType = requestData.getTargetType();
        this.action = requestData.getAction();
        this.actionType = requestData.getActionType();
    }
}
