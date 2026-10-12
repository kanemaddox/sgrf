package com.saims.sgrf.service.impl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.saims.sgrf.service.EmailService;

import jakarta.mail.internet.MimeMessage;

@Service
//@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
	
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender mailSender,TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        
    }
	
    @Async
	public void enviarCorreo(String destino,String sujeto, String from,String emailTemplate, Context contexto) {
    	try {
	        //SimpleMailMessage mensaje = new SimpleMailMessage();
	        MimeMessage mensaje = this.mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
	        
	        String template = templateEngine.process(emailTemplate, contexto);
	        
	        helper.setTo(destino);
	        helper.setFrom(from);
	        helper.setSubject(sujeto);
	        helper.setText(template,true);
	        
	        this.mailSender.send(mensaje);
	        
    	}catch(Exception e) {
    		System.err.println("Error al enviar el correo: " + e.getMessage());
    	}
    }

}
