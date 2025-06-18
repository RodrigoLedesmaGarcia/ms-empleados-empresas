package com.rodrigoledesmagarcia.com.ms_empleados.service;

import com.rodrigoledesmagarcia.com.ms_empleados.entity.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmpleadoService {

    Page<Empleado> listarEmpleados(Pageable pageable);

    Optional<Empleado> buscarPorId (Long id);

    Optional<Empleado> buscarPorNombre (String nombre);

    Empleado crearEmpleado (Empleado empleado);

    void eliminarEmpleado (Long id);



}
