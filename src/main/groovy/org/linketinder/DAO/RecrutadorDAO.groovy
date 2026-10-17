package org.linketinder.DAO

import org.linketinder.model.objetos.Recrutador

interface RecrutadorDAO {
    List<Recrutador> get_lista_recrutador()

    Recrutador get_recrutador_by_id(int id)

    int get_recrutador_id_by_CPF(String CPF)

    int get_nicho_id_by_nome(String nicho)

    int cadastrar_recrutador_se_nao_existe(Recrutador recrutador)

    boolean delete_recrutador_by_id(int id)

    boolean update_recrutador(Recrutador recrutador)
}