package org.linketinder.dados

import org.linketinder.classes.Candidato
import org.linketinder.classes.Empresa

class Dados {
    List<Candidato> candidatos = []
    List<Empresa> empresas = []

    Random r = new Random()

    void init(){
        /*
        1.upto(5) {
            candidatos << new Candidato(
                    r.get_random_nome(),
                    r.get_random_email(),
                    "DF",
                    r.get_random_cep(),
                    "um bom candidato",
                    r.get_random_cpf(),
                    r.get_random_idade(),
                    r.get_random_competencias()
            )
        }

        1.upto(5) {
            empresas << new Empresa(
                    r.get_random_empresa_nome(),
                    r.get_random_email(),
                    "DF",
                    r.get_random_cep(),
                    "uma boa empresa",
                    r.get_random_cnpj(),
                    "BRASIL",
                    r.get_random_competencias()
            )
        }
        funciona, mas acho que "pré cadastrado" significa que deve ser constante... percebi tarde de mais
         */

        1.upto(5) {
            candidatos << new Candidato(
                    "nome $it",
                    "email${it}@mail",
                    "DF",
                    r.get_random_cep(),
                    "um bom candidato",
                    r.get_random_cpf(),
                    30,
                    r.get_fixed_competencias(it as int, it+2 as int)
            )
        }

        1.upto(5) {
            empresas << new Empresa(
                    "nome ${it*4}",
                    "email${it*4}@mail",
                    "DF",
                    r.get_random_cep(),
                    "uma boa empresa",
                    r.get_random_cnpj(),
                    "BRASIL",
                    r.get_fixed_competencias(it as int, it+2 as int)
            )
        }
    }
}
