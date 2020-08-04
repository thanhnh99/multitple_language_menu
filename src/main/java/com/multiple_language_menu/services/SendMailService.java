package com.multiple_language_menu.services;

import com.multiple_language_menu.models.request.ReqSendMail;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class SendMailService implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
//    private final Logger logger = (Logger) LogManager.getLogger(getClass());
//    private final JavaMailSender emailSender;
//
//    public SendMailService(JavaMailSender emailSender) {
//        this.emailSender = emailSender;
//    }
//
//
//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//        try {
//            ReqSendMail reqSendMail = (ReqSendMail) context.getJobDetail().getJobDataMap().get("data");
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(reqSendMail.getSendTo());
//            message.setText(reqSendMail.getContent());
//            message.setSubject(reqSendMail.getSubject());
//            emailSender.send(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
