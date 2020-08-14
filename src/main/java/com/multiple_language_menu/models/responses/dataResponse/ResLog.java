package com.multiple_language_menu.models.responses.dataResponse;

import com.multiple_language_menu.constants.EActionType;
import com.multiple_language_menu.constants.ETargetType;
import com.multiple_language_menu.models.entities.Logs;
import lombok.Data;

import java.util.Date;

@Data
public class ResLog {
    String target;
    ETargetType targetType;
    EActionType actionType;
    Date createAt;
    Date updateAt;

    public ResLog(Logs log)
    {
        this.target = log.getTargetName();
        this.targetType = log.getTargetType();
        this.actionType = log.getActionType();
        this.createAt = log.getCreatedAt();
        this.updateAt = log.getUpdatedAt();
    }
}
