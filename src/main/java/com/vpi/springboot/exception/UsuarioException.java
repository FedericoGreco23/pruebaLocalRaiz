package com.vpi.springboot.exception;

public class UsuarioException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public UsuarioException(String mensaje) {
		super(mensaje);
	}
	
	public static String NotFoundException(String mail) {
		return "Usuario de mail " + mail + " no se pudo encontrar.";
	}
	
	public static String UsuarioYaExiste(String mail) {
		return "Usuario " + mail + " ya existe.";
	}
	
	public static String PassIncorrecta() {
		return "Contraseña incorrecta.";
	}
	
	public static String NoNickname() {
		return "Debe ingresar nickname.";
	}
	
	public static String NicknameRepetido() {
		return "Nickname en uso";
	}
	
	public static String NoPermisosException (String userTypeRequerido) {
		return "El acceso está restringido para usuarios "+userTypeRequerido;
	}
}
