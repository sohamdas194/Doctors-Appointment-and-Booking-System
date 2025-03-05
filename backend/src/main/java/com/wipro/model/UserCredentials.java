package com.wipro.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCredentials {
    private String userEmail;
    private String userPassword;

    public UserCredentials() {
    }

}
