package com.diedari.jimdur.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.service.CategoriaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaRestController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping()
    public List<Categoria> listarCategorias() {
        return categoriaService.obtenerTodasLasCategorias();
    }

    @GetMapping("/activa/{activa}")
    public List<Categoria> listarCategoriasActivas(@PathVariable boolean activa) {
        return categoriaService.obtenerCategoriaPorEstado(activa);
    }

    @GetMapping("/nombre/{nombre}")
    public List<Categoria> listarCategoriasPorNombre(@PathVariable String nombre) {
        return categoriaService.obtenerCategoriaPorNombre(nombre);
    }

    @GetMapping("/{id}")
    public Categoria obtenerCategoriaPorId(@PathVariable Long id) {
        return categoriaService.obtenerCategoriaPorId(id);
    }

    @PostMapping("/crear")
    public Categoria crearCategoria(@RequestBody Categoria categoria) {
        return categoriaService.crearCategoria(categoria);
    }

    // Con el RequestBody se puede recibir un objeto completo
    // Osea pasa ese JSON a un objeto Categoria

    @PutMapping("/actualizar/{id}")
    public Categoria actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        Categoria actual = categoriaService.obtenerCategoriaPorId(id);
        if (actual != null) {
            actual.setNombre(categoria.getNombre());
            actual.setDescripcion(categoria.getDescripcion());
            actual.setActiva(categoria.isActiva());
            return categoriaService.actualizarCategoria(actual);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoriaPorId(id);
    }

}
