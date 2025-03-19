package com.egg.electric_app.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.electric_app.Entities.Factory;
import com.egg.electric_app.Exceptions.MyException;
import com.egg.electric_app.Repository.FactoryRepository;



@Service
public class FactoryService {

    @Autowired
    private FactoryRepository factoryRepository;

    @Transactional
    public Factory save(String name) throws MyException {

        check(name);

        Factory factory = new Factory();

        return factoryRepository.save(factory);
    }

    @Transactional
    public Factory update(UUID id, String name) throws MyException {

        check(name);

        Optional<Factory> answer = factoryRepository.findById(id);   

        if (answer.isEmpty()) {
            throw new MyException("Factory not found.");            
        }

        Factory factory = answer.get();
        factory.setName(name);

        return factoryRepository.save(factory);
    }

    @Transactional(readOnly = true)
    public List<Factory> getAll() {
        return factoryRepository.findAll();
    }   

    @Transactional(readOnly = true)
    public Factory getById(UUID id) {
        return factoryRepository.findById(id).get();
    }


    public void check(String name) throws MyException {
        if(name == null || name.trim().isEmpty()) {
            throw new MyException("Name cannot be null or empty. Please try again.");
        }
    }
    
}
