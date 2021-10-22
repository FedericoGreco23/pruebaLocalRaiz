package com.vpi.springboot.ControladorRest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vpi.springboot.Logica.ClienteService;
import com.vpi.springboot.Modelo.dto.DTBuscarRestaurante;
import com.vpi.springboot.Modelo.dto.DTCarrito;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;
import com.vpi.springboot.exception.CarritoException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;
import com.vpi.springboot.security.util.JwtUtil;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/cliente/")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private HttpServletRequest request;

	@GetMapping("/getDireccion")
	public List<DTDireccion> getDireccionUsuario() {
		try {
			// obtiene mail del jwt del header. Si mail es null, devuelve null
			String mail = getMailFromJwt();
			return mail != null ? clienteService.getDireccionCliente(mail) : null;
		} catch (UsuarioException e) {
			return null;
		}
	}

	@PostMapping(path = "/agregarDireccion", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> agregarDireccion(@RequestBody DTDireccion direccion) {
		try {
			return new ResponseEntity<>(clienteService.altaDireccion(direccion, getMailFromJwt()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("bajaCuenta")
	public ResponseEntity<?> bajaCuenta() {
		try {
			return new ResponseEntity<>(clienteService.bajaCuenta(getMailFromJwt()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("modificarDireccion")
	public ResponseEntity<?> modificarDireccion(@RequestParam int id, @RequestBody DTDireccion direccionNueva) {
		try {
			return new ResponseEntity<>(clienteService.modificarDireccion(id, direccionNueva, getMailFromJwt()),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("eliminarDireccion")
	public ResponseEntity<?> eliminarDireccion(@RequestParam Integer id) {
		try {
			return new ResponseEntity<>(clienteService.eliminarDireccion(id, getMailFromJwt()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("agregarACarrito")
	public ResponseEntity<?> agregarACarrito(@RequestParam int producto, Integer cantidad, String mailRestaurante) {
		try {
			String mail = getMailFromJwt();
			// DTProductoCarrito productoCarrito = new DTProductoCarrito(producto,
			// cantidad);
			clienteService.agregarACarrito(producto, cantidad, mail, mailRestaurante);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Producto agregado con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/getLastDireccion")
	public ResponseEntity<?> getLastDireccion() {
		try {
			String mail = getMailFromJwt();
			String respuesta = clienteService.getUltimaDireccionSeleccionada(mail);
			System.out.println(respuesta);
			return mail != null ? new ResponseEntity<String>(respuesta, HttpStatus.OK) : null;
		} catch (Exception e) {
			return null;
		}
	}

	@PostMapping(path = "/lastDireccion/{idDireccion}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> lastDireccion(@PathVariable String idDireccion) {
		try {
			clienteService.setUltimaDireccionSeleccionada(Integer.valueOf(idDireccion), getMailFromJwt());
			return new ResponseEntity<>(new DTRespuesta("Direccion actualizada con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getCarrito")
	public DTCarrito verCarrito() {
		try {
			String mail = getMailFromJwt();
			return mail != null ? clienteService.verCarrito(mail) : null;
		} catch (Exception e) {
			return null;
		}
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

	@PostMapping("/altaPedido")
	public ResponseEntity<DTRespuesta> altaPedido(@RequestParam int idCarrito, @RequestParam int metodoPago,
			@RequestParam int idDireccion, @RequestParam String comentario) {
		try {
			String mail = getMailFromJwt();
			EnumMetodoDePago pago;
			if (metodoPago == 1) {
				pago = EnumMetodoDePago.PAYPAL;
			} else {
				pago = EnumMetodoDePago.EFECTIVO;
			}
			clienteService.altaPedido(idCarrito, pago, idDireccion, mail, comentario);
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Pedido enviado con éxito"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/altaReclamo")
	public ResponseEntity<DTRespuesta> altaReclamo(@RequestParam int idPedidoReclamado,
			@RequestParam String Comentario) {
		try {
			return new ResponseEntity<DTRespuesta>(
					clienteService.altaReclamo(idPedidoReclamado, getMailFromJwt(), Comentario), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/eliminarProductoCarrito")
	public ResponseEntity<DTRespuesta> eliminarProductoCarrito(@RequestParam int idProducto){
		try {
			clienteService.eliminarProductoCarrito(idProducto, getMailFromJwt());
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Producto eliminado con éxito")
					, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/eliminarCarrito")
	public ResponseEntity<DTRespuesta> eliminarCarrito(@RequestParam int idCarrito) {
		try {
			clienteService.eliminarCarrito(idCarrito, getMailFromJwt());
			return new ResponseEntity<DTRespuesta>(new DTRespuesta("Carrito eliminado con éxito")
					, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DTRespuesta>(new DTRespuesta(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}