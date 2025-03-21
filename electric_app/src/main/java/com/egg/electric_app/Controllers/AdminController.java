package com.egg.electric_app.Controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String firstPanelAdm() {
        return "panel.html";
    }

    @GetMapping("/usuarios")
    public String listar(ModelMap model) {

        List<UserElectric> users = userService.getAll();
        model.addAttribute("users", users);
        return "user_list.html";
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable UUID id) {
        userService.changeRol(id);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuario/{id}")
    public String modificarUsuario(@PathVariable UUID id, ModelMap modelo) {
        UserElectric user = userService.getById(id);
        modelo.addAttribute("user", user);

        return "user_edit.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(MultipartFile file, @PathVariable UUID id, @RequestParam String email,
            @RequestParam String name,
            @RequestParam String lastName, @RequestParam String password, ModelMap modelo) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/inicio"; 
        }

        try {
            userService.update(file, id, email, name, lastName, password);
            modelo.put("good", "El usuario fue actualizado correctamente.");
            return "redirect:/admin/usuarios";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", name);
            modelo.put("email", email);
            modelo.put("apellido", lastName);
            return "user_edit.html";
        }
    }

}
