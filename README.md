[JAVA_BADGE]:https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white
[SPRING_BADGE]: https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white
[MYSQL_BADGE]:https://img.shields.io/badge/MySQL-%234479A1.svg?style=for-the-badge&logo=mysql&logoColor=white
[AWS_BADGE]:https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white
[LOMBOK_BADGE]:https://img.shields.io/badge/Lombok-%2318A558.svg?style=for-the-badge&logo=lombok&logoColor=white
[JPA_BADGE]:https://img.shields.io/badge/JPA-%2300A3E0.svg?style=for-the-badge&logo=java&logoColor=white
[JUNIT_BADGE]:https://img.shields.io/badge/JUnit5-25A162.svg?style=for-the-badge&logo=JUnit5&logoColor=white
[FLYWAY_BADGE]:https://img.shields.io/badge/Flyway-%2300A6A0.svg?style=for-the-badge&logo=flyway&logoColor=white
[SPRING_SECURITY_BADGE]:https://img.shields.io/badge/Spring%20Security-6DB33F.svg?style=for-the-badge&logo=Spring-Security&logoColor=white
[DOCKER_BADGE]:https://img.shields.io/badge/Docker-2496ED.svg?style=for-the-badge&logo=Docker&logoColor=white

<h1 align="center" style="font-weight: bold;">Banking System 💻</h1>

![AWS][AWS_BADGE]
![spring][SPRING_BADGE]
![java][JAVA_BADGE]
![mysql][MYSQL_BADGE]
![lombok][LOMBOK_BADGE]
![jpa][JPA_BADGE]
![junit][JUNIT_BADGE]
![flyway][FLYWAY_BADGE]
![springsecurity][SPRING_SECURITY_BADGE]
![docker][DOCKER_BADGE]

<p align="center">
 <a href="#started">Getting Started</a> • 
  <a href="#routes">API Endpoints</a> •
 <a href="#colab">Collaborators</a> •
</p>

<p align="center">
  <b>Bank account management with features for customer registration and financial operations, including withdrawals, deposits, transfers and PIX.</b>
</p>

<h2 id="started">🚀 Getting started</h2>

<h3>Prerequisites</h3>

- [Java](https://download.oracle.com/java/17/archive/jdk-17.0.6_windows-x64_bin.msi)
- [Spring Boot](https://start.spring.io/)
- [MySQL](https://dev.mysql.com/downloads/installer/)
- [Git 2](https://github.com)

<h3>Cloning</h3>

```bash
https://github.com/Gabrielgln/BankingSystem.git
```

<h3>Starting</h3>

```bash
cd BankingSystem
run BankingSystemApplication
```

<h2 id="routes">📍 API Endpoints</h2>

| route                                 | description                                          
|---------------------------------------|---------------------------------------------------------------------------------------------------------
| <kbd>POST /user</kbd>                 | register a user, see [request details](#post-user-detail)
| <kbd>POST /auth</kbd>                 | authenticate user into the api, see [request details](#post-auth-detail)
| <kbd>POST /address</kbd>              | create address in API, see [request details](#post-address-detail)
| <kbd>POST /agency</kbd>               | create agency in API, see [request details](#post-agency-detail)
| <kbd>POST /client</kbd>               | create client in API, see [request details](#post-client-detail)
| <kbd>POST /account</kbd>              | create account in API, see [request details](#post-account-detail)
| <kbd>POST /transaction/transfer</kbd> | create tranfer in API, see [request details](#post-transaction-transfer-detail)

<h3 id="post-user-detail">POST /user</h3>

**REQUEST**
```json
{
  "email": "gabriel@gmail.com",
  "password": "12345",
  "role": "ADMIN"
}
```

**RESPONSE**
```json
{
  "email": "gabriel@gmail.com",
  "password": null,
  "role": "ADMIN"
}
```

<h3 id="post-auth-detail">POST /auth</h3>

**REQUEST**
```json
{
  "email": "ezequias@gmail.com",
  "password": "12345"
}
```

**RESPONSE**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoLWFwaSIsInN1YiI6ImV6ZXF1aWFzQGdtYWlsLmNvbSIsImV4cCI6MTcyMzIxMjI1OH0.uKB2fcg3BS7-niaZf8iIccLJA0Zk-XK_3DZgbHMQxLc",
  "tokenRefresh": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoLWFwaSIsInVzZXIiOiJlemVxdWlhc0BnbWFpbC5jb20iLCJjb2RlQWNjZXNzIjoiOGQxMGEyYjQtNjZmZS04NzIyLTE5NGItNWJjNjU1NTRkZTE5IiwiZXhwIjoxNzI0NDY1MDU4LCJpc1JlZnJlc2hUb2tlbiI6dHJ1ZX0.7JJq7C8LSHDIVkJIejhbrLnfBnULOvkOvOM0pszjHgU"
}
```

<h3 id="post-address-detail">POST /address</h3>

**REQUEST**
```json
{
  "cep": "58700-010",
  "number": "872",
  "street": "Rua do Prado",
  "district": "Centro"
}
```

**RESPONSE**
```json
{
  "id": "22d5a7d8-0f38-4f23-8715-3a340daded9d",
  "cep": "58700-010",
  "number": "872",
  "street": "Rua do Prado",
  "district": "Centro"
}
```

<h3 id="post-agency-detail">POST /agency</h3>

**REQUEST**
```json
{
  "name": "Sede",
  "telephone": "83988997766",
  "number": "45890120",
  "addressId": "22d5a7d8-0f38-4f23-8715-3a340daded9d"
}
```

**RESPONSE**
```json
{
  "id": "b7f95a5e-38fd-42ce-a4d1-ccee3635c151",
  "name": "Sede",
  "telephone": "83988997766",
  "number": "45890120",
  "address_id": "22d5a7d8-0f38-4f23-8715-3a340daded9d"
}
```

<h3 id="post-client-detail">POST /client</h3>

**REQUEST**
```json
{
  "name": "Ezequias",
  "cpf": "12374951480",
  "telephone": "83987164734",
  "address_id": "22d5a7d8-0f38-4f23-8715-3a340daded9d"
}
```

**RESPONSE**
```json
{
  "id": "310b6d0e-4d15-48c5-821e-791e58acae5d",
  "name": "Ezequias",
  "cpf": "12374951480",
  "telephone": "83987164734",
  "address_id": "22d5a7d8-0f38-4f23-8715-3a340daded9d",
  "user_id": "fed1022f-6767-468e-8b68-bfd5ac91b096"
}
```

<h3 id="post-account-detail">POST /account</h3>

**REQUEST**
```json
{
  "accountType": "CURRENT",
  "agencyId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```

**RESPONSE**
```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "number": "string",
  "accountType": "CURRENT",
  "agencyId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "balance": 0,
  "clientId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```

<h3 id="post-transaction-transfer-detail">POST /transaction/transfer</h3>

**REQUEST**
```json
{
  "accountType": "CURRENT",
  "receiverId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "value": 0
}
```

**RESPONSE**
```json
{
  "accountType": "CURRENT",
  "transactionType": "DEPOSIT",
  "agencyId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "accountId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "balance": 0,
  "dataTransaction": "2024-08-12T15:19:16.683Z",
  "valueTransaction": 0
}
```

<h2 id="colab">🤝 Collaborators</h2>

Special thank you for all people that contributed for this project.

<table>
  <tr>
    <td align="center">
      <a href="#">
        <img src="https://avatars.githubusercontent.com/u/106107461?v=4" width="100px;" alt="Gabriel Lira Profile Picture"/><br>
        <sub>
          <b>Gabriel Lira</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="#">
        <img src="https://avatars.githubusercontent.com/u/87997012?v=4" width="100px;" alt="Ezequias Soares Profile Picture"/><br>
        <sub>
          <b>Ezequias Soares</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="#">
        <img src="https://avatars.githubusercontent.com/u/58977849?v=4" width="100px;" alt="Eric Soares Profile Picture"/><br>
        <sub>
          <b>Eric Soares</b>
        </sub>
      </a>
    </td>
  </tr>
</table>
