package com.rodrigoledesmagarcia.com.ms_empleados.repository;

import com.rodrigoledesmagarcia.com.ms_empleados.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository  extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findEmpleadoByNombreIgnoreCase(String nombre);
}
