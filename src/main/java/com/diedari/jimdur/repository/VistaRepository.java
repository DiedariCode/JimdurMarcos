package com.diedari.jimdur.repository;

import com.diedari.jimdur.model.Vista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface VistaRepository extends JpaRepository<Vista, Long> {
    List<Vista> findAll();

    @Query("SELECT DISTINCT v FROM Vista v LEFT JOIN FETCH v.roles")
    List<Vista> findAllWithRoles();

    @Query("SELECT v FROM Vista v LEFT JOIN FETCH v.roles WHERE v.id = :id")
    Optional<Vista> findByIdWithRoles(@Param("id") Long id);

    Optional<Vista> findByPath(String path);
} 