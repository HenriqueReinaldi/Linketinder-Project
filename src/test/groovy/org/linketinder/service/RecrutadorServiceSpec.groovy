package org.linketinder.service

import org.linketinder.DAO.EmpresaDAO
import org.linketinder.DAO.RecrutadorDAO
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Recrutador
import spock.lang.Specification

class RecrutadorServiceSpec extends Specification {
    RecrutadorDAO recrutador_dao = Mock()
    EmpresaDAO empresa_dao = Mock()
    RecrutadorService service = new RecrutadorService(recrutador_dao, empresa_dao)

    def "get lista recrutador hidrata empresa completa pelo DAO de empresa"() {
        given:
        Recrutador recrutador = new Recrutador(id: 3, empresa: new Empresa(id: 8))
        Empresa empresa = new Empresa(id: 8, nome: "Empresa completa", email: "empresa@example.com")

        when:
        List<Recrutador> resultado = service.get_lista()

        then:
        1 * recrutador_dao.get_lista_recrutador() >> [recrutador]
        1 * empresa_dao.get_empresa_by_id(8) >> empresa
        resultado.size() == 1
        resultado[0].empresa.is(empresa)
        resultado[0].empresa.nome == "Empresa completa"
    }

    def "cadastra recrutador delega ao DAO sem validar campos no service"() {
        given:
        Recrutador recrutador = new Recrutador()

        when:
        boolean cadastrado = service.cadastrar(recrutador)

        then:
        1 * recrutador_dao.cadastrar_recrutador_se_nao_existe(recrutador) >> 12
        cadastrado
    }

    def "cadastro falho retorna falso"() {
        given:
        Recrutador recrutador = new Recrutador()

        when:
        boolean cadastrado = service.cadastrar(recrutador)

        then:
        1 * recrutador_dao.cadastrar_recrutador_se_nao_existe(recrutador) >> -1
        !cadastrado
    }

    def "deleta recrutador delega ao DAO"() {
        when:
        boolean deletado = service.deletar(20)

        then:
        1 * recrutador_dao.delete_recrutador_by_id(20)
        deletado
    }

    def "atualiza recrutador delega ao DAO sem validar campos no service"() {
        given:
        Recrutador recrutador = new Recrutador()

        when:
        boolean atualizado = service.update(recrutador)

        then:
        1 * recrutador_dao.update_recrutador(recrutador)
        atualizado
    }
}
