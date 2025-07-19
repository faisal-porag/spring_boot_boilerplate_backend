package com.faisal.controller.v1;

import com.faisal.dto.v1.*;
import com.faisal.dto.v1.usersDTO.UserDTO;
import com.faisal.dto.v1.usersDTO.UserDeleteDTO;
import com.faisal.model.v1.User;
import com.faisal.repository.v1.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Create user - single object response
    @PostMapping
    public ResponseEntity<CustomResponse<UserDTO>> createUser(@Valid @RequestBody UserDTO userDto) {
        if (userRepository.existsByName(userDto.getName())) {
            return ResponseEntity.badRequest().body(
                    new CustomResponse<>(
                            "REQUEST_FAILED",
                            false,
                            "Name must be unique.",
                            null
                    )
            );
        }

        User user = new User();
        user.setName(userDto.getName());
        user = userRepository.save(user);

        CustomResponse<UserDTO> response = new CustomResponse<>(
                "REQUEST_SUCCESS_200",
                true,
                "User created successfully.",
                new UserDTO(user.getId(), user.getName())
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<UserDTO>> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDTO userDTO = new UserDTO(user.getId(), user.getName());

            CustomResponse<UserDTO> response = new CustomResponse<>(
                    "REQUEST_SUCCESS_200",
                    true,
                    "User retrieved successfully.",
                    userDTO
            );
            return ResponseEntity.ok(response);
        } else {
            CustomResponse<UserDTO> response = new CustomResponse<>(
                    "REQUEST_NOT_FOUND",
                    false,
                    "User not found.",
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    // Get users - paged list response
    @GetMapping
    public ResponseEntity<CustomResponse<PagedData<UserDTO>>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserDTO> userDTOs = userPage.getContent().stream()
                .map(user -> new UserDTO(user.getId(), user.getName()))
                .collect(Collectors.toList());

        PageInfo pageInfo = new PageInfo(
                userPage.getTotalElements(),
                size,
                page,
                userPage.getTotalPages(),
                userPage.hasNext()
        );

        PagedData<UserDTO> pagedData = new PagedData<>(pageInfo, userDTOs);

        CustomResponse<PagedData<UserDTO>> response = new CustomResponse<>(
                "REQUEST_SUCCESS_200",
                true,
                "Your request is successful.",
                pagedData
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<UserDTO>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDto) {

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            CustomResponse<UserDTO> response = new CustomResponse<>(
                    "REQUEST_NOT_FOUND",
                    false,
                    "User not found.",
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Check if the updated name is unique (excluding current user)
        boolean nameExists = userRepository.existsByName(userDto.getName());
        if (nameExists && !optionalUser.get().getName().equals(userDto.getName())) {
            CustomResponse<UserDTO> response = new CustomResponse<>(
                    "REQUEST_FAILED",
                    false,
                    "Name must be unique.",
                    null
            );
            return ResponseEntity.badRequest().body(response);
        }

        User user = optionalUser.get();
        user.setName(userDto.getName());
        user = userRepository.save(user);

        UserDTO updatedUserDTO = new UserDTO(user.getId(), user.getName());
        CustomResponse<UserDTO> response = new CustomResponse<>(
                "REQUEST_SUCCESS_200",
                true,
                "User updated successfully.",
                updatedUserDTO
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete-user")
    public ResponseEntity<CustomResponse<Object>> deleteUserByPost(@RequestBody UserDeleteDTO deleteRequest) {
        Long id = deleteRequest.getId();

        if (id == null) {
            CustomResponse<Object> response = new CustomResponse<>(
                    "REQUEST_FAILED",
                    false,
                    "User ID must be provided.",
                    null
            );
            return ResponseEntity.badRequest().body(response);
        }

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            CustomResponse<Object> response = new CustomResponse<>(
                    "REQUEST_NOT_FOUND",
                    false,
                    "User not found.",
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        userRepository.deleteById(id);

        CustomResponse<Object> response = new CustomResponse<>(
                "REQUEST_SUCCESS_200",
                true,
                "User deleted successfully.",
                null
        );
        return ResponseEntity.ok(response);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<CustomResponse<Object>> deleteUser(@PathVariable Long id) {
//        Optional<User> optionalUser = userRepository.findById(id);
//        if (optionalUser.isEmpty()) {
//            CustomResponse<Object> response = new CustomResponse<>(
//                    "REQUEST_NOT_FOUND",
//                    false,
//                    "User not found.",
//                    null
//            );
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//
//        userRepository.deleteById(id);
//
//        CustomResponse<Object> response = new CustomResponse<>(
//                "REQUEST_SUCCESS_200",
//                true,
//                "User deleted successfully.",
//                null
//        );
//        return ResponseEntity.ok(response);
//    }

    // ====================================================================================================
    // ====================================================================================================
    // ====================================== RAW QUERY ===================================================
    // ====================================================================================================
    // ====================================================================================================

    @GetMapping("/get-all-users")
    public ResponseEntity<CustomResponse<PagedData<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        // Pageable: page is zero-based index in Spring Data
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<User> userPage = userRepository.findUsersByRawQuery(pageable);

        List<UserDTO> userDTOs = userPage.getContent().stream()
                .map(user -> new UserDTO(user.getId(), user.getName()))
                .collect(Collectors.toList());

        PageInfo pageInfo = new PageInfo(
                userPage.getTotalElements(),
                size,
                page,
                userPage.getTotalPages(),
                userPage.hasNext()
        );

        PagedData<UserDTO> pagedData = new PagedData<>(pageInfo, userDTOs);

        CustomResponse<PagedData<UserDTO>> response = new CustomResponse<>(
                "REQUEST_SUCCESS_200",
                true,
                "Your request is successful.",
                pagedData
        );

        return ResponseEntity.ok(response);
    }
}