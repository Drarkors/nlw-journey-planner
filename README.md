![Banner NLW Journey](./readme-assets/banner_xl.png "NLW Journey Banner")

## Plann.er - NLW Journey

---

Aplicação desenvolvida durante a NLW Journey, utilizando de Java, Maven, SpringBoot, SpringJPA, Spring Validation e
Flyway.
Utilizando do H2 como banco em memória para facilitar o desenvolvimento.

#### 🛠️ Tecnologias

<img src="./readme-assets/spring-3.svg" style="width: 20px"> Spring: https://spring.io

---

### Pré-requisitons:

Maven, Java 21 e IntelliJ.

---

### 🎲 Executando

Após clonar o repositório, acesse o diretório, abra-o com o IntelliJ ou execute o comando Maven:

```bash
$ mvn clean install -U
```

Depois, você já pode executar a aplicação.

---

### 🧪 Testes mutáveis

Diferente da suíte do JUnit padrão, os testes mutáveis utilizam do PITest,
sendo necessário rodar um dos comandos abaixo para executa-los:

```bash
$ mvn test-compile org.pitest:pitest-maven:mutationCoverage
```

Caso já tenha rodado uma vez você pode usar da flag de `withHistory`:

```bash
$ mvn -DwithHistory test-compile org.pitest:pitest-maven:mutationCoverage
```