package com.diedari.jimdur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// NO QUIERO MODIFICARLOS POR ESO SOLO GETTERS
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmailDTO {
    private String[] toUser;
    private String subject;
    private String message;
}
