package ru.checkdev.mock.dto;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitterDTO {

    private int id;

    @Column(name = "name")
    private String name;
}
