package com.example.filmorate.model;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private Integer id;
    @Email
    private String email;
    @NotEmpty
    @NotBlank
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;

}
