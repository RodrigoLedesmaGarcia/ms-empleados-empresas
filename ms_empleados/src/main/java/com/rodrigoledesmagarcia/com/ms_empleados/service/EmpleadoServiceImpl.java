package com.rodrigoledesmagarcia.com.ms_empleados.service;

import com.rodrigoledesmagarcia.com.ms_empleados.clients.EmpresaFeign;
import com.rodrigoledesmagarcia.com.ms_empleados.entity.Empleado;
import com.rodrigoledesmagarcia.com.ms_empleados.pojo.Empresa;
import com.rodrigoledesmagarcia.com.ms_empleados.repository.EmpleadoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository repository;

    private final EmpresaFeign feign;

    public EmpleadoServiceImpl(EmpleadoRepository repository, EmpresaFeign feign) {
        this.repository = repository;
        this.feign = feign;
    }

    @Override
    public Page<Empleado> listarEmpleados(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Empleado> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Empleado> buscarPorNombre(String nombre) {
        return repository.findEmpleadoByNombreIgnoreCase(nombre);
    }

    @Override
    public Empleado crearEmpleado(Empleado empleado) {
        return repository.save(empleado);
    }

    @Override
    public void eliminarEmpleado(Long id) {
        repository.deleteById(id);
    }

    public Map<String, Object> empleadoEmpresaRelacion (Long id) {
        Optional<Empleado> empleadoEmpresa = repository.findById(id);
        if (empleadoEmpresa.isEmpty()){ return Map.of("error","no econtrado");
        }
        Empleado empleado = empleadoEmpresa.get();
        Empresa empresa = feign.buscarEmpresaPorId(empleado.getEmpresaId());
        return Map.of("empleado: ",empleado," empresa: ",empresa);
    }
}
