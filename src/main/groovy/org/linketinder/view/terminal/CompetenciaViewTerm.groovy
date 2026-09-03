package org.linketinder.view.terminal

import org.linketinder.model.objetos.Competencia
import org.linketinder.view.traits.Representavel

class CompetenciaViewTerm implements Representavel<Competencia>{
    @Override
    String representacao(Competencia objeto) {
        "Competencia: ${objeto.tecnologia}"
    }

    @Override
    void exibir(Competencia objeto) {
        println representacao(objeto)
    }
}
