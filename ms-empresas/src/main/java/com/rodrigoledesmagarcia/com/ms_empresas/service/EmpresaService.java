package com.rodrigoledesmagarcia.com.ms_empresas.service;

import com.rodrigoledesmagarcia.com.ms_empresas.entity.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;

public interface EmpresaService  {

    Page<Empresa> listarEmpresas(Pageable pageable);

    Optional<Empresa> buscarEmpresaPorNombre(String nombre);

    Optional<Empresa> buscarEmpresaPorId(Long id);

    Empresa crearEmpresa(Empresa empresa);

    void eliminarEmpresa(Long id);
}
