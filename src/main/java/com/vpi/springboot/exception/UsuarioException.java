package com.vpi.springboot.exception;

public class UsuarioException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public UsuarioException(String mensaje) {
		super(mensaje);
	}
	
	public static String NotFoundException(String mail) {
		return "Usuario de mail " + mail + " no se pudo encontrar";
	}
	
	public static String UsuarioYaExiste() {
		return "Usuario ya existe";
	}
	
	public static String PassIncorrecta() {
		return "Contraseña incorrecta";
	}
}