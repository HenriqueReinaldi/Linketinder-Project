package org.linketinder.dao

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Empresa

class Banco {
    List<Candidato> candidatos = []
    List<Empresa> empresas = []
    
    int get_qtd_candidatos(){
        return candidatos.size()
    }
    int get_qtd_empresas(){
        return empresas.size()
    }

    boolean cadastrar_candidato(Candidato c){
        if (c == null) return false;

        candidatos.add(c);
        return true;
    }

    boolean cadastrar_empresa(Empresa m){
        if (m == null) return false;

        empresas.add(m);
        return true;
    }
}

