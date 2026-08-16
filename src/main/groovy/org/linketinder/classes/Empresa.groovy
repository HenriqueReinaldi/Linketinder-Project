package org.linketinder.classes

import groovy.transform.ToString
import groovy.transform.TupleConstructor

@ToString(includeSuperProperties = true, includeNames = true)
@TupleConstructor(includeSuperProperties = true)
class Empresa extends Pessoa{
    String CNPJ, pais
    List competencias_desejadas = [];


    void representacao(){
        println """Empresa $nome:
        Email : $email
        Pais  : $pais
        Estado: $estado
        CEP   : $CEP
        CNPJ  : $CNPJ
        Busca : $competencias_desejadas
        """
    }
}
