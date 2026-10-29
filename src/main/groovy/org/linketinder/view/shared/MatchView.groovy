package org.linketinder.view.shared

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Match
import org.linketinder.view.ViewIO
import org.linketinder.view.traits.Representavel

@TupleConstructor
class MatchView implements Representavel<Match> {
    ViewIO view

    @Override
    String representacao(Match objeto) {
        """Candidato     : ${objeto.candidato.id} ${objeto.candidato.nome}
           |Vaga         : ${objeto.vaga.id} ${objeto.vaga.nome}
           |Recrutador   : ${objeto.recrutador.id} ${objeto.recrutador.nome}
           |Data Match   : ${objeto.data_match}
        """.stripMargin()
    }

    @Override
    void exibir(Match objeto) {
        view.send_message(representacao objeto)
    }
}
