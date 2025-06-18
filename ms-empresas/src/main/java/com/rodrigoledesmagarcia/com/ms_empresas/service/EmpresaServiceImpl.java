package com.rodrigoledesmagarcia.com.ms_empresas.service;

import com.rodrigoledesmagarcia.com.ms_empresas.entity.Empresa;
import com.rodrigoledesmagarcia.com.ms_empresas.repository.EmpresaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpresaServiceImpl implements EmpresaService{

    private final EmpresaRepository repository;

    public EmpresaServiceImpl(EmpresaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Empresa> listarEmpresas(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Empresa> buscarEmpresaPorNombre(String nombre) {
        return repository.findEmpresaByNombreIgnoreCase(nombre);
    }

    @Override
    public Optional<Empresa> buscarEmpresaPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Empresa crearEmpresa(Empresa empresa) {
        return repository.save(empresa);
    }

    @Override
    public void eliminarEmpresa(Long id) {
        repository.deleteById(id);
    }
}
