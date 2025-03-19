package com.egg.electric_app.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.egg.electric_app.Entities.ImageUser;
import com.egg.electric_app.Entities.UserElectric;
import com.egg.electric_app.Enums.Rol;
import com.egg.electric_app.Exceptions.MyException;
import com.egg.electric_app.Repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageUserService imageUserService;

    @Transactional
    public void save(MultipartFile archivo, String email, String name, String lastName, String password)
            throws MyException {

        check(email, name, lastName, password);

        UserElectric user = new UserElectric();
        user.setEmail(email);
        user.setName(name);
        user.setLastName(lastName);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRol(Rol.USER);

        if (archivo != null && !archivo.isEmpty()) {
            user.setImage(imageUserService.save(archivo));
        }        

        userRepository.save(user);

    }

    @Transactional
    public void updateImage(MultipartFile archivo, UUID idUser, String email, String name, String lastName, String password ) throws MyException {
        
        check(email, name, lastName, password);

        Optional<UserElectric> answer = userRepository.findById(idUser);

        if (answer.isEmpty()) {
            throw new MyException("User not found.");
        }

        UserElectric user = answer.get();

        user.setEmail(email);
        user.setName(name);
        user.setLastName(lastName);
        user.setPassword(new BCryptPasswordEncoder().encode(password));

        if (archivo != null && !archivo.isEmpty()) {
            String idImage = user.getImage() != null ? user.getImage().getId().toString() : null;
            ImageUser image = imageUserService.update(archivo, idImage != null ? UUID.fromString(idImage) : null);
            user.setImage(image);
        }
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserElectric> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UserElectric getByEmail(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public void changeRol(UUID id){

        Optional<UserElectric> answer = userRepository.findById(id);

        if(answer.isPresent()){
            UserElectric user = answer.get();
            if(user.getRol().equals(Rol.USER)){
                user.setRol(Rol.ADMIN);
            }else if (user.getRol().equals(Rol.ADMIN)){
                user.setRol(Rol.USER);
            }
            
        }
    }

    public void check(String email, String name, String lastName, String password) throws MyException {
        if (name == null || name.trim().isEmpty()) {
            throw new MyException("Name cannot be null or empty. Please try again.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new MyException("Last name cannot be null or empty. Please try again.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new MyException("Email cannot be null or empty. Please try again.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new MyException("Password cannot be null or empty. Please try again.");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserElectric user = userRepository.findByEmail(email);

        if (user != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();
            
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+ user.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usersession", user);

            return new User(user.getEmail(), user.getPassword(), permisos);
        }else{
            return null;
        }
    }

}
