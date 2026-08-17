package org.linketinder.dados;

import org.linketinder.classes.Candidato;
import org.linketinder.classes.Empresa;

public class Cadastro {
    private final Dados d;

    public boolean cadastrar_candidato(Candidato c){
        if (c == null) return false;

        d.getCandidatos().add(c);
        return true;
    }

    public boolean cadastrar_empresa(Empresa m){
        if (m == null) return false;

        d.getEmpresas().add(m);
        return true;
    }

    public Cadastro(Dados d){
        this.d = d;
    }
}
