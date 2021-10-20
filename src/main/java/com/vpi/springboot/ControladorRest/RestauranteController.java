package com.vpi.springboot.ControladorRest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Logica.RestauranteService;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.exception.PermisosException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;
import com.vpi.springboot.security.util.JwtUtil;
import com.vpi.springboot.security.util.JwtUtil.keyInfoJWT;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/restaurante/")
public class RestauranteController {

	@Autowired
	private RestauranteService service;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private HttpServletRequest request;

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/crearMenu")
	public ResponseEntity<?> altaMenu(@RequestBody Producto menu) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			service.altaMenu(menu, getMailFromJwt());
			return new ResponseEntity<Producto>(menu, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/eliminarMenu/{id}")
	public ResponseEntity<?> bajaMenu(@PathVariable int id) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			service.bajaMenu(id);
			return new ResponseEntity<String>("Eliminado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/modificarMenu")
	public ResponseEntity<?> modificarMenu(@RequestBody Producto menu) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			service.modificarMenu(menu);
			return new ResponseEntity<Producto>(menu, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getPedidos")
	public ResponseEntity<?> listarPedidos(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			
			return new ResponseEntity<Map<String, Object>>(service.listarPedidos(page, size, getMailFromJwt()),
					HttpStatus.OK);
		} catch (RestauranteException e) {
			e.printStackTrace();
			return null;
		}
	}

	@PostMapping("/abrirRestaurante")
	ResponseEntity<?> abrirRestaurante() {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			service.abrirRestaurante(getMailFromJwt());
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Restaurante abierto"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/cerrarRestaurante")
	ResponseEntity<?> cerrarRestaurante() {
		if (!esRestaurante()) {
			return new ResponseEntity<>(
					new UsuarioException(PermisosException.NoPermisosException("RESTAURANTE")).getMessage(),
					HttpStatus.FORBIDDEN);
		}

		try {
			service.cerrarRestaurante(getMailFromJwt());
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Restaurante cerrado"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/// PRIVADAS PARA JWT ///
	/////////////////////////

	private Boolean esRestaurante() {
		return getInfoFromJwt(keyInfoJWT.user_type.name()).contains("RESTAURANTE");
	}

	private String getInfoFromJwt(String infoName) {
		// obtenemos el token del header y le sacamos "Bearer "
		final String authorizationHeader = request.getHeader("Authorization");

		String infoSolicitada = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			switch (infoName) {
			case "mail":
				infoSolicitada = jwtUtil.extractUsername(jwt);
			case "user_type":
				infoSolicitada = jwtUtil.extractUserType(jwt);
			}
		}

		return infoSolicitada;
	}

	private String getMailFromJwt() {
		// obtenemos el token del header y le sacamos "Bearer "
		final String authorizationHeader = request.getHeader("Authorization");

		String mail = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			mail = jwtUtil.extractUsername(jwt);
		}
		return mail;
	}
}
