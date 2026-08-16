package org.linketinder.classes

import groovy.transform.ToString
import groovy.transform.TupleConstructor

@ToString(includeSuperProperties = true, includeNames = true)
@TupleConstructor(includeSuperProperties = true)
class Candidato extends Pessoa{
    String CPF
    int idade
    List competencias = [];

    void representacao(){
        println """Candidato $nome:
        Idade : $idade
        Email : $email
        Estado: $estado
        CEP   : $CEP
        CPF   : $CPF
        Competencias: $competencias
        """
    }
}
