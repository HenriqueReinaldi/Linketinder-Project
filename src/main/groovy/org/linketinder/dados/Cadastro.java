package org.linketinder.dados;

import org.linketinder.classes.Candidato;
import org.linketinder.classes.Empresa;

public class Cadastro {

    private final Dados d;

    public boolean cadastrar_candidato(Candidato c){
        return false;
    }

    public boolean cadastrar_empresa(Empresa m){
        return false;
    }

    public Cadastro(Dados d){
        this.d = d;
    }
}
