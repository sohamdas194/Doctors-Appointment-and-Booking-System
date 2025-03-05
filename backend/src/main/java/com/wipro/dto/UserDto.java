package com.wipro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserDto {

    private String email;
    private String password;
    private String role;

    public UserDto() {
    }

    public UserDto(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
