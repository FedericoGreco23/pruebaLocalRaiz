package com.vpi.springboot.Modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Direccion {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String calle;
    private String nroPuerta;
    private String nombre;
    @OneToOne
    private GeoLocalizacion geoLocalizacion;
	@ManyToOne
	@JoinColumn(name="mail")
	private Cliente cliente;

    
    public Direccion() {
    }

    public Direccion(String calle, String nroPuerta, String nombre, GeoLocalizacion geoLocalizacion) {
        this.calle = calle;
        this.nroPuerta = nroPuerta;
        this.nombre = nombre;
        this.geoLocalizacion = geoLocalizacion;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNroPuerta() {
        return nroPuerta;
    }

    public void setNroPuerta(String nroPuerta) {
        this.nroPuerta = nroPuerta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public GeoLocalizacion getGeoLocalizacion() {
        return geoLocalizacion;
    }

    public void setGeoLocalizacion(GeoLocalizacion geoLocalizacion) {
        this.geoLocalizacion = geoLocalizacion;
    }
    
    

    public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
    public String toString() {
        return "Direccion{" +
                "id=" + id +
                ", calle='" + calle + '\'' +
                ", nroPuerta='" + nroPuerta + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
