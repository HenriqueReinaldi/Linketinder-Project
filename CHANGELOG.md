# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [não lançado]

### Adicionado
- Criação do schema de `nicho` e `recrutador` no banco.
- Coluna `vaga.nicho_id` com referência obrigatória para `nicho.id`.
- Migração SQL para preencher automaticamente o nicho `geral` em vagas existentes.
- Dados mock de recrutadores de exemplo no seed de desenvolvimento.

### Modificado
- Atualização dos snapshots SQL de schema e entrega para refletirem o novo modelo de nicho e recrutador.
- Ajuste do seed de vagas para associar todas as vagas existentes ao nicho `geral`.
- Adicionado o modelo `Recrutador` e o campo `nicho` em `Vaga`, alinhando os domínios da feature com o plano aprovado.
- Implementada a persistência de recrutadores e o mapeamento dos nichos entre o modelo e as tabelas `nicho`, `recrutador` e `vaga`.
- Criação ou reutilização atômica do nicho ao cadastrar vagas e recrutadores, seguindo o padrão `ON CONFLICT` dos demais DAOs.
- Consulta de vagas do recrutador limitada no DAO à empresa associada e ao nicho compatível.
- Atualização de curtidas pelo recrutador condicionada a uma curtida existente e à elegibilidade de empresa e nicho.
- Serviços de recrutador para listar, cadastrar, atualizar e remover registros via DAO; listagem de vagas delegada ao filtro do DAO e hidratação dos detalhes retornados.
- Hidratação da empresa completa de cada recrutador pela `EmpresaDAO` ao listar recrutadores.
- Revisada a spec de `VagaService` para verificar a delegação da filtragem e a hidratação dos detalhes das vagas retornadas.

### Corrigido
- Compatibilidade da migração com versões de PostgreSQL que não suportam `ADD CONSTRAINT IF NOT EXISTS`.

### Removido
