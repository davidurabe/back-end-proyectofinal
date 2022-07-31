package com.trabajofinal.demo.Controller;

import com.trabajofinal.demo.Entity.Persona;
import com.trabajofinal.demo.Interface.IPersonaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class PersonaController {

    @Autowired
    IPersonaService ipersonaService;

    @GetMapping("persona/traer")
    public List<Persona> getPersona() {
        return ipersonaService.getPersona();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("persona/crear")
    public String createPersona(@RequestBody Persona persona) {
        ipersonaService.savePersona(persona);
        return "la persona fue creada con exito";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("persona/delete/id")
    public String deletePersona(@PathVariable Long id) {
        ipersonaService.deletePersona(id);
        return "la persona fue eliminada correctamente";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("persona/editar/id")
    public Persona editPersona(@PathVariable Long id, @RequestParam("nombre") String nuevoNombre,
            @RequestParam("apellido") String nuevoApellido,
            @RequestParam("sobre_mi") String nuevoSobre_mi) {
        Persona persona = ipersonaService.findPersona(id);
        persona.setNombre(nuevoNombre);
        persona.setApellido(nuevoApellido);
        persona.setSobre_mi(nuevoSobre_mi);
        ipersonaService.savePersona(persona);
        return persona;

    }
   
    @GetMapping("persona/traer/perfil")
    public Persona findPersona() {
        return ipersonaService.findPersona((long) 1);
    }
}
