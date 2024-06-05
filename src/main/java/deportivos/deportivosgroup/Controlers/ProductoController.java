package deportivos.deportivosgroup.Controlers;


import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deportivos.deportivosgroup.Entities.Producto;
import deportivos.deportivosgroup.Repositories.ProductoRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;







@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<Producto> getAllProductos(){
        return productoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Producto obtenerProductoPorId(@PathVariable Long id) {
        return  productoRepository.findById(id)
        .orElseThrow(() -> new RuntimeErrorException(null, "no se encontro el producto con ese ID : " + id));
    }
    

    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto){
        return productoRepository.save(producto);
    }

    @PutMapping("/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto detalleProducto) {
        
        
        Producto producto = productoRepository.findById(id)
        .orElseThrow(() -> new RuntimeErrorException(null, "no se encontro el producto con ese ID : " + id));
        producto.setTitulo(detalleProducto.getTitulo());
        producto.setPrecio(detalleProducto.getPrecio());
        producto.setCategoria(detalleProducto.getCategoria());
        producto.setColor(detalleProducto.getColor());
        producto.setMarca(detalleProducto.getMarca());
        producto.setUrl(detalleProducto.getUrl());

        return productoRepository.save(producto);
    }
    
    
    @DeleteMapping("/{id}")
    public String borrarProducto(@PathVariable Long id){
        Producto producto = productoRepository.findById(id)
        .orElseThrow(() -> new RuntimeErrorException(null, "no se encontro el producto con ese ID : " + id));

        productoRepository.delete(producto);

        return "El Producto fue eliminado";
    }
}
