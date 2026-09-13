package org.linketinder.service

import groovy.transform.TupleConstructor
import groovy.transform.Undefined.EXCEPTION
import org.linketinder.controller.AssembleModel
import org.linketinder.database.Banco
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga

import java.sql.SQLException

@TupleConstructor
class Service {
    Banco bd

    static <GENERICO> GENERICO executar_com_seguranca(Closure<GENERICO> operacao){
        try{
            return operacao()
        }
        catch (Exception e){
            println "erro executando operação:"
            println "    " + e.message
            println ""
            //e.printStackTrace()
        }
        return null
    }


    <GENERICO> List<GENERICO> get_lista_generico(String entidade){
        executar_com_seguranca {
            bd.read."get_lista_${entidade}"()
        }
    }

    <GENERICO> void cadastrar_generico(String entidade, GENERICO entidade_objeto){
        executar_com_seguranca {
            bd.create."cadastrar_${entidade}_if_not_exists"(entidade_objeto)
        }
    }

    void deletar_generico(String entidade, int id_entidade){
        executar_com_seguranca {
            bd.delete."delete_${entidade}_by_id"(id_entidade)
        }
    }

    <GENERICO> void update_generico(String entidade, GENERICO entidade_objeto){
        executar_com_seguranca {
            bd.update."update_${entidade}"(entidade_objeto)
        }
    }


    void empresa_curtir(Curtida c){
        executar_com_seguranca {bd.update.empresa_curtir(c)}
    }
    void candidato_curtir(Curtida c){
        executar_com_seguranca {
            bd.create.cadastrar_curtida(c)
        }
    }


    Empresa get_empresa_by_CNPJ(String CNPJ){
        executar_com_seguranca {
            int emp_id = bd.read.get_empresa_id_by_CNPJ(CNPJ)
            if (emp_id == -1) return null
            bd.read.get_empresa_by_id(emp_id)
        }
    }
    Candidato get_candidato_by_id(String id){
        executar_com_seguranca {
            bd.read.get_candidato_by_id(Integer.parseInt(id))
        }

    }
    Vaga get_vaga_by_id(String id){
        executar_com_seguranca {
            bd.read.get_vaga_by_id(Integer.parseInt(id))
        }
    }

}
