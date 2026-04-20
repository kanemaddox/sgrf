package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.CuentaBancariaDtoRequest;
import com.saims.sgrf.dto.CuentaBancariaDtoResponse;
import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.BancoModel;
import com.saims.sgrf.model.CuentaBancariaModel;
import com.saims.sgrf.model.PersonaModel;

/**
 * 📌 Interface: CuentaBancariaService
 * 
 * 🧾 Descripción:
 * Define los métodos para la gestión de cuentas bancarias.
 * Permite crear, consultar y obtener cuentas asociadas a personas y bancos.
 */
public interface CuentaBancariaService {

    /**
     * 🔹 Crea una cuenta bancaria o la obtiene si ya existe.
     * 
     * @param personaModel Persona asociada a la cuenta
     * @param bancoModel Banco asociado a la cuenta
     * @param datosDtoRequest Datos adicionales de la cuenta
     * @return CuentaBancariaModel entidad persistida o existente
     */
    CuentaBancariaModel createOrGet(PersonaModel personaModel,
                                    BancoModel bancoModel,
                                    DatosDtoRequest datosDtoRequest);

    /**
     * 🔹 Crea una cuenta bancaria a partir de un DTO.
     * 
     * @param request datos de la cuenta bancaria
     * @return DTO de respuesta con la cuenta creada
     */
    CuentaBancariaDtoResponse createCuentaBancaria(CuentaBancariaDtoRequest request);

    /**
     * 🔹 Obtiene una cuenta bancaria por su ID.
     * 
     * @param id identificador de la cuenta
     * @return DTO de la cuenta bancaria
     */
    CuentaBancariaDtoResponse getCuentaBancaria(Long id);

    /**
     * 🔹 Obtiene todas las cuentas bancarias de una persona.
     * 
     * @param idPersona identificador de la persona
     * @return lista de cuentas bancarias
     */
    List<CuentaBancariaDtoResponse> getCuentasBancarias(Long idPersona);
    
    List<CuentaBancariaModel> getCuentasBancarias();

    /**
     * 🔹 Obtiene todas las cuentas bancarias registradas.
     * 
     * @return lista de cuentas bancarias
     */
    List<CuentaBancariaDtoResponse> findByAll(Estado estado);
    
    CuentaBancariaDtoResponse update(CuentaBancariaDtoResponse response);
}
