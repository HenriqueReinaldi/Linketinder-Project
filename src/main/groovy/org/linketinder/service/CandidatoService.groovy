package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.Banco
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Curtida

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

    void deletar(int id){
        try{
            bd.delete.delete_candidato_by_id(id)
        }
        catch (Exception ignored) {}
    }

    void update(Candidato c){
        try{
            bd.update.update_candidato(c)
        }
        catch (Exception ignored) {}
    }

    void curtir(Curtida c){
        try{
            bd.create.cadastrar_curtida(c)
        }
        catch (Exception ignored) {}
    }

    Candidato get_by_id(String id){
        try {
            bd.read.get_candidato_by_id(Integer.parseInt(id))
        } catch (Exception ignored) {
            return null
        }
    }
}
