package com.diedari.jimdur.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.service.MarcaService;



@RestController
@RequestMapping("/api/marca")
public class MarcaRestController {
    
    @Autowired
    private MarcaService marcaService;

    @GetMapping()
    public List<Marca> listarTodasLasMarcas() {
        return marcaService.listarTodasLasMarcas();
    }

    @GetMapping("/id/{id}")
    public Marca obtenerMarcaPorId(@PathVariable Long id) {
        return marcaService.obtenerMarcaPorId(id);
    }

    @GetMapping("/nombre/{nombre}")
    public List<Marca> obtenerMarcaPorNombre(@PathVariable String nombre) {
        return marcaService.obtenerMarcaPorNombres(nombre);
    }
    
    @GetMapping("/estado/{activo}")
    public List<Marca> obtenerMarcaPorSuEstado(@PathVariable boolean activo) {
        return marcaService.obtenerMarcasPorEstado(activo);
    }

    // @PutMapping("/actualizar/{id}")
    // public Marca actualizarMarca(@PathVariable Long id, @RequestBody Marca marca) {
    //     Marca actual = marcaService.obtenerMarcaPorId(id);
    //     if (actual != null) {
    //         actual.setNombre(marca.getNombre());
    //         actual.setDescripcion(marca.getDescripcion());
    //         actual.setLogoUrl(marca.getLogoUrl());
    //         actual.setPaisOrigen(marca.getPaisOrigen());
    //         actual.setSitioWeb(marca.getSitioWeb());
    //         actual.setActivo(marca.isActivo());
    //         return marcaService.actualizarMarca(actual);
    //     } else {
    //         return null;
    //     }
    // }
    
    @PostMapping("/crear")
    public Marca crearMarca(@RequestBody Marca marca) {
        return marcaService.guardarMarcaNuevo(marca);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarMarca(@PathVariable Long id) {
        marcaService.eliminarMarca(id);
    }
    
}
