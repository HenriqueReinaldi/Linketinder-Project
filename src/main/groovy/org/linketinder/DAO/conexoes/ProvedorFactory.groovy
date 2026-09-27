package org.linketinder.DAO.conexoes

class ProvedorFactory {

    static ProvedorBanco pegar_provedor(String tipo) {
        switch (tipo) {
            case "postgres":
                return new ProvedorPostgres()
            default:
                return null
        }
    }
}
