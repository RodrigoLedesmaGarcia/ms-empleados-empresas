package com.rodrigoledesmagarcia.com.ms_empresas.repository;

import com.rodrigoledesmagarcia.com.ms_empresas.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findEmpresaByNombreIgnoreCase(String nombre);
}
