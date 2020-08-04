package com.multiple_language_menu.models.request;

import lombok.Data;

@Data
public class ReqSendMail {
    private String sendTo;
    private String content;
    private String subject;
}
