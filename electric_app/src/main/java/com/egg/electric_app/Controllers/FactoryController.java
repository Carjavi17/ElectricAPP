package com.egg.electric_app.Controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.electric_app.Entities.Factory;
import com.egg.electric_app.Exceptions.MyException;
import com.egg.electric_app.Services.FactoryService;

@Controller
@RequestMapping("/fabrica")
public class FactoryController {

    @Autowired 
    private FactoryService factoryService;

    
    @GetMapping("/registrar")
    public String registrar() {
        return "factory_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String name, ModelMap modelo){
        try {
            factoryService.save(name);
            modelo.put("good", "La fabrica fue cargada correctamente.");       
        } catch (MyException ex) {    
            modelo.put("error", ex.getMessage());      
            return "factory_form.html";
        }        
        return "inicio.html";
    
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Factory> factories = factoryService.getAll();
        modelo.addAttribute("factories", factories);
        return "factory_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable UUID id, ModelMap model) {
        model.put("factory", factoryService.getById(id));
        return "factory_edit.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable UUID id, String name, ModelMap model) {
        try{
            factoryService.update(id, name);
            model.put("good", "La fabrica fue actualizada correctamente.");
            return "redirect:../lista";
        }catch (MyException ex) {
            model.put("error", ex.getMessage());
            return "factory_edit.html";
        }
        
    }
    
}
