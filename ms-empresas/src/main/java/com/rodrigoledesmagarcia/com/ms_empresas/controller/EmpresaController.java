package com.rodrigoledesmagarcia.com.ms_empresas.controller;

import com.rodrigoledesmagarcia.com.ms_empresas.entity.Empresa;
import com.rodrigoledesmagarcia.com.ms_empresas.exceptions.HandlerException;
import com.rodrigoledesmagarcia.com.ms_empresas.service.EmpresaServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaServiceImpl service;

    public EmpresaController(EmpresaServiceImpl service) {
        this.service = service;
    }

    @Operation(
            summary = "Paginar todas las empresas",
            description = "Lista todas las empresas existentes con soporte de paginación"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresas listadas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    @GetMapping("/listar-empresa")
    public Page<Empresa> listarEmpresa (Pageable pageable) {
        return service.listarEmpresas(pageable);
    }


    @Operation(
            summary = "busca una empresa por su id",
            description = "busca alguna empresa por si id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "empresa encontrada"),
            @ApiResponse(responseCode = "404", description = "empresa no encontrada")
    })
    @GetMapping("/buscar-empresa-id/{id}")
    public ResponseEntity<?> buscarEmpresaPorId (@Parameter(description = "id de la empresa a buscar") @PathVariable Long id) {
        Optional<Empresa> empresaId = service.buscarEmpresaPorId(id);
        if (empresaId.isPresent()){
            return ResponseEntity.ok(empresaId.get());
        } else {
            return HandlerException.notFound("404 not found : no se encontro al empleado con el id: "+id);
        }
    }


    @Operation (
            summary = "busca empresa por nombre",
            description = "buscar una empresa por su nombre"
    )
    @ApiResponses(value =  {
            @ApiResponse (responseCode = "200", description = "empresa encontrada"),
            @ApiResponse (responseCode = "404", description = "empresa no encontrada")
    })
    @GetMapping("/buscar-empresa-por-nombre/")
    public ResponseEntity<?> buscarEmpresaPorNombre (@Parameter(description = "buscar empresa por nombre") @RequestParam String nombre) {
        Optional<Empresa> buscarEmpresaNombre = service.buscarEmpresaPorNombre(nombre);
        if (buscarEmpresaNombre.isPresent()){
            return ResponseEntity.ok(buscarEmpresaNombre.get());
        } else {
            return HandlerException.notFound("no se encontro a ninguna empresa con el de nombre: "+nombre);
        }
    }

    @Operation(summary = "Crear nueva empresa", description = "Crea una nueva empresa en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empresa creada exitosamente", content = @Content(schema = @Schema(implementation = Empresa.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (validación o lógica de negocio)", content = @Content(schema = @Schema(example = "{ \"nombre\": \"el campo nombre\" }")))
    })
    @PostMapping("/crear-empresa")
    public ResponseEntity<?> crearEmpresa (@Valid @RequestBody Empresa empresa, BindingResult result) {
        if(result.hasErrors()){
            Map<String, Object> errors = new HashMap<>();
            result.getFieldErrors().forEach( e -> {
                errors.put(e.getField(), " el campo "+e.getField());
            });
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Empresa newEmpresa = service.crearEmpresa(empresa);
            return ResponseEntity.status(HttpStatus.CREATED).body(newEmpresa);
        } catch (IllegalArgumentException e){
            return HandlerException.badRequest("error 400 : no se pudo crear a la empresa");
        }
    }
    @Operation(
            summary = "Editar empresa por ID",
            description = "Edita los datos de una empresa existente, como nombre e industria"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empresa editada correctamente",
                    content = @Content(schema = @Schema(implementation = Empresa.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación o negocio",
                    content = @Content(schema = @Schema(example = "{ \"nombre\": \"el campo nombre es obligatorio\" }"))),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @PutMapping("/editar-empresa/{id}")
    public ResponseEntity<?> editarEmpresa (@Valid @RequestBody Empresa empresa, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()){
            Map<String, Object> errors = new HashMap<>();
            result.getFieldErrors().forEach( e -> {
                errors.put(e.getField(), "el campo "+e.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Optional<Empresa> empresaId = service.buscarEmpresaPorId(id);
            if (empresaId.isPresent()){
                Empresa empresaDb = empresaId.get();
                empresaDb.setNombre(empresa.getNombre());
                empresaDb.setIndustria(empresa.getIndustria());

                Empresa empresaNew = service.crearEmpresa(empresaDb);
                return ResponseEntity.status(HttpStatus.CREATED).body(empresaNew);
            } else {
                return HandlerException.badRequest("error 404 : bad request");
            }
        } catch (IllegalArgumentException e){
            return HandlerException.badRequest("error 400 : no se puedo editar la empresa");
        }

    }


    @Operation(
            summary = "Eliminar empresa por ID",
            description = "Elimina una empresa registrada usando su identificador único"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa eliminada exitosamente",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Se eliminó con éxito la empresa con id: 1\" }"))),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarEmpresa (@PathVariable Long id) {
        Optional<Empresa> empresaId = service.buscarEmpresaPorId(id);
        try {
            if (empresaId.isPresent()){
                service.eliminarEmpresa(id);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                        "message","se elimino con exito al empleado con id: "+id
                ));
            } else {
                return HandlerException.badRequest("error 404 not found : no se pudo eliminar a la empresa");
            }
        } catch (IllegalArgumentException e){
            return HandlerException.internalServerError("ocurrio un error");
        }
    }




} // clase controller
