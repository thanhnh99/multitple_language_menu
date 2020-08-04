package com.multiple_language_menu.models.responses.dataResponse;

import com.multiple_language_menu.enums.EActionType;
import com.multiple_language_menu.enums.ETargetType;
import com.multiple_language_menu.models.entities.Logs;
import lombok.Data;

import java.util.Date;

@Data
public class ResLog {
    String target;
    ETargetType targetType;
    String action;
    EActionType actionType;
    Date createAt;
    Date updateAt;

    public ResLog(Logs log)
    {
        this.target = log.getTarget();
        this.targetType = log.getTargetType();
        this.action = log.getAction();
        this.actionType = log.getActionType();
        this.createAt = log.getCreatedAt();
        this.updateAt = log.getUpdatedAt();
    }
}
