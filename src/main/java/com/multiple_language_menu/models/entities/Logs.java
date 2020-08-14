package com.multiple_language_menu.models.entities;

import com.multiple_language_menu.constants.EActionType;
import com.multiple_language_menu.constants.ETargetType;
import com.multiple_language_menu.models.request.ReqCreateLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Logs extends BaseEntity{
    private String targetId;
    private String targetName;
    private ETargetType targetType ;
    private EActionType actionType;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shops shop;

    public Logs(ReqCreateLog requestData)
    {
        this.targetId = requestData.getTargetId();
        this.targetName = requestData.getTargetName();
        this.targetType = requestData.getTargetType();
        this.actionType = requestData.getActionType();
    }
}
