package com.multiple_language_menu.models.responses.dataResponse;


import com.multiple_language_menu.models.entities.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ResUser {
    private String username;
    private String userId;
    private Boolean status;

    public ResUser(Users user)
    {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.status = user.getEnable();
    }
}
