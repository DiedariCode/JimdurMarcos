package com.diedari.jimdur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroUsuarioDTO
{
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private String contrasena;
    private String confirmContrasena;
}
