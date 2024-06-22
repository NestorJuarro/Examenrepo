package com.ugb.controlesbasicos;

public class VEHICULOS {
    String idVehiculo;
    String nombre;
    String direccion;
    String telefono;
    String email;
    String dui;
    String urlFotoVehiculo;

    public VEHICULOS(String idVehiculo, String nombre, String direccion, String telefono, String email, String dui, String urlFoto) {
        this.idVehiculo = idVehiculo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.dui = dui;
        this.urlFotoVehiculo = urlFoto;
    }

    public String getUrlFotoVehiculo() {
        return urlFotoVehiculo;
    }

    public void setUrlFotoVehiculo(String urlFotoVehiculo) {
        this.urlFotoVehiculo = urlFotoVehiculo;
    }

    public String getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(String idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }
}
