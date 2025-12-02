package com.maletify.gestorempaque.api.domain.model;

import lombok.Getter;      
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter 
@AllArgsConstructor
@NoArgsConstructor
public class Clima {


private double temperaturaMedia;
private String condiciones;  
private double humedadMax;  

 public String obtenerClasificacion() {
 if (temperaturaMedia < 10) return "FRIO_EXTREMO";
 if (temperaturaMedia < 20) return "TEMPLADO";
 if (temperaturaMedia >= 20) return "CALIDO";
 return "DESCONOCIDO";
 }
}


