# Recrutador (Backend)

**Status:** aprovado para implementação.

**Processo confirmado para alterações no banco:** qualquer criação ou alteração de schema, tabela, restrição ou dado será entregue em um arquivo SQL dedicado, acompanhado de um arquivo com os comandos exatos para inspeção e aplicação. O usuário revisará e executará esses comandos. O assistente não deve conectar ao banco nem executar operações que alterem schema ou dados. Essa regra também se aplica a carga/população de dados e migrações.

## Contexto e objetivo

Uma empresa pode contratar recrutadores para encontrar candidatos e responder às curtidas recebidas em suas vagas. O recrutador atua em nome de uma única empresa e não pode administrar as vagas dela.

Esta especificação cobre primeiro o backend (`src/` e `banco_de_dados/`). A funcionalidade correspondente da aplicação frontend será especificada e implementada em uma etapa separada. As duas aplicações continuam independentes; esta feature não as conecta.

## Diretriz de implementação

Antes de criar código, consultar as implementações e testes existentes mais próximos e reutilizar seus padrões sempre que possível. Por exemplo, ao criar um controller de recrutador, revisar os controllers e specs atuais para manter a mesma organização e convenções. Só introduzir uma estrutura diferente quando houver uma necessidade concreta, registrada na implementação.

Quando a feature exigir mudança no banco, preparar primeiro o schema e a migração. As fases de código só começam depois que o usuário executar e validar as mudanças no banco, conforme o processo confirmado acima.

## Modelo de dados

### `nicho`

- `id`: chave primária.
- `nome`: obrigatório e único, compartilhado por vagas e recrutadores.
- A unicidade usa a restrição `UNIQUE` do banco sobre o valor armazenado; não há normalização de caixa ou espaços definida.
- Deve existir o valor `geral`. Ele pode ser atribuído tanto a um recrutador quanto a uma vaga.

### `recrutador`

- `id`: chave primária.
- `nome`: `varchar(50) NOT NULL`, conforme a coluna de candidato.
- `descricao`: `TEXT NOT NULL`.
- `e_mail`: `varchar(100) NOT NULL`, sem restrição de unicidade, conforme candidato.
- `CPF`: `varchar(11) UNIQUE NOT NULL`, conforme candidato.
- `data_nascimento`: `date NOT NULL`.
- `senha`: `TEXT NOT NULL` com comprimento mínimo de seis caracteres, conforme a restrição do schema de candidato.
- `empresa_id`: obrigatório, referência à empresa para a qual trabalha.
- `nicho_id`: obrigatório, referência ao nicho de atuação.
- Uma empresa pode ter vários recrutadores; cada recrutador pertence a uma empresa.
- O conjunto de campos segue a lista do issue. Não são adicionados sobrenome nem endereço, que existem no modelo de candidato mas não foram listados para recrutador.
- O e-mail é obrigatório, mas não único, tal como no schema atual de candidato.

### `vaga`

- Adicionar `nicho_id`, obrigatório para vagas classificadas após a migração, referenciando `nicho.id`.
- Cada vaga tem um nicho. Recrutadores de nicho específico só podem atuar em vagas do mesmo nicho; recrutadores de nicho `geral` podem atuar em qualquer vaga.
- A migração cria/garante o nicho `geral` e atribui esse nicho às vagas existentes. Uma vaga marcada `geral` é visível apenas para recrutadores `geral`, pois os demais precisam ter o mesmo nicho.

## Regras de negócio e interações

### Empresa e recrutador

- O recrutador atua exclusivamente em nome da empresa associada ao seu registro.
- O provisionamento, a atualização e a desativação de recrutadores são operações administrativas, fora dos fluxos da empresa nesta etapa.
- O backend deve filtrar a lista de vagas pelo `empresa_id` do recrutador e pelo nicho, conforme as regras acima.
- O recrutador também deve poder consultar as curtidas relevantes às vagas da sua empresa e compatíveis com o seu nicho, usando o mesmo critério funcional de empresa e nicho sem criar uma relação de autorização.
- O recrutador não pode criar, editar ou excluir vagas, nem alterar a empresa proprietária de uma vaga.

### Curtidas e matches

- O recrutador só pode responder a uma curtida candidato-vaga já existente; ele não cria a primeira curtida do par.
- Uma resposta válida marca `empresa_curtiu=true` no registro existente `(candidato_id, vaga_id)`, equivalendo à curtida da empresa associada.
- Se o candidato já curtiu a vaga, essa resposta completa o match. A resposta não cria um segundo registro nem guarda a identidade do recrutador que a realizou.
- O recrutador também pode listar as curtidas elegíveis para o seu escopo empresarial e de nicho: somente registros cuja vaga pertença à empresa do recrutador e cujo nicho seja compatível com o nicho do recrutador, com o caso `geral` permitindo qualquer vaga da empresa.
- A operação só pode afetar uma vaga da empresa associada e compatível com o nicho do recrutador. Uma tentativa fora desses limites não altera a curtida.
- O recrutador não pode responder por outra empresa nem atuar em vaga de outro nicho, exceto que o nicho `geral` permite todas as vagas.

### Candidatos e anonimato

- Empresas e recrutadores não recebem nome, sobrenome, idade, data de nascimento, CPF, e-mail ou endereço do candidato nos fluxos de seleção/curtida.
- Competências podem ser expostas como informação profissional não identificadora.
- **Exceção de risco aceita para esta etapa:** a descrição do candidato será exposta como texto livre sem proteção contra conteúdo identificador. Portanto, o anonimato não pode ser garantido enquanto esse texto puder conter nomes, contatos ou outros identificadores. A futura proteção contra descrições identificadoras é uma dependência de privacidade e não faz parte desta etapa.
- O candidato não recebe a identidade da empresa proprietária da vaga nem a identidade do recrutador nos fluxos voltados a candidatos.
- A anonimização deve ser aplicada aos dados retornados pelo backend, e não apenas à apresentação da interface.

### Autenticação e autorização

- A autenticação e a autorização de usuários ficam adiadas e fora do escopo desta etapa.
- O filtro por empresa/nicho é uma regra funcional aplicada pelo backend com base no recrutador informado, mas, sem identidade autenticada, não constitui uma fronteira de segurança: um chamador pode tentar informar outro recrutador. O requisito de isolamento entre empresas só poderá ser considerado seguro após uma etapa de autenticação/autorização.

## Validações e casos-limite

- Rejeitar recrutador sem qualquer campo obrigatório, sem empresa existente ou sem nicho existente.
- Rejeitar CPF duplicado e senha com menos de seis caracteres.
- Rejeitar nicho duplicado pela restrição `UNIQUE`; a comparação segue o valor armazenado pelo banco, sem equivalência automática entre caixa/espaços.
- Rejeitar vaga nova/atualizada sem nicho ou com nicho inexistente.
- Migrar vagas existentes para `geral`, sem apagar ou recriar vagas nem alterar outros dados delas.
- Rejeitar resposta de recrutador se não existir curtida do candidato para aquele par, se a vaga não pertencer à empresa associada ou se o nicho não for compatível; a rejeição não deve alterar o registro de curtida.
- Repetir uma resposta já registrada não cria registros duplicados nem reverte `empresa_curtiu` para `false`.
- **Decisão aprovada para esta implementação:** armazenar a senha do recrutador como texto simples, seguindo o padrão legado de candidato e empresa. Isso mantém o risco de exposição em caso de acesso indevido ao banco; qualquer migração futura para hash deverá considerar compatibilidade e redefinição/atualização de credenciais. A senha continua proibida em respostas, logs e serialização.
- Entregar mudanças no banco como arquivo(s) SQL e instruções com os comandos de inspeção/aplicação; somente o usuário executa esses comandos e valida os resultados no banco.

## Critérios de aceite

- Dado um recrutador associado a uma empresa e a um nicho específico, quando consulta vagas, então recebe somente vagas da empresa associada que tenham exatamente o mesmo nicho.
- Dado um recrutador de nicho `geral`, quando consulta vagas, então recebe vagas de qualquer nicho somente da empresa associada.
- Dado um recrutador associado a uma empresa e a um nicho específico, quando consulta curtidas elegíveis, então recebe somente registros cuja vaga pertença à empresa associada e tenha o mesmo nicho, ou qualquer nicho quando o recrutador é `geral`.
- Dada uma vaga existente antes da migração, quando a migração termina, então ela permanece disponível e fica associada ao nicho `geral`.
- Dado um recrutador, quando tenta criar, editar ou excluir uma vaga, então a operação é negada e os dados da vaga permanecem inalterados.
- Dado um candidato que já curtiu uma vaga elegível, quando o recrutador associado responde à curtida, então o mesmo registro passa a ter `empresa_curtiu=true` e o par representa um match.
- Dado um par candidato-vaga sem curtida prévia do candidato, quando o recrutador tenta responder, então nenhum registro de curtida é criado.
- Dado um recrutador que tenta responder em vaga de outra empresa ou de nicho incompatível, quando a operação é solicitada, então ela falha sem alterar a curtida.
- Dado um payload de candidato retornado para seleção, quando consultado por empresa ou recrutador, então não contém os campos estruturados identificadores listados nesta spec; contém competências e, conforme a exceção aceita, a descrição livre sem filtragem.
- Dado um fluxo de candidato que retorna uma vaga ou um evento de curtida/match, quando consultado pelo candidato, então não revela empresa proprietária nem recrutador.
- Dado um recrutador com senha, quando qualquer resposta ou log do backend é produzido, então a senha não é exposta.
- Dado um CPF ou nicho já existente, quando se tenta cadastrar outro com o mesmo valor conforme a restrição do banco, então o cadastro duplicado é rejeitado.

## Impactos no comportamento existente

- O schema e os dados de vaga mudam: será necessária migração que crie `nicho`, crie `recrutador`, adicione `vaga.nicho_id` e classifique vagas antigas como `geral`.
- A migração e qualquer carga ou alteração de dados serão fornecidas em arquivos para execução pelo usuário; o assistente não aplicará mudanças diretamente ao banco.
- A criação e atualização de vagas passam a exigir nicho.
- O fluxo existente de curtida de empresa continua representado pelo booleano `empresa_curtiu`; não haverá autoria ou histórico do recrutador nesta etapa.
- Os modelos/serviços/DAOs e qualquer serialização de dados de vaga/candidato afetados devem preservar as regras de anonimato acima, com a exceção explícita da descrição livre.
- As rotas atuais não autenticam usuários. Os novos filtros funcionais não resolvem esse risco, que permanece até uma futura etapa de autorização.

## Escopo

### Incluído

- Entidades e persistência backend para recrutador e nicho.
- Relações de nicho para recrutador e vaga, incluindo migração das vagas existentes.
- Consulta funcional de vagas por empresa e nicho do recrutador.
- Consulta funcional de curtidas elegíveis por empresa e nicho do recrutador.
- Resposta do recrutador à curtida existente como curtida da empresa associada.
- Restrições de ação sobre vagas e anonimização dos campos estruturados do candidato.
- Provisionamento administrativo de recrutadores.

### Excluído

- Implementação do frontend; será tratada em especificação/etapa própria.
- Integração entre frontend e backend.
- Autenticação e autorização seguras, incluindo garantia de identidade do recrutador nas rotas.
- Exposição segura da descrição do candidato; a proteção de conteúdo identificador fica para etapa futura, com o risco temporário descrito acima.
- Curtida inicial do candidato pelo recrutador, autoria/auditoria da resposta, notificações, mensagens ou outros passos de contratação.
- Criação, edição ou exclusão de vagas pelo recrutador.
- Validações de formato de e-mail/CPF ou regra mínima/máxima de idade além das restrições já definidas para candidato.
- Política segura de armazenamento de senha, que precisa ser decidida antes da implementação.