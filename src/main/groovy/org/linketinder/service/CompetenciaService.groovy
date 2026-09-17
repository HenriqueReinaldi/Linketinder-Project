package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.Banco
import org.linketinder.model.objetos.Competencia

@TupleConstructor
class CompetenciaService {
    Banco bd

    List<Competencia> get_lista(){
        try{
            return bd.read.get_lista_competencia()
        }
        catch (Exception ignored){
            return null
        }
    }
}
