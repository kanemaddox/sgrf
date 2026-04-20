package com.saims.sgrf.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Personalizer {
	
	/**
     * Normaliza textos:
     * - Convierte a mayúsculas
     * - Elimina espacios en blanco al inicio y final
     */
	public String normalizer(String value) {
		return value != null ? value.toUpperCase().trim().replaceAll("\\s+", " ") : null;
    }
	
	public String normalizerLower(String value) {
        return value != null ? value.toLowerCase().trim().replaceAll("\\s+", "") : null;
    }
	
	public String normalizerName(String value) {
		return value !=null ? name(value):null;
	}
	private String name(String value) {
		String name = value.trim().replaceAll("\\s+", " ");
		return (name.substring(0, 1).toUpperCase()+ name.substring(1).toLowerCase());
	}
	
	/**
     * 📌 Genera el prefijo de la sucursal
     * ✔ Toma los primeros 2 caracteres del código
     */
    public String generatePrefijo(String codigo) {
        return (codigo != null && codigo.length() >= 2) 
                ? codigo.substring(0, 2) 
                : codigo;
    }
    public String normalizerIdp(String value) {
		return value != null ? value.toUpperCase().trim().replaceAll("\\s+", "") : null;
    }
    
    /**
	 * funsion de creacion de password 
	 * @param value dato para contrasena 
	 * @return value decodificado
	 * @throws Exception
	 */
	public String password(String value) throws Exception{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
		StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
		return hexString.toString();
	}
	
    public String getPass(String password) throws Exception {
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
		StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
		return hexString.toString();
	}

}
