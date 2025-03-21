package com.egg.electric_app.Controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.egg.electric_app.Entities.UserElectric;
import com.egg.electric_app.Exceptions.MyException;
import com.egg.electric_app.Services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class PortalController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/registrar")
    public String register() {
        return "registro.html";
    }

    @PostMapping("/registro")
    public String saveUser(@RequestParam String email, @RequestParam String name, @RequestParam String lastName,
                            @RequestParam String password, ModelMap model, MultipartFile file) {
        try{
            userService.save(file, email, name, lastName, password);
            model.put("good", "User created successfully.");
            return "index.html";

        }catch (MyException ex){
            model.put("error", ex.getMessage());
            model.put("name", name);
            model.put("email", email);    
            model.put("lastName", lastName);

            return "registro.html";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "User or password incorrect.");
        }
        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String index(HttpSession session) {
        UserElectric logueado = (UserElectric) session.getAttribute("usersession");

        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        return "inicio.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String profile(ModelMap modelo, HttpSession session) {
        UserElectric user = (UserElectric) session.getAttribute("usersession");
        modelo.put("user", user);
        
        return "user_edit.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String update(MultipartFile file, @PathVariable UUID id, @RequestParam String email, @RequestParam String name, 
        @RequestParam String lastName, @RequestParam String password, ModelMap modelo) {
        
            try{
                userService.update(file, id, name, email, lastName, password);
                modelo.put("good", "User updated successfully.");
                return "inicio.html";
            } catch (MyException ex) {
                modelo.put("error", ex.getMessage());
                modelo.put("name", name);
                modelo.put("email", email);
                modelo.put("lastName", lastName);
                return "usuario_modificar.html";
            }
    }

    @GetMapping("/logout")
    public String logout() {
        return "login.html";
    }

    
}
