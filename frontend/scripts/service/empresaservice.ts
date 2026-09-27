import { fetch_localstorage, update_localstorage, lista_empresas } from "../persistencia/dados.js";
import { Empresa } from "../model/empresa.js";



export function popular_empresas(): void{
    for (let i: number = 0; i < 5; i++){
        lista_empresas.push(
            new Empresa(
                `nome${i}`,
                `email${i}`,
                `estado${i}`,
                `CEP${i}`, 
                `descricao${i}`,
                `pais${i}`,
                `CNPJ${i}`
            )
        );
    }
}

export function cadastrar_empresa(nome: string, email: string, estado:string, CEP:string, descricao:string, pais:string, CNPJ:string): boolean{
    fetch_localstorage();
    
    try{
        let emp: Empresa = new Empresa(
            nome, email, estado, CEP, descricao, pais, CNPJ
        )

        lista_empresas.push(emp);
    } catch (_){
        return false;
    }
    
    update_localstorage();
    return true;
}