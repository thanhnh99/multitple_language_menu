package com.multiple_language_menu.models.responses.dataResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResLogin {
    String token;
    ArrayList<String> role;
}
