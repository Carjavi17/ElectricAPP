package com.egg.electric_app.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.egg.electric_app.Entities.ImageUser;
import com.egg.electric_app.Exceptions.MyException;
import com.egg.electric_app.Repository.ImageUserRepository;

@Service
public class ImageUserService {

     @Autowired
    private ImageUserRepository imageUserRepository;
    
    @Transactional
    public ImageUser save(MultipartFile archivo) throws MyException{
        if (archivo != null) {
            try {
                
                ImageUser image = new ImageUser();
                
                image.setMime(archivo.getContentType());
                
                image.setName(archivo.getName());
                
                image.setContent(archivo.getBytes());
                
                return imageUserRepository.save(image);
                
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
    
    @Transactional
    public ImageUser update(MultipartFile archivo, UUID idImage) throws MyException{
        if (archivo != null) {
            try {
                
                ImageUser image = new ImageUser();
                
                if (idImage != null) {
                    Optional<ImageUser> answer = imageUserRepository.findById(idImage);
                    
                    if (answer.isPresent()) {
                        image = answer.get();
                    }

                    image.setMime(archivo.getContentType());
                
                    image.setName(archivo.getName());
                    
                    image.setContent(archivo.getBytes());
                    
                    return imageUserRepository.save(image);
                }                

                
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
        
    }
    
    @Transactional(readOnly = true)
	public List<ImageUser> listAll() {
		return imageUserRepository.findAll();
	}
    
}
