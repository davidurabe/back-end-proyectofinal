/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.trabajofinal.demo.Security.Controller;

import com.trabajofinal.demo.Security.Dto.JwtDto;
import com.trabajofinal.demo.Security.Dto.LoginUsuario;
import com.trabajofinal.demo.Security.Dto.NuevoUsuario;
import com.trabajofinal.demo.Security.Entity.Rol;
import com.trabajofinal.demo.Security.Entity.Usuario;
import com.trabajofinal.demo.Security.Enums.RolNombre;
import com.trabajofinal.demo.Security.Service.RolService;
import com.trabajofinal.demo.Security.Service.UsuarioService;
import com.trabajofinal.demo.Security.jwt.JwtProvider;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author david
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    RolService rolService;
    @Autowired
    JwtProvider jwtProvider;
    
    @PostMapping("/nuevo")
    
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campo mal puesto o email invalido"),HttpStatus.BAD_REQUEST);
        if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity(new Mensaje("ese nombre de usuario existe"), HttpStatus.BAD_REQUEST);
         if(usuarioService.existsByEmail(nuevoUsuario.getEmail()))
            return new ResponseEntity(new Mensaje("ese email ya existe"), HttpStatus.BAD_REQUEST);
         Usuario usuario = new Usuario(nuevoUsuario.getNombreUsuario(), nuevoUsuario.getNombreUsuario(),nuevoUsuario.getEmail(),passwordEncoder.encode(nuevoUsuario.getPassword()));
         
         Set<Rol> roles = new HashSet<>();
         roles.add(rolService.getByRolNOmbre(RolNombre.ROLE_USER).get());
         if(nuevoUsuario.getRoles().contains("admin"))
             roles.add(rolService.getByRolNOmbre(RolNombre.ROLE_ADMIN).get());
         usuario.setRoles(roles);
         usuarioService.save(usuario);
         
         return new ResponseEntity(new Mensaje("Usuario guardado"),HttpStatus.CREATED);
      
    }
    @PostMapping("/login")
    public ResponseEntity<JwtDto> Login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
        
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(),loginUsuario.getPassword()));
        SecurityContextHolder .getContext().setAuthentication(authentication);
        
        String jwt = jwtProvider.generateToken(authentication);
        
       UserDetails userDetails = (UserDetails) authentication.getPrincipal();
       JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
       
       return new ResponseEntity(jwtDto, HttpStatus.OK);
    }
}
