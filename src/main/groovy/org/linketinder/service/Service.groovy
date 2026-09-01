package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.dao.Banco
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Empresa

@TupleConstructor
class Service {
    Banco bd;

    boolean cadastrar_candidato(Candidato candidato){
        return bd.cadastrar_candidato(candidato)
    }

    boolean cadastrar_empresa(Empresa empresa){
        return bd.cadastrar_empresa(empresa);
    }
}
