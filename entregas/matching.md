# Parte 1: BANCO DE DADOS

 - Para expandir a lógica de matching, criei uma nova tabela "match" que contem o id do candidato, vaga, data do match e o id do recrutador (caso o match tenha sido por um recrutador).
 - Uma entrada de match desaparece se o candidato ou vaga associada forem deletados. Caso o recrutador seja deletado, a entrada é atualizada e o campo recrutador_id é posto como nulo.
 - A lógica proposta é que quando o evento "match" ocorrer (empresa curtiu de volta o candidato), além da entrada na tabela curtida ser atualizada (empresa_curtiu), uma nova entrada na tabela match é criada.

# Parte 2: MVP (implementação)

 - TODO:
   - Necessario criar novo Controller + Service para match
   - Necessario alterar lógica já existente para novo comportamento