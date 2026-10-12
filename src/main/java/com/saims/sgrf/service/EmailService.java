package com.saims.sgrf.service;

import org.thymeleaf.context.Context;

public interface EmailService {
	void enviarCorreo(String destino,String sujeto, String from,String emailTemplate, Context contexto);

}
