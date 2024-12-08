package com.example.filmorate.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Integer id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive
    private String duration;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotBlank(message = "Название фильма не может быть пустым") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Название фильма не может быть пустым") String name) {
        this.name = name;
    }

    public @Size(max = 200, message = "Максимальная длина описания — 200 символов") String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 200, message = "Максимальная длина описания — 200 символов") String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public @Positive String getDuration() {
        return duration;
    }

    public void setDuration(@Positive String duration) {
        this.duration = duration;
    }
}


