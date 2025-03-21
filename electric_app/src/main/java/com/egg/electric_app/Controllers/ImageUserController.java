package com.egg.electric_app.Controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.egg.electric_app.Entities.UserElectric;
import com.egg.electric_app.Services.UserService;

@Controller
@RequestMapping("/imagen")
public class ImageUserController {
     @Autowired
    UserService userService;

    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> UserImage(@PathVariable UUID id) {
        UserElectric user = userService.getById(id);
        
        byte[] image = user.getImage().getContent();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_JPEG);


        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    
}
