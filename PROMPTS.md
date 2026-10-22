Lista dos prompts:

agents: openrouter -thinking machines, copilot

# Criar AGENTS.md:

    Look at the repository and create an AGENTS.md file. contains: overview, stack, strucutre, how to execute and test, conventions, restrictions, domain entities and how they relate, business rules, database.
    RUles:
    0. In case of doubts, ambiguity or a decision that hasnt been written, stop and ask instead of making decisions by yourself.
    1 .anonimity principle: um candidato nao sabe qual empresa a vaga pertence, uma empresa nao sabe o nome/ informacoes pessoais do candidato que curtiu

    Do take into account that the project found in src/ and the projkect found in frontend/ are completely separate and have different business logic.

<br>

    update agents.md so that it explains that the endpoints / functions that fully expose candidates / compaines are admin-related, so they dont break any rules.
    make sure to reference database attributes and references, also, update business rules..
    Curtida:
    candidato curte vaga -> novo recorde curtida é criado. UM candidato pode curtir varias vagas mas apenas uma curtida por vaga
    Match:
    empresa curte candidato que curtiu vaga -> recorde de curtida é atualizado
    do also consider the postgres schema to infer other rules. make the rule separation clear instead of point list.

<br>

    Treat these as separate projects with different models and business logic. The frontend is not currently an API client for the backend. Do not infer that a change in one project should be copied to the other, or that their models are interchangeable.
    though they are separate projects, that is because they are still in development. THey should share the same bussines rules and logic, but current state does not follow it because it is in development.
    update agents.md to reflect that
<br>

# Criar skill:

    PLease, refactor this skill takinig into account all that is said on agents.md.

    this skill is suposed to be a SPEC DRIVEN DEVELOPMENT.
    specs go into docs/specs/<nome-da-feature>
    plans of implementation go into docs/plans/<nome-da-feature>

    plans should be derived from the spec, really small tasks that can be analyzed tested and validated sequentially.

<br>

    Posteriormente, foi pedido para ser alterada a estrutura de pastas "docs/specs/" e "docs/plans"... mas perdi esse prompt.    

# Criar changelog:

    Create in the root of the project a CHANGELOG.md file.

    it shall follow the KEEP A CHANGELOG pattern: a [não lançado] section at the top and the changes grouped by "adicionado", "modificado", "corrigido" and "removido"

<br>

    PLease do make sure that agents.md has a reference to the changelog.

<br>

# Criar especificação:

### Recrutador — spec do feature:

    lets create the spec for a new feature "recrutador".
    alongside what is already defined, it should contain data model, anonymity principle, how the entity interacts with others, how it works with likes and matches, scope of action, and validations.

<br>

# Criar plano de implementação:

### Recrutador — plano de implementação:

    great, lets now create a plan of implementation.
    then fragment the plan with emphasis on MVCS + DAO + endpoints separation.
    order:
    1 - model
    2 - view + controller
    3 - service
    4 - dao
    5 - endpoints

    also, create the database schema before all of that and keep code generation aligned with already existing patterns.

<br>

### Recrutador — fluxo de curtida e match:

    this needs to take into account which recruiter is trying to like back.
    we need to collect the recruiter ID and verify if he is actually allowed to like the vaga.
    change the flow as needed.

    this is tied to the match business rule:
    empresa curte candidato que curtiu vaga -> recorde de curtida é atualizado

<br>

### Ajustes de negócio e regra de anonimato:

    update agents.md and the specific docs so that endpoints / functions that fully expose candidates / companies are clearly admin-related, so they do not break the anonymous flow.
    also clarify that any inconsistency exists only because the app is still in development.
    it is essential to keep the business rules separated clearly from implementation detail.

<br>

# Implementações em sequência (tarefas "implement task X"):

### Task 0:

    Lets start implementing task 0.
<br>

    add some mock recruiters in engrega.sql, like how there are mock users


### Task 1:

    Lets start implementation phase with task 1.

<br>

### Task 2:
    Now, implement task 2 from the plan.
<br>

    this bit is completely redundant
    lets change this to curtir_como_recrutador(Curtida c)

<br>

    assemble_resposta_curtida is useless, so si recrutador_responder_curtida... unless you can explain their purpose, remove them

<br>

### Task 3:

    Infelizmente perdi os prompts dessa task...
<br>

### Task 4:

    implement task 4 of the plan

<br>

    just like candidato does, recrutador needs to get from empresa dao the full empresa.

<br>

    make it so when creating a vaga / recrutador, if NICHO does not exist then we create one to use, watch how other DAOS handled situations like this to do it

<br>

### Task 5:

    lets start implementing the 5th task of the plan

<br>

### Task 6: 

    move onto task 6
<br>


### Expansão do plano: 
<br>

    lets expand the plan, we now need:
    A new controller function to let recruiter view all curtidas that references vagas that contains the same nicho and references the same empresa as the recruiter.

<br>

    now add the implementation plan steps for the frontend part.

    should follow the same logic as already implemented, with a new "visao recrutador" that lets us select the recruiter by ID and view matching curtidas.
    by that logic, we need to implement a feature to let candidatos to like vagas.

<br>

### Task 7:

    lets implement STEP 7.

<br>

    Lets add a new viw trait: SecretamenteRepresntavel.
    it shall contain a function

    String representacao_secreta()

    that function changes names like CANDIDATO : CANDIDATO_NOME to descriptions instead.

    lets implment it for curtida view and use that function whn we call "recrutador.curtidas"

<br>

### Task 8:

    yoo my guy, lets implement task 8 from the plan

<br>

    please add the other recruiter attributes as seen in spec.


<br>

### Task 9:

    lets implement task 9

<br>

    my guy, no CSS, the view_recrutador looks nothing like the other view pages.
    base it entirely on the other pages.

<br>

### Implement task 10:

    lets implement task 10 of the plan

<br>

    expand the part 10 of the plan to also include the logic of allowing a recruiter like a candidate back.
    on the view_recrutador page, lets add a button to the curtidas with no likebakcs.
    the button creates a match. THis shall finalize task 10

<br>

### Implement task 11:

    im pretty sure task 11 is already implemented, verify that

<br>

    lets add a new task behind task 12. one to allow creating new recruiters just like how its done with candidato and empresa.

<br>

### Implement task 12:

    Implement task 12.


<br>

### Implement task 13:

    Verify task 13.

<br>

# Adicionar criptografia para senhas:

    lets change this. We should now store passwords by passing it through SHA512 function.
    though not 100% safe, it should be better than plain-text storage.
<br>

    Now, lets add a new section right after task 13 with a new task #14, that modifies the service layer to apply the new password policy to recruiter.
    this change should only affect the recruiter.

<br>

    implement task 14 of the plan
<br>

## Nota:

Para fazer esse MARKDOWN, encontrei o diretório contendo histórico de mensagens. Pedi para o copilot analisar os arquivos e reconstruir as partes principais das minhas conversas.