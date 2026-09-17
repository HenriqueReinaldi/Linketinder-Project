package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.Banco
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Vaga

@TupleConstructor
class VagaService {
    Banco bd

    List<Vaga> get_lista(){
        try{
            return bd.read.get_lista_vaga()
        }
        catch (Exception ignored){
            return null
        }
    }

    void cadastrar(Vaga v){
        try{
            bd.create.cadastrar_vaga_if_not_exists(v)
        }
        catch (Exception ignored) {}
    }
}
