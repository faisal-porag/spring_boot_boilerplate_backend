package com.faisal.dto.v1.usersDTO;

public class UserDeleteDTO {
    private Long id;

    public UserDeleteDTO() {}

    public UserDeleteDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}