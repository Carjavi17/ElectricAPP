package com.egg.electric_app.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.electric_app.Entities.Factory;
import com.egg.electric_app.Entities.ImageItem;
import com.egg.electric_app.Entities.Item;
import com.egg.electric_app.Exceptions.MyException;
import com.egg.electric_app.Repository.FactoryRepository;
import com.egg.electric_app.Repository.ImageItemRepository;
import com.egg.electric_app.Repository.ItemRepository;



@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FactoryRepository factoryRepository;

    @Autowired
    private ImageItemRepository imageItemRepository;

    @Transactional
    public Item save(String name, String description, double price, int stock,  UUID idFactory, UUID idImage) throws MyException {

        check(name, description, price, stock, idFactory, idImage);

        Factory factory = factoryRepository.findById(idFactory).get();
        ImageItem imageItem = imageItemRepository.findById(idImage).get();

        if (factory == null || imageItem == null) {
            throw new MyException("Factory or image not found.");
        }


        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        item.setStock(stock);
        item.setImage(imageItem);
        item.setFactory(factory);

        return itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Item getById(UUID id) {
        return itemRepository.findById(id).get();
    }

    @Transactional
    public Item update(UUID id, String name, String description, double price, int stock, UUID idFactory, UUID idImage) throws MyException {

        check(name, description, price, stock, idFactory, idImage);

        Optional<Item> answer = itemRepository.findById(id);
        Optional<Factory> answerfactory = factoryRepository.findById(idFactory);
        Optional<ImageItem> answerimage = imageItemRepository.findById(idImage);

        if (answer.isEmpty() || answerfactory.isEmpty() || answerimage.isEmpty()) {
            throw new MyException("Item, factory or image not found.");        
        }

        Item item = answer.get();
        item.setName(name); 
        item.setDescription(description);
        item.setPrice(price);
        item.setStock(stock);        
        item.setImage(answerimage.get());
        item.setFactory(answerfactory.get());

        return itemRepository.save(item);
    }


    private void check(String name, String description, double price, int stock, UUID idFactory, UUID idImage) throws MyException {
        if(name == null || name.trim().isEmpty()) {
            throw new MyException("Name cannot be null or empty. Please try again.");
        }
        if(description == null || description.trim().isEmpty()){
            throw new MyException("Description cannot be null or empty. Please try again.");
        }
        if(price <= 0){
            throw new MyException("Price cannot be negative or zero. Please try again.");
        }
        if(stock < 0){
            throw new MyException("Stock cannot be negative. Please try again.");
        }
        if(idFactory == null){
            throw new MyException("Factory cannot be null. Please try again.");
        }
        if(idImage == null){
            throw new MyException("Image cannot be null. Please try again.");          
        }

    }
}
    

