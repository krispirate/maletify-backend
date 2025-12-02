package com.maletify.gestorempaque.api.domain.model;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter 
@AllArgsConstructor 
@NoArgsConstructor
public class ClimaRequest {

    private double latitude;
    private double longitude;

    private String destino;
    private LocalDate fecha;
}