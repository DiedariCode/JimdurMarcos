package com.diedari.jimdur.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombres;

    @Column(name = "correo", nullable = false, unique = true, length = 100)
    private String correo;

    @Column(name = "numero_telefono", nullable = false, unique = true)
    private String numeroTelefono;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "rol", nullable = false, length = 20)
    private String rol;

    // Campo para almacenar la fecha y hora del último acceso
    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    // Campo para indicar si el usuario está activo o no
    @Column(name = "estado", nullable = false)
    private boolean estado; // true = activo, false = inactivo

    // Constructor vacío
    public Usuario() {
    }

    public Usuario(Long id, String nombres, String correo, String numeroTelefono, String contrasena,
            LocalDateTime ultimoAcceso, boolean estado, String rol) {
        this.id = id;
        this.nombres = nombres;
        this.correo = correo;
        this.numeroTelefono = numeroTelefono;
        this.contrasena = contrasena;
        this.ultimoAcceso = ultimoAcceso;
        this.estado = estado;
        this.rol = rol;
    }

    public Usuario(String nombres, String correo, String numeroTelefono, String contrasena, LocalDateTime ultimoAcceso,
            boolean estado, String rol) {
        this.nombres = nombres;
        this.correo = correo;
        this.numeroTelefono = numeroTelefono;
        this.contrasena = contrasena;
        this.ultimoAcceso = ultimoAcceso;
        this.estado = estado;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nombres=" + nombres + ", correo=" + correo + ", numeroTelefono="
                + numeroTelefono + ", contraseña=" + contrasena + ", ultimoAcceso=" + ultimoAcceso + ", estado="
                + estado + ", rol=" + rol + "]";
    }
}
