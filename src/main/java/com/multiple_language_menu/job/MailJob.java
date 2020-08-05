package com.multiple_language_menu.job;

import com.multiple_language_menu.models.request.ReqSendMail;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;


@Service
public class MailJob implements Job {

//    @Autowired
//    public JavaMailSender emailSender;



    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            ReqSendMail reqSendMail = (ReqSendMail) context.getJobDetail().getJobDataMap().get("data");
            JavaMailSender emailSender = (JavaMailSender) context.getJobDetail().getJobDataMap().get("emailSender");
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(reqSendMail.getSendTo());
            message.setText(reqSendMail.getContent());
            message.setSubject("Translated");
            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

