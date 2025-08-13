
package com.digis.EDesalesTareaVistaAgosto11.ML;


import java.util.Date;

public class Tarea {
    int id;
    String titulo;
    Date fechaVencimiento;
    EstadoTarea idEstadoTarea;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public EstadoTarea getIdEstadoTarea() {
        return idEstadoTarea;
    }

    public void setIdEstadoTarea(EstadoTarea idEstadoTarea) {
        this.idEstadoTarea = idEstadoTarea;
    }
    
    
            
    
}
