package com.saims.sgrf.config;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.thymeleaf.context.Context;

import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.RdgModel;
import com.saims.sgrf.model.SettingsModel;
import com.saims.sgrf.model.SrfModel;

public class EmailTemplate {

	public Context getEmailSrfPendiente(SrfModel formulario, SettingsModel setting) {

		String uri = (String) setting.getValue().get("dominio");
		uri = uri + "/srf/estado/pendiente/detalle/" + formulario.getToken();

		Context contexto = new Context();
		contexto.setVariable("titulo", (String) formulario.getFormato().get("titulo"));
		contexto.setVariable("numero", formulario.getNumero());
		contexto.setVariable("codigo",
				formulario.getEmpleado().getPersona().getIdp() + " | " + formulario.getGestion());
		contexto.setVariable("fechaSolicitud", formulario.getFechaSolicitud());
		contexto.setVariable("seccionUno", (String) formulario.getFormato().get("seccionUno"));
		contexto.setVariable("nombre",
				formulario.getEmpleado().getPersona().getNombre() + " "
						+ formulario.getEmpleado().getPersona().getPaterno() + " "
						+ formulario.getEmpleado().getPersona().getMaterno());
		contexto.setVariable("idpName", (String) setting.getValue().get("idp"));
		contexto.setVariable("idp", formulario.getEmpleado().getPersona().getIdp());
		contexto.setVariable("cargo", formulario.getCargo());
		contexto.setVariable("centro", formulario.getCentrocosto());
		contexto.setVariable("cc", formulario.getCodigo());
		contexto.setVariable("seccionDos", (String) formulario.getFormato().get("seccionDos"));
		contexto.setVariable("fechaTitulo", (String) formulario.getFormato().get("fechaTitulo"));
		contexto.setVariable("fechaEstimada", formulario.getFechaEstimada().toString().substring(0, 10));
		contexto.setVariable("montoTitulo", (String) formulario.getFormato().get("montoTitulo"));
		contexto.setVariable("monto", formulario.getMonto() + " " + formulario.getMoneda());
		contexto.setVariable("detalleTitulo", (String) formulario.getFormato().get("descripcion"));
		contexto.setVariable("detalle", formulario.getDetalle());
		contexto.setVariable("uri", uri);

		return contexto;
	}

	public Context getEmailSrfEstado(SrfModel formulario, SettingsModel setting, Estado estado) {

		String uri = (String) setting.getValue().get("dominio");
		uri = uri + "/srf/estado/" + estado.toString().toLowerCase() + "/detalle/" + formulario.getToken();
		String color = "";
		switch (estado) {
		case APROBADO:
			color = "#18BA54";
			break;
		case RECHAZADO:
			color = "#D6806B";
			break;
		case DEVUELTO:
			color = "#5CC4B9";
			break;
		}

		Context contexto = new Context();
		contexto.setVariable("estado", estado.name());
		contexto.setVariable("titulo", (String) formulario.getFormato().get("titulo"));
		contexto.setVariable("numero", formulario.getNumero());
		contexto.setVariable("codigo",
				formulario.getEmpleado().getPersona().getIdp() + " | " + formulario.getGestion());
		contexto.setVariable("fechaSolicitud", formulario.getFechaSolicitud());
		contexto.setVariable("seccionDos", (String) formulario.getFormato().get("seccionDos"));
		contexto.setVariable("fechaTitulo", (String) formulario.getFormato().get("fechaTitulo"));
		contexto.setVariable("fechaEstimada", formulario.getFechaEstimada().toString().substring(0, 10));
		contexto.setVariable("montoTitulo", (String) formulario.getFormato().get("montoTitulo"));
		contexto.setVariable("monto", formulario.getMonto() + " " + formulario.getMoneda());
		contexto.setVariable("detalleTitulo", (String) formulario.getFormato().get("descripcion"));
		contexto.setVariable("detalle", formulario.getDetalle());
		contexto.setVariable("uri", uri);
		contexto.setVariable("colorVariable", color);

		return contexto;
	}

	public Context getEmailRdgPendiente(RdgModel formulario, SettingsModel setting) {

		String uri = (String) setting.getValue().get("dominio");
		uri = uri + "/rdg/autorizarrdg/pendiente/revisar/" + formulario.getToken();

		Context contexto = new Context();
		contexto.setVariable("titulo", (String) formulario.getFormato().get("titulo"));
		contexto.setVariable("seccionUno", (String) formulario.getFormato().get("seccionUno"));
		contexto.setVariable("seccionDos", (String) formulario.getFormato().get("seccionDos"));
		contexto.setVariable("seccionTres", (String) formulario.getFormato().get("seccionTres"));
		
		contexto.setVariable("numero", formulario.getNumero());
		contexto.setVariable("codigo",
				formulario.getEmpleado().getPersona().getIdp() + " | " + formulario.getGestion());
		
		contexto.setVariable("fechaActualizacion", formulario.getFechaActualizacion().toString().substring(0, 16));
		
		contexto.setVariable("nombre",
				formulario.getEmpleado().getPersona().getNombre() + " "
						+ formulario.getEmpleado().getPersona().getPaterno() + " "
						+ formulario.getEmpleado().getPersona().getMaterno());
		
		contexto.setVariable("idpName", (String) setting.getValue().get("idp"));
		contexto.setVariable("idp", formulario.getEmpleado().getPersona().getIdp());
		contexto.setVariable("cargo", formulario.getCargo());
		contexto.setVariable("centro", formulario.getCentroCostos());
		contexto.setVariable("cc", formulario.getCodigo());
		contexto.setVariable("area", formulario.getArea());
		
		contexto.setVariable("cuenta", formulario.getCuenta());
		contexto.setVariable("banco", formulario.getBanco());
		contexto.setVariable("tipoCuenta", formulario.getTipoCuenta());
		
		contexto.setVariable("actividad", formulario.getActividad());
		
		String fechaDelTexto = formulario.getDel().toString().substring(0, 10); // "2026-08-24"
		String fechaAlTexto = formulario.getAl().toString().substring(0, 10);   // "2026-08-29"

		// 2. Las conviertes a objetos LocalDate (Java las reconoce automáticamente en formato yyyy-MM-dd)
		LocalDate fechaDel = LocalDate.parse(fechaDelTexto);
		LocalDate fechaAl = LocalDate.parse(fechaAlTexto);

		// 3. Calculas la diferencia en días
		long diasDiferencia = ChronoUnit.DAYS.between(fechaDel, fechaAl);

		// 4. Guardas las variables en tu contexto (incluyendo los días)
		contexto.setVariable("del", fechaDelTexto);
		contexto.setVariable("al", fechaAlTexto);
		contexto.setVariable("dias", diasDiferencia); 
		
		contexto.setVariable("detalle", formulario.getDetalle());
		contexto.setVariable("participante", formulario.getParticipantes());
		
		
		contexto.setVariable("importe", formulario.getImporte() + " " + formulario.getMoneda());
		contexto.setVariable("saldo", formulario.getSaldo()+ " " + formulario.getMoneda());
		contexto.setVariable("gasto", formulario.getGasto()+ " " + formulario.getMoneda());
		
		contexto.setVariable("uri", uri);

		return contexto;
	}
	
	public Context getEmailRdgEstado(RdgModel formulario, SettingsModel setting, Estado estado) {

		String uri = (String) setting.getValue().get("dominio");
		uri = uri + "/rdg/estadordg/" + estado.toString().toLowerCase() + "/detalle/" + formulario.getToken();
		String color = "";
		switch (estado) {
		case APROBADO:
			color = "#18BA54";
			break;
		case RECHAZADO:
			color = "#D6806B";
			break;
		case DEVUELTO:
			color = "#5CC4B9";
			break;
		}

		Context contexto = new Context();
		contexto.setVariable("estado", estado.name());
		contexto.setVariable("titulo", (String) formulario.getFormato().get("titulo"));
		contexto.setVariable("seccionDos", (String) formulario.getFormato().get("seccionDos"));
		contexto.setVariable("seccionTres", (String) formulario.getFormato().get("seccionTres"));
		
		contexto.setVariable("numero", formulario.getNumero());
		contexto.setVariable("codigo",
				formulario.getEmpleado().getPersona().getIdp() + " | " + formulario.getGestion());
		
		contexto.setVariable("fechaActualizacion", formulario.getFechaActualizacion().toString().substring(0, 16));
		
		contexto.setVariable("actividad", formulario.getActividad());
		
		String fechaDelTexto = formulario.getDel().toString().substring(0, 10); // "2026-08-24"
		String fechaAlTexto = formulario.getAl().toString().substring(0, 10);   // "2026-08-29"

		// 2. Las conviertes a objetos LocalDate (Java las reconoce automáticamente en formato yyyy-MM-dd)
		LocalDate fechaDel = LocalDate.parse(fechaDelTexto);
		LocalDate fechaAl = LocalDate.parse(fechaAlTexto);

		// 3. Calculas la diferencia en días
		long diasDiferencia = ChronoUnit.DAYS.between(fechaDel, fechaAl);

		// 4. Guardas las variables en tu contexto (incluyendo los días)
		contexto.setVariable("del", fechaDelTexto);
		contexto.setVariable("al", fechaAlTexto);
		contexto.setVariable("dias", diasDiferencia); 
		
		contexto.setVariable("detalle", formulario.getDetalle());
		contexto.setVariable("participante", formulario.getParticipantes());
		
		
		contexto.setVariable("importe", formulario.getImporte() + " " + formulario.getMoneda());
		contexto.setVariable("saldo", formulario.getSaldo()+ " " + formulario.getMoneda());
		contexto.setVariable("gasto", formulario.getGasto()+ " " + formulario.getMoneda());
		
		contexto.setVariable("uri", uri);
		contexto.setVariable("colorVariable", color);

		return contexto;
	}
}
