package com.example.filmorate.model;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public @NotEmpty @NotBlank String getLogin() {
        return login;
    }

    public void setLogin(@NotEmpty @NotBlank String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @PastOrPresent LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(@PastOrPresent LocalDate birthday) {
        this.birthday = birthday;
    }
}
