package com.vn.petcare.auth_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.s;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vn.petcare.auth_service.repository.UserRepository;
import com.vn.petcare.domain.Role;
import com.vn.petcare.domain.User;
import com.vn.petcare.domain.response.user.ResCreateUserDTO;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public User handleCreateUser(User user) {
        // Check spa
        // if (user.getCompany() != null) {
        // Optional<Company> companyOptional =
        // this.companyService.findById(user.getCompany().getId());
        // user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
        // }
        // check role
        if (user.getRole() != null) {
            Role r = this.roleService.fetchById(user.getRole().getId());
            user.setRole(r != null ? r : null);
        }

        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

      public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        ResCreateUserDTO.Clinic com = new ResCreateUserDTO.Clinic();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        if (user.getClinic() != null) {
            com.setId(user.getClinic().getId());
            com.setName(user.getClinic().getClinicName());
            res.setClinic(com);
        }

        return res;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);

        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

}
