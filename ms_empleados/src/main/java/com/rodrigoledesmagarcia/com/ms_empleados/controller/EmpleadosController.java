package com.rodrigoledesmagarcia.com.ms_empleados.controller;

import com.rodrigoledesmagarcia.com.ms_empleados.entity.Empleado;
import com.rodrigoledesmagarcia.com.ms_empleados.exception.HandlerApiException;
import com.rodrigoledesmagarcia.com.ms_empleados.service.EmpleadoService;
import com.rodrigoledesmagarcia.com.ms_empleados.service.EmpleadoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadosController {

    private final EmpleadoServiceImpl service;
    private final EmpleadoService empleadoService;

    public EmpleadosController(EmpleadoServiceImpl service, EmpleadoService empleadoService) {
        this.service = service;
        this.empleadoService = empleadoService;
    }


    @GetMapping("/listar-empleados")
    public Page<Empleado> listarEmpleados (Pageable pageable){
        return service.listarEmpleados(pageable);
    }


    @GetMapping("/buscar-por-id/{id}")
    public ResponseEntity<?> buscarEmpleadoPorId (@PathVariable Long id) {
        Optional<Empleado> empleadoId = service.buscarPorId(id);
        if (empleadoId.isPresent()){
            return ResponseEntity.ok(empleadoId.get());
        } else {
            return HandlerApiException.notFound("404 not found : empleado no econtrado");
        }
    }

    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<?> buscarEmpleadoPorNombre(@RequestParam String nombre){
        Optional<?> empleadoNombre = service.buscarPorNombre(nombre);
        if (empleadoNombre.isPresent()){
            return ResponseEntity.ok(empleadoNombre.get());
        } else {
            return HandlerApiException.notFound("404 not found");
        }
    }

    @GetMapping("/relacion-laboral")
    public ResponseEntity<?> relacionLaboral (@PathVariable Long id){
        Object relacion = service.empleadoEmpresaRelacion(id);

        if (relacion == null) {
           return HandlerApiException.notFound("relacion labora no existente");
        }

        return ResponseEntity.ok(relacion);
    }

    @PostMapping("/crear-empleado")
    public ResponseEntity<?> crearEmpleado (@Valid @RequestBody Empleado empleado, BindingResult result){
        if (result.hasErrors()){
            Map<String, Object> errors = new HashMap<>();
            result.getFieldErrors().forEach( e -> {
                errors.put(e.getField(),"el campo "+e.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            service.crearEmpleado(empleado);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "201 created"));
        } catch (IllegalArgumentException e){
            return HandlerApiException.badRequest("400 bad request");
        }
    }

    @PutMapping("/editar-empleado/{id}")
    public ResponseEntity<?> crearEmpleado (@Valid @RequestBody Empleado empleado, BindingResult result, @PathVariable Long id){
        if (result.hasErrors()){
            Map<String, Object> errors = new HashMap<>();
            result.getFieldErrors().forEach( e -> {
                errors.put(e.getField(), "el campo "+e.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Optional<Empleado> empleadoId = service.buscarPorId(id);
            if (empleadoId.isPresent()){
                Empleado newEmpledo =empleadoId.get();
                newEmpledo.setNombre(empleado.getNombre());
                newEmpledo.setPuesto(empleado.getPuesto());

                return ResponseEntity.ok(newEmpledo);
            } else {
                return HandlerApiException.notFound("404 not found");
            }
        } catch (IllegalArgumentException e){
            return HandlerApiException.badRequest("400 bad request");
        }
    }

    @DeleteMapping("/eliminar-empleado")
    public ResponseEntity<?> eliminarEmpleado (@PathVariable Long id) {
        Optional<Empleado> empleadoId = service.buscarPorId(id);
        if (empleadoId.isPresent()){
            service.eliminarEmpleado(id);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "empleado eliminado"));
        } else {
            return HandlerApiException.notFound("empleado no encontrado");
        }
    }
} // clase controller
