package com.wowmate.server.user.service.impl;

import com.wowmate.server.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    public static String ePw;


    private final SpringTemplateEngine templateEngine;

    private MimeMessage createMessage(String to)throws Exception{

        ePw = createKey();

        System.out.println("보내는 대상 : "+ to);
        System.out.println("인증 번호 : "+ePw);

        MimeMessage  message = emailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
        message.setSubject("[WOWMATE] 인증번호 발송");//제목

        Context context = new Context();
        context.setVariable("ePw", ePw);

        String html = templateEngine.process("authcode", context);
        message.setText(html, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("gyun1712@gmail.com","WOWMATE"));//보내는 사람

        return message;
    }

    private MimeMessage createAccusation(String accuser, String accused, String category, String ID, String content) throws Exception{

        String to = "gyun1712@gmail.com";

        System.out.println("보내는 대상 : "+to);
        System.out.println("신고 내용 : "+ category + " " + ID + " " + content);

        MimeMessage  message = emailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
        message.setSubject("[WOWMATE] 신고 접수");//제목

        Context context = new Context();
        context.setVariable("accuser", accuser);
        context.setVariable("accused", accused);
        context.setVariable("category", category);
        context.setVariable("ID", ID);
        context.setVariable("content", content);

        String html = templateEngine.process("accusation", context);
        message.setText(html, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("gyun1712@gmail.com","WOWMATE"));//보내는 사람

        return message;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    @Override
    public String sendSimpleMessage(String to) throws Exception {
        // TODO Auto-generated method stub
        MimeMessage message = createMessage(to);
        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }

    @Override
    public void sendAccusationMessage(String accuser, String accused, String category, String ID, String content) throws Exception {
        // TODO Auto-generated method stub
        MimeMessage message = createAccusation(accuser, accused, category, ID, content);
        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
