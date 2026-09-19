package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.Banco
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa

@TupleConstructor
class CurtidaService {
    Banco bd

    List<Curtida> get_lista(){
        try{
            return bd.read.get_lista_curtida()
        }
        catch (Exception ignored){
            return null
        }
    }
}
