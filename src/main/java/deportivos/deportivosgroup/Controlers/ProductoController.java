package deportivos.deportivosgroup.Controlers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.management.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import deportivos.deportivosgroup.Entities.Producto;
import deportivos.deportivosgroup.Repositories.ProductoRepository;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Producto obtenerProductoPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeErrorException(null, "no se encontro el producto con ese ID : " + id));
    }

    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("titulo") String titulo,
            @RequestParam("precio") Double precio,
            @RequestParam("marca") String marca,
            @RequestParam("color") String color,
            @RequestParam("categoria") String categoria) {

        // Define la ruta donde se almacenarán los archivos
        String uploadDirectory = System.getProperty("user.dir") + "/uploads";
        File dir = new File(uploadDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Genera un nombre único para el archivo y guarda la imagen
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File dest = new File(uploadDirectory + "/" + fileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el archivo");
        }

        // Crea y guarda el producto en la base de datos
        Producto producto = new Producto();
        producto.setTitulo(titulo);
        producto.setPrecio(precio);
        producto.setMarca(marca);
        producto.setColor(color);
        producto.setCategoria(categoria);
        producto.setUrl("/uploads/" + fileName);

        productoRepository.save(producto);

        return ResponseEntity.ok("Archivo subido exitosamente y producto creado");
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
    public String borrarProducto(@PathVariable Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeErrorException(null, "no se encontro el producto con ese ID : " + id));

        productoRepository.delete(producto);

        return "El Producto fue eliminado";
    }
}