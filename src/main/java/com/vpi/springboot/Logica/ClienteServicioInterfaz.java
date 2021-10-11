package com.vpi.springboot.Logica;

import java.util.List;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.exception.UsuarioException;

public interface ClienteServicioInterfaz {
	public List<DTDireccion> getDireccionCliente(String mail) throws UsuarioException;
	
	public void altaCliente(Cliente usuario) throws UsuarioException, Exception;
	
	public List<Cliente> obtenerClientes();
	
	public void altaDireccion(DTDireccion direccion, String mail) throws UsuarioException;
	
	public void bajaCuenta(String mail) throws UsuarioException;

	void modificarDireccion(int id, DTDireccion nueva, String mail) throws UsuarioException;

	void eliminarDireccion(Integer id, String mail) throws UsuarioException;
}