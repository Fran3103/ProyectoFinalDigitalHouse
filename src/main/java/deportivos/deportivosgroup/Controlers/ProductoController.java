package deportivos.deportivosgroup.Controlers;

import java.io.File;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import deportivos.deportivosgroup.Entities.Imagenes;
import deportivos.deportivosgroup.Entities.Producto;
import deportivos.deportivosgroup.Repositories.ProductoRepository;
import deportivos.deportivosgroup.Repositories.ImagenesRepositories;


@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ImagenesRepositories imagenesRepositories;

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
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("titulo") String titulo,
            @RequestParam("precio") Double precio,
            @RequestParam("marca") String marca,
            @RequestParam("color") String color,
            @RequestParam("categoria") String categoria) {
    
        String uploadDirectory = System.getProperty("user.dir") + "/uploads";
        File dir = new File(uploadDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    
        List<Imagenes> imagenes = new ArrayList<>();
        Producto producto = new Producto();
        producto.setTitulo(titulo);
        producto.setPrecio(precio);
        producto.setMarca(marca);
        producto.setColor(color);
        producto.setCategoria(categoria);
        productoRepository.save(producto);
    
        // for (MultipartFile file : files) {
        //     String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        //     File dest = new File(uploadDirectory + "/" + fileName);
        //     try {
        //         file.transferTo(dest);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el archivo");
        //     }
    
        //     Imagenes imagen = new Imagenes();
        //     imagen.setUrl("/uploads/" + fileName);
        //     imagen.setProducto(producto);
        //     imagenes.add(imagen);
        // }
    
        imagenesRepositories.saveAll(imagenes);
    
        return ResponseEntity.ok("Archivos subidos exitosamente y producto creado");
    }

    
    

    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Path file = Paths.get(System.getProperty("user.dir") + "/uploads").resolve(filename);
        Resource resource;
        try {
            resource = new UrlResource(file.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("No se puede leer el archivo: " + filename);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
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

    // agrego funcion para eliminar todos los productos
    @DeleteMapping("/productos")
    public String allProductos() {

        productoRepository.deleteAll();
        return "Todos los productos han sido eliminados";
    }
}
