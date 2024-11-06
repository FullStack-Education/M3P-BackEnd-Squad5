# M3P-BackEnd-Squad5

## Alunos

Ana Helena Fernandes Peres

Cheryl Henkels de Souza

Heverton Luan Roieski

Leonardo Madeira Barbosa da Silva

Suzi Fortunata Harima

Vitor Schultz Sertã Machado


## Descrição

Nesta API Rest, você encontrará uma solução para um sistema escolar. Neste sistema, há 5 papéis de usuário, cada um com usos e acessos específicos. Abaixo, você encontrará informações sobre o que cada um dos papéis faz e ao que eles têm acesso.

A API conta com diversas verificações e validações para garantir que cada papel só acesse aquilo que é permitido, além de logs de informações e erros.

Utilizando esta API, você pode fazer login no sistema recebendo um token JWT (para verificações de segurança), cadastrar novos usuários atribuindo um dos cinco papéis disponíveis. Buscar, cadastrar, e atualizar cada uma das entidades: docente, aluno, turma, curso, materia e nota, além de algumas outras funcionalidades para algumas das entidades, como por exemplo uma pontuação final para cada aluno.

Neste projeto, foram utilizadas as seguintes tecnologias:

- Spring Boot
- Spring Security
- Spring Data
- Maven
- Docker
- PostgreSQL

## Funcionalidades dos Papéis de Usuário

| Papel       | Funcionalidade                                                                 |
|-------------|--------------------------------------------------------------------------------|
| **Admin**   | Acesso total ao sistema; o único que pode deletar entidades.                   |
| **Pedagógico** | Acessa tudo sobre **turmas, cursos, professores e alunos**.                  |
| **Recruiter**  | Acessa informações sobre **professores**.                                    |
| **Professor**  | Acessa informações sobre **notas**.                                          |
| **Aluno**      | Acessa suas próprias **notas e pontuação total**.                            |

## Execução

### Configuração do Docker

Para rodar a API, você deve criar um contêiner com a imagem do PostgreSQL. Um Dockerfile com todas as configurações necessárias foi incluído no sistema, assim, para configurar o ambiente basta rodar

` docker-compose up -d`

### Configuração do banco de dados


### Configuração do banco de dados

Para este projeto foi utilizado o pgAdmin 4, as instruções serão passadas para a utilização do mesmo:

    Para criar um novo server, escolha o nome que preferir e coloque as seguintes informações:
    Vá em Connection
    Host name/address: localhost
    Port: 1432
    Maintenance database: sistema-escolar
    Username: meuUsuario
    Password: minhaSenha
    Clique em Save

Após isso o bando de dados estará configurado e você poderá seguir para a próxima etapa.

### Executando a API

Para poder executar a API clone o repositório usando:

```
  git clone https://github.com/FullStack-Education/M3P-BackEnd-Squad5.git
```
Caso não dê para rodar a aplicação logo após o clone, faça o seguinte:

    1. Clique com o botão direito no arquivo **pom.xml**.
    2. Clique em **add as maven projet**, deve estar nas últimas opções.
   
 
Após isso a aplicação deve rodar normalmente.

### Requisições

Para facilidade, o software foi documentado com o swagger.

Todos os edpoints podem ser acessados e testados a partir do link http://localhost:8080/swagger-ui/index.html

## Documentação da API

Ao rodar a API pela primeira vez, será populado o banco de dados com os 5 papéis possíveis, assim como um usuário para cada um.

Os logins para cada um são:

    1. admin
    2. pedagogico
    3. recruiter
    4. professor
    5. aluno

A senha por padrão é **1234**

Para fazer login entre na requisição **Token** e coloque o email e senha do usuário desejado.

Os emails dos usuários padrão são:

    1. admin@school.com
    2.pedagogico@school.com"
    3.recruiter@school.com"
    4.professor@school.com"
    5.aluno@school.com"

Para fazer uso das requisição copie o token jwt que você recebeu ao fazer login e cole na aba de Authorization da requisição, escolhendo a opção Bearer Token, lembre-se de fazer isso sempre que fizer login com outro usuário, assim você pode testar a fundo todas funcionalidades desta API.

Para cadastrar novos usuários você deve logar como um usuário **admin**.

No corpo de cada requisição terá todas informações necessárias para cadastrar ou atualizar algum item, como por exemplo no cadastro de um novo docente.

```
{
    "nome": "Nome do docente",
    "usuario": 1
}
```

### Lista das requisições

#### Retorna todos os itens

```http
  GET localhost:8080/nome da entidade/
```

| Descrição                           |
| :---------------------------------- |
| Retorna uma lista de itens da entidade em questão |

#### Retorna um item

```http
  GET localhost:8080/nome da entidade/{id}
```

| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | Retorna busca o item com id em questão |

#### Cadastrar um item

```http
  POST localhost:8080/nome da entidade
```

| Descrição                                   |
| :------------------------------------------ |
| Cadastra um novo item na tabela em questão |

#### Atualizar um item

```http
  PUT localhost:8080/nome da entidade/{id}
```

| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | Atualiza informações do item em questão |

#### Deletar  um item

```http
  DELETE localhost:8080/nome da entidade/{id}
```

| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | Deleta o item em questão |


### Requisições adicionais

#### Buscar Usuário por Usuário login

```http
  GET localhost:8080/usuarios/{login}
```

| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `usuario login`      |Busca o usuário através do sesu nome de login |

#### Buscar dados do dashboard

```http
  GET localhost:8080/dashboard
```

| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
|      |Busca os dados do dashboard |

#### Buscar Materia por Curso id

```http
  GET localhost:8080/materia/cursos/{curso_id}
```

| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `curso_id`      |Busca todas as matérias referente ao curso em questão |

#### Buscar Cursos por Aluno id

```http
  GET localhost:8080/cursos/cursos/{curso_id}
```

| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `aluno_id`      |Busca todos os cursos referente ao aluno em questão |


#### Buscar Nota por Docente id

```http
  GET localhost:8080/nota/docentes/{docente_id}
```

| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `docente_id`      |Busca todas as notas que o docente em questão lançou |

#### Buscar Nota por Aluno id

```http
  GET localhost:8080/nota/alunos/{aluno_id}
```

| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `aluno_id`      |Busca todas as notas do aluno em questão |


#### Buscar Pontuação por Aluno id

```http
  GET localhost:8080/nota/alunos/{aluno_id}/pontuacao
```

| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `aluno_id`      |Busca a pontuação total do aluno em questão |


## Informações finais
Este projeto foi desenvolvido para o curso FullStack [Education] para compor a nota do módulo.

O projeto está todo contido no repositório do gitHub: <https://github.com/FullStack-Education/M3P-BackEnd-Squad5.git>

A organização das tarefas foi feita no Trello e pode ser conferida no quadro (board):
<https://trello.com/b/1dxhDGdw/m3p-backend-squad-5>


