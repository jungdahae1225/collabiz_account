package com.example.collabiz_account.event;

import com.example.collabiz_account.Account;
import com.example.collabiz_account.EEmail;
import com.example.collabiz_account.infra.mail.EmailMessage;
import com.example.collabiz_account.infra.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Async
@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class AccountEventListener {
    private final EmailService emailService;
    private final TemplateEngine templateEngine;


    @EventListener
    public void handleSignUpConfirmEvent(SignUpConfirmEvent event) {
        //throw new RuntimeException();
        EEmail email = event.getEmail();
        sendEmailCheckToken(email);
    }

    private void sendEmailCheckToken(EEmail email) {
        Context context = new Context(); // model에 내용담아주듯이
        context.setVariable("token",email.getEmailCheckToken());
        context.setVariable("usermail", email.getEmail());
        context.setVariable("message","COLLABIZ 서비스 사용을 위해 코드를 복사하여 붙여넣어주세요.");

        String message = templateEngine.process("mail/emailAuth_Template", context);

        EmailMessage build = EmailMessage.builder()
                .to(email.getEmail())
                .subject("COLLABIZ 회원 가입 인증")
                .message(message)
                .build();

        emailService.sendEmail(build);
    }

}
