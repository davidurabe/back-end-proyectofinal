
package com.trabajofinal.demo.Interface;

import com.trabajofinal.demo.Entity.Persona;
import java.util.List;


public interface IPersonaService {
    //traer una lista
    public List<Persona> getPersona();
    public void savePersona(Persona persona);
    public void deletePersona(Long id);
    public Persona findPersona(Long id);
}
