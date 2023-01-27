package com.rafamoreno.clientechat.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Usuario {
    int id_usuario;
    String nombreUsuario, contraseniaUsuario;
    LocalDateTime fechaAlta, fechaUltimaSesion, fechaSesionActual;

    public Usuario(int id_usuario, String nombreUsuario, String contraseniaUsuario, LocalDateTime fechaAlta) {
        this.id_usuario = id_usuario;
        this.nombreUsuario = nombreUsuario;
        this.contraseniaUsuario = contraseniaUsuario;
        this.fechaAlta = fechaAlta;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }


    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContraseniaUsuario() {
        return contraseniaUsuario;
    }

    public void setContraseniaUsuario(String contraseniaUsuario) {
        this.contraseniaUsuario = contraseniaUsuario;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDateTime getFechaUltimaSesion() {
        return fechaUltimaSesion;
    }

    public void setFechaUltimaSesion(LocalDateTime fechaUltimaSesion) {
        this.fechaUltimaSesion = fechaUltimaSesion;
    }

    public LocalDateTime getFechaSesionActual() {
        return fechaSesionActual;
    }

    public void setFechaSesionActual(LocalDateTime fechaSesionActual) {
        this.fechaSesionActual = fechaSesionActual;
    }
}
