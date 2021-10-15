package com.vpi.springboot.ControladorRest;

import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.AdministradorService;
import com.vpi.springboot.Modelo.Administrador;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/admin/")
public class AdministradorController {

	@Autowired
	private AdministradorService service;

//	@GetMapping("/getallClientes")
//	public List<Cliente> getAllUser() {
//		return userService.getAllClientes();
//	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/crear")
	public ResponseEntity<?> altaAdministrador(@RequestBody Administrador admin) {
		try {
			service.crearAdministrador(admin);
			return new ResponseEntity<Administrador>(admin, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/eliminar")
	public ResponseEntity<?> eliminarUsuario(@RequestParam String mail) {
		try {
			service.eliminarUsuario(mail);
			return new ResponseEntity<String>("Eliminado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/bloquear")
	public ResponseEntity<?> bloquearUsuario(@RequestParam String mail, @RequestParam String clienteRestaurante) {
		try {
			service.bloquearUsuario(mail, clienteRestaurante);
			return new ResponseEntity<String>("Bloqueado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/desbloquear")
	public ResponseEntity<?> desbloquearUsuario(@RequestParam String mail, @RequestParam String clienteRestaurante) {
		try {
			service.desbloquearUsuario(mail, clienteRestaurante);
			return new ResponseEntity<String>("Desbloqueado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getUsuarios")
	public Map<String, Object> getUsuarios(@RequestParam(defaultValue = "0") int page,
									  	   @RequestParam(defaultValue = "5") int size, 
									       @RequestParam(defaultValue = "0") int tipoUsuario) {
		return service.listarUsuariosRegistrados(page, size, tipoUsuario);
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getRestaurantes")
	public Map<String, Object> getRestaurantes(@RequestParam(defaultValue = "0") int page,
									  	   @RequestParam(defaultValue = "5") int size, 
									       @RequestParam(defaultValue = "3") int tipo) {
		return service.listarRestaurantes(page, size, tipo);
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/cambiarEstado/{varRestaurante}")
	public ResponseEntity<?> cambiarEstadoRestaurante(@PathVariable String varRestaurante, @RequestParam (required = true) int estado ) {
		try {
			service.cambiarEstadoRestaurante(varRestaurante, estado);
			return new ResponseEntity<String>("Cambio de estado exitoso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}