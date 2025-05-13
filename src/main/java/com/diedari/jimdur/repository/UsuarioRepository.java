package com.diedari.jimdur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.diedari.jimdur.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


}
