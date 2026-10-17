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

### Corrigido
- Compatibilidade da migração com versões de PostgreSQL que não suportam `ADD CONSTRAINT IF NOT EXISTS`.

### Removido
