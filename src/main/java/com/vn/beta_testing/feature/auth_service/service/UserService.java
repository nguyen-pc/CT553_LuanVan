package com.vn.beta_testing.feature.auth_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.s;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.CompanyProfile;
import com.vn.beta_testing.domain.Role;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.domain.response.user.ResCreateUserDTO;
import com.vn.beta_testing.domain.response.user.ResUpdateUserDTO;
import com.vn.beta_testing.domain.response.user.ResUserDTO;
import com.vn.beta_testing.feature.auth_service.repository.UserRepository;
import com.vn.beta_testing.feature.company_service.service.CompanyService;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, CompanyService companyService, RoleService roleService,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyService = companyService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public User handleCreateUser(User user) {
        // Check spa
        if (user.getCompanyProfile() != null) {
            CompanyProfile companyOptional = this.companyService.fetchCompanyById(user.getCompanyProfile().getId());
            user.setCompanyProfile(companyOptional != null ? companyOptional : null);
        }
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
            // currentUser.setAge(reqUser.getAge());
            currentUser.setName(reqUser.getName());
            currentUser.setPhoneNumber(reqUser.getPhoneNumber());
        }
        // // check company
        if (reqUser.getCompanyProfile() != null) {
            CompanyProfile companyOptional = this.companyService.fetchCompanyById(reqUser.getCompanyProfile().getId());
            currentUser.setCompanyProfile(companyOptional != null ? companyOptional : null);
        }

        // check roles
        if (reqUser.getRole() != null) {
            Role r = this.roleService.fetchById(reqUser.getRole().getId());
            currentUser.setRole(r != null ? r : null);
        }

        currentUser = this.userRepository.save(currentUser);

        return currentUser;
    }

    public User handleUpdateUserNotNull(User reqUser) {
        System.out.println("Update user: " + reqUser);
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser == null) {
            throw new RuntimeException("User not found with id = " + reqUser.getId());
        }

        // 🧩 Cập nhật chỉ khi có dữ liệu truyền vào (không null)
        if (reqUser.getName() != null) {
            currentUser.setName(reqUser.getName());
        }
        if (reqUser.getPhoneNumber() != null) {
            currentUser.setPhoneNumber(reqUser.getPhoneNumber());
        }
        if (reqUser.getAddress() != null) {
            currentUser.setAddress(reqUser.getAddress());
        }
        if (reqUser.getGender() != null) {
            currentUser.setGender(reqUser.getGender());
        }

        // 🏢 Cập nhật company nếu có ID
        if (reqUser.getCompanyProfile() != null && reqUser.getCompanyProfile().getId() != 0) {
            CompanyProfile company = this.companyService.fetchCompanyById(reqUser.getCompanyProfile().getId());
            if (company != null) {
                currentUser.setCompanyProfile(company);
            }
        }

        // 👮 Cập nhật role nếu có ID
        if (reqUser.getRole() != null && reqUser.getRole().getId() != 0) {
            Role role = this.roleService.fetchById(reqUser.getRole().getId());
            if (role != null) {
                currentUser.setRole(role);
            }
        }

        // 💾 Lưu lại
        currentUser = this.userRepository.save(currentUser);
        return currentUser;
    }

    public boolean handleChangePassword(Long userId, String oldPassword, String newPassword) {
        User currentUser = fetchUserById(userId);
        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        // ✅ So sánh mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // ✅ Mã hóa mật khẩu mới và lưu lại
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
        return true;
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
        ResCreateUserDTO.CompanyProfile com = new ResCreateUserDTO.CompanyProfile();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        // res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        // res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        if (user.getCompanyProfile() != null) {
            com.setId(user.getCompanyProfile().getId());
            com.setName(user.getCompanyProfile().getCompanyName());
            res.setCompanyProfile(com);
        }

        return res;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.CompanyProfile com = new ResUserDTO.CompanyProfile();
        ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();

        if (user.getCompanyProfile() != null) {
            com.setId(user.getCompanyProfile().getId());
            com.setName(user.getCompanyProfile().getCompanyName());
            res.setCompanyProfile(com);
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
        res.setPhoneNumber(user.getPhoneNumber());
        // res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }

    public List<ResUserDTO> fetchUsersByCompanyId(Long companyId) {
        List<User> users = userRepository.findByCompanyProfile_Id(companyId);

        // ✅ Map từng user sang DTO
        return users.stream()
                .map(this::convertToResUserDTO)
                .toList(); // dùng toList() (Java 16+) hoặc Collectors.toList()
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyProfile com = new ResUpdateUserDTO.CompanyProfile();

        if (user.getCompanyProfile() != null) {
            com.setId(user.getCompanyProfile().getId());
            com.setName(user.getCompanyProfile().getCompanyName());
            res.setCompanyProfile(com);
        }
        res.setId(user.getId());
        res.setName(user.getName());
        // res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        // res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        return res;

    }

    public List<ResUserDTO> fetchTop10NewestUsers() {
        List<User> newestUsers = userRepository.findTop10NewestUsers();

        return newestUsers.stream()
                .map(this::convertToResUserDTO)
                .collect(Collectors.toList());
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
