package com.restapiblog.restapiblog.Dto;

import com.restapiblog.restapiblog.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserDto {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Role userRole;
}
