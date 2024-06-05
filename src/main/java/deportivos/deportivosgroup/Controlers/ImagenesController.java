package deportivos.deportivosgroup.Controlers;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deportivos.deportivosgroup.Repositories.ImagenesRepositories;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import deportivos.deportivosgroup.Entities.Imagenes;






@RestController
@RequestMapping("/imagenes")
public class ImagenesController {

    @Autowired
    private ImagenesRepositories imagenesRepositories;

    @GetMapping
    public List<Imagenes> GetAllImagenes() {
        return imagenesRepositories.findAll();

    }

    @GetMapping("/{id}")
    public Imagenes ImagenePorId(@PathVariable Long id) {
        return imagenesRepositories.findById(id)
                .orElseThrow(()-> new RuntimeErrorException(null,"No se encontro la imagen"));
    }
    
    @PostMapping
    public Imagenes crearImagen(@RequestBody Imagenes imagenes){
        return imagenesRepositories.save(imagenes);
    }

    @PutMapping("/{id}")
    public Imagenes actualizarImagen(@PathVariable Long id, @RequestBody Imagenes detalleImagen){
        Imagenes imagenes = imagenesRepositories.findById(id)
                    .orElseThrow(()-> new RuntimeErrorException(null, "No se encontro la imagen"));
        
        imagenes.setUrl(detalleImagen.getUrl());
        imagenes.setProducto(detalleImagen.getProducto());

        return imagenesRepositories.save(imagenes);

    }
    


    @DeleteMapping("/{id}")
    public String borrarImagen(@PathVariable Long id){
        Imagenes imagenes = imagenesRepositories.findById(id)
                    .orElseThrow(()-> new RuntimeErrorException(null, "No se encontro la imagen"));
        imagenesRepositories.delete(imagenes);

        return "La imagen fue eliminada exitosamente";
    }
}
