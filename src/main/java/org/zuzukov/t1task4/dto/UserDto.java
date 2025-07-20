package org.zuzukov.t1task4.dto;


import lombok.Data;

@Data
public class UserDto {
    String userId;
    String firstName;
    String lastName;
    String email;
    String password;
}
