package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.Banco
import org.linketinder.model.objetos.Candidato

@TupleConstructor
class CandidatoService {
    Banco bd

    List<Candidato> get_lista(){
        try{
            return bd.read.get_lista_candidato()
        }
        catch (Exception ignored){
            return null
        }
    }

    void cadastrar(Candidato c){
        try{
            bd.create.cadastrar_candidato_if_not_exists(c)
        }
        catch (Exception ignored) {}
    }
}
