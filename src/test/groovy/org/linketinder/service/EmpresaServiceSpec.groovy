package org.linketinder.service

import org.linketinder.DAO.Banco
import org.linketinder.DAO.CandidatoDAO
import org.linketinder.DAO.CompetenciaDAO
import org.linketinder.DAO.EmpresaDAO
import org.linketinder.DAO.EnderecoDAO
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import spock.lang.Specification

class EmpresaServiceSpec extends Specification {
    EmpresaDAO empresa_dao = Mock()
    EnderecoDAO endereco_dao = Mock()
    EmpresaService service = new EmpresaService(empresa_dao, endereco_dao)

    def "get lista empresa chama o metodo DAO correto"() {
        when:
        service.get_lista()

        then:
        1 * empresa_dao.get_lista_empresa()
    }

    def "cadastrar empresa chama os metodos DAO corretos"() {
        given:
        Empresa empresa = new Empresa(
                id: 1,
                endereco: new Endereco(id: 1),
        )

        when:
        service.cadastrar(empresa)

        then:
        1 * endereco_dao.cadastrar_endereco_se_nao_existe(empresa.endereco)
        1 * empresa_dao.cadastrar_empresa_se_nao_existe(empresa)
    }

    def "deletar empresa chama o metodo DAO correto"() {
        when:
        service.deletar(20)

        then:
        1 * empresa_dao.delete_empresa_by_id(20)
    }

    def "update empresa chama os metodos DAO corretos"() {
        given:
        Empresa empresa = new Empresa(
                id: 1,
                endereco: new Endereco(id: 1),
        )

        when:
        service.update(empresa)

        then:
        1 * endereco_dao.cadastrar_endereco_se_nao_existe(empresa.endereco)
        1 * empresa_dao.update_empresa(empresa)
    }

    def "get by CNPJ chama os metodos DAOs corretos"() {
        when:
        service.get_by_CNPJ("2033")

        then:
        1 * empresa_dao.get_empresa_id_by_CNPJ("2033") >> 20
        1 * empresa_dao.get_empresa_by_id(20)
    }
}
