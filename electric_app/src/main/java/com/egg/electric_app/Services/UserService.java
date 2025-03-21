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
    public void update(MultipartFile archivo, UUID idUser, String email, String name, String lastName, String password)
            throws MyException {

        checkEdit(email, name, lastName);

        Optional<UserElectric> answer = userRepository.findById(idUser);

        if (answer.isEmpty()) {
            throw new MyException("Usuario no encontrado.");
        }

        UserElectric user = answer.get();

        user.setEmail(email);
        user.setName(name);
        user.setLastName(lastName);


        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(new BCryptPasswordEncoder().encode(password));
        }

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
    public UserElectric getById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public void changeRol(UUID id) {

        Optional<UserElectric> answer = userRepository.findById(id);

        if (answer.isPresent()) {
            UserElectric user = answer.get();
            if (user.getRol().equals(Rol.USER)) {
                user.setRol(Rol.ADMIN);
            } else if (user.getRol().equals(Rol.ADMIN)) {
                user.setRol(Rol.USER);
            }

        }
    }

    public void check(String email, String name, String lastName, String password) throws MyException {
        if (name == null || name.trim().isEmpty()) {
            throw new MyException("El nombre no puede estar vacío.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new MyException("El apellido no puede estar vacío.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new MyException("El email no puede estar vacío.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new MyException("La contraseña no puede estar vacía.");
        }
    }

    public void checkEdit(String email, String name, String lastName) throws MyException {
        if (name == null || name.trim().isEmpty()) {
            throw new MyException("El nombre no puede estar vacío.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new MyException("El apellido no puede estar vacío.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new MyException("El email no puede estar vacío.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserElectric user = userRepository.findByEmail(email);

        if (user != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + user.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usersession", user);

            return new User(user.getEmail(), user.getPassword(), permisos);
        } else {
            return null;
        }
    }

}
