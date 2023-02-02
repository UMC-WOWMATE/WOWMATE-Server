package com.wowmate.server.user.service.impl;

import com.wowmate.server.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    public static String ePw;

    private MimeMessage createMessage(String to)throws Exception{

        ePw = createKey();

        System.out.println("보내는 대상 : "+ to);
        System.out.println("인증 번호 : "+ePw);

        MimeMessage  message = emailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
        message.setSubject("[WOWMATE] 인증번호 발송");//제목

        String msgg="";
        msgg+= "<div style='margin:20px;'>";
        msgg+= "<table border='0' cellpadding='0' cellspacing='0' align='center' style='width:100%;max-width:630px;margin-left:auto;margin-right:auto;letter-spacing:-1px'>";
        msgg+= "<tbody>";
        msgg+= "<tr><td style='font-size:44px;line-height:48px;font-weight:bold;padding-bottom:60px;text-align:left;letter-spacing:-1px;'>";
        msgg+= "<span>이메일 인증번호 안내</span>";
        msgg+= "</td></tr>";
        msgg+= "<tr><td style='font-size:28px;font-weight:bold;color:purple;'>";
        msgg+= "<span>WOWMATE</span>";
        msgg+= "</td></tr>";
        msgg+= "<tr><td style='padding-bottom:50px;font-size:14px;line-height:22px;text-align:left;letter-spacing:-1px;'>";
        msgg+= "<p style='margin:0;padding:0'>본 메일은 WOWMATE 회원가입을 위한 이메일 인증입니다.</p>";
        msgg+= "<p style='margin:0;padding:0'>아래의 [이메일 인증번호]를 입력하여 본인확인을 해주시기 바랍니다.</p>";
        msgg+= "</td></tr>";
        msgg+= "<tr><td style='padding-bottom:50px'>";
        msgg+=    "<table border='0' cellpadding='0' cellspacing='0' align='center' style='width:100%'>";
        msgg+=        "<tbody>";
        msgg+=            "<tr><td style='padding:50px 30px;border:1px solid #eeeeee;background:#fbfbfb;text-align:center'>";
        msgg+=                "<p style='margin:0;padding:0;font-size:18px;font-weight:bold;letter-spacing:-1px;'>" + ePw + "</p>";
        msgg+=            "</td></tr>";
        msgg+=    "</tbody></table>";
        msgg+= "</td></tr>";
        msgg+= "<tr><td style='font-size:14px;line-height:22px;text-align:left;letter-spacing:-1px;'>";
        msgg+= "<p style='margin:0;padding:0'>감사합니다.<br>WOWMATE 드림</p>";
        msgg+= "</td></tr>";
        msgg+= "</tbody></table>";
        message.setText(msgg, "utf-8", "html");//내용
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
}
