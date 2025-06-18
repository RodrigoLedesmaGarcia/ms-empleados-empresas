package com.rodrigoledesmagarcia.com.ms_empleados.clients;

import com.rodrigoledesmagarcia.com.ms_empleados.pojo.Empresa;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-empresas", url = "http://localhost:8500/api/empresas/")
public interface EmpresaFeign {

    @GetMapping("/buscar-empresa-id/{id}")
    Empresa buscarEmpresaPorId (@PathVariable Long id);
}
