package com.vn.beta_testing.auth_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.s;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vn.beta_testing.auth_service.repository.UserRepository;
import com.vn.beta_testing.domain.Clinic;
import com.vn.beta_testing.domain.Role;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.domain.response.user.ResCreateUserDTO;
import com.vn.beta_testing.domain.response.user.ResUpdateUserDTO;
import com.vn.beta_testing.domain.response.user.ResUserDTO;

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

    public User handleUpdateUser(User reqUser) {
        System.out.println("Update user: " + reqUser);
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setAddress(reqUser.getAddress());
            currentUser.setGender(reqUser.getGender());
            currentUser.setAge(reqUser.getAge());
            currentUser.setName(reqUser.getName());
            currentUser.setPhoneNumber(reqUser.getPhoneNumber());
        }
        // // check company
        // if (reqUser.getClinic() != null) {
        // Optional<Clinic> clinicOptional =
        // this.clinicService.findById(reqUser.getClinic().getId());
        // currentUser.setClinic(clinicOptional.isPresent() ? clinicOptional.get() :
        // null);
        // }

        // check roles
        if (reqUser.getRole() != null) {
            Role r = this.roleService.fetchById(reqUser.getRole().getId());
            currentUser.setRole(r != null ? r : null);
        }

        currentUser = this.userRepository.save(currentUser);

        return currentUser;
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

    public ResultPaginationDTO fetchAllUser(Specification<User> specification, Pageable pageable) {

        Page<User> pageUser = this.userRepository.findAll(specification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        rs.setMeta(meta);

        // remove sensitive data

        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> this.convertToResUserDTO(item))
                .collect(Collectors.toList());

        rs.setResult(listUser);

        return rs;
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

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.Clinic com = new ResUserDTO.Clinic();
        ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();

        if (user.getClinic() != null) {
            com.setId(user.getClinic().getId());
            com.setName(user.getClinic().getClinicName());
            res.setClinic(com);
        }

        if (user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            res.setRole(roleUser);
        }

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        ResUpdateUserDTO.Clinic com = new ResUpdateUserDTO.Clinic();

        if (user.getClinic() != null) {
            com.setId(user.getClinic().getId());
            com.setName(user.getClinic().getClinicName());
            res.setClinic(com);
        }
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());

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
