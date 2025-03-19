package com.egg.electric_app.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.egg.electric_app.Entities.ImageItem;
import com.egg.electric_app.Exceptions.MyException;
import com.egg.electric_app.Repository.ImageItemRepository;

@Service
public class ImageItemService {

    @Autowired
    private ImageItemRepository imageItemRepository;
    
    @Transactional
    public ImageItem save(MultipartFile archivo) throws MyException{
        if (archivo != null) {
            try {
                
                ImageItem image = new ImageItem();
                
                image.setMime(archivo.getContentType());
                
                image.setName(archivo.getName());
                
                image.setContent(archivo.getBytes());
                
                return imageItemRepository.save(image);
                
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
    
    @Transactional
    public ImageItem update(MultipartFile archivo, UUID idImage) throws MyException{
        if (archivo != null) {
            try {
                
                ImageItem image = new ImageItem();
                
                if (idImage != null) {
                    Optional<ImageItem> answer = imageItemRepository.findById(idImage);
                    
                    if (answer.isPresent()) {
                        image = answer.get();
                    }

                    image.setMime(archivo.getContentType());
                
                    image.setName(archivo.getName());
                    
                    image.setContent(archivo.getBytes());
                    
                    return imageItemRepository.save(image);
                }                

                
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
        
    }
    
    @Transactional(readOnly = true)
	public List<ImageItem> listAll() {
		return imageItemRepository.findAll();
	}
    
}
