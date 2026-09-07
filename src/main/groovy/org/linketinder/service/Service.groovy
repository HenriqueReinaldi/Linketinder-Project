package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.database.Banco
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga

@TupleConstructor
class Service {
    Banco bd
}
