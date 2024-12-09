<div id="top"></div>

<p align="center">
  <h1 align="center">Job Searching API</h1>
</p>

<p align="center">
  <a href="#-product">Product</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-stack">Stack</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-structure">Structure</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-execution">Execution</a>
</p> 

<p align="center">
  <a href="https://www.oracle.com/br/java/technologies/javase/jdk17-archive-downloads.html"><img alt="Java" src="https://img.shields.io/badge/Java-CC342D?style=for-the-badge&logo=java&logoColor=white"></a>
  <a href="https://spring.io/"><img alt="Spring" src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"></a>
  <a href="https://cucumber.io/docs/installation/java/"><img alt="Cucumber" src="https://img.shields.io/badge/Cucumber-23D96C?style=for-the-badge&logo=Cucumber&logoColor=FFFFFF"></a>
  <a href="https://www.postgresql.org/"><img alt="PostgresSQL" src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white"></a>
  <a href="https://le.ac.uk/"><img alt="UoL" src="https://img.shields.io/badge/UoL-E20612?style=for-the-badge&logo=leanpub"></a>
</p>

<p align = "center">
<b> :earth_americas: Quality Assurance and Software Measurement | :heart: Group Work </b>
</p>

## 💻 Product

<p>
Basically the project consists of a job provider/finder api, where providers can post jobs and accept or deny applications and seekers can find and apply to jobs.
</p>

<p align="right">(<a href="#top">back to top</a>)</p>

## ⚙ Stack

This project was developed using the following technologies:

|                                                                                         |                                       Technologies                                       |                                                         |
|:---------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------:|:-------------------------------------------------------:|
| [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) | [Spring Boot (3.1.2)](https://spring.io/blog/2023/07/20/spring-boot-3-1-2-available-now) | [Cucumber](https://cucumber.io/docs/installation/java/) |

<p align="right">(<a href="#top">back to top</a>)</p>

## 🎯 Objective

Integration between job seekers and providers in an easy way.

<p align="right">(<a href="#top">back to top</a>)</p>

## 🌌 Structure

For the organization of the application, it was separated into several folders so that they were distributed according
to their functions.

- ### **dockpay-fincore-api**
    - .mvn
        - Local maven wrapper library and configuration.

    - ***build***
        - Contains the Docker configuration to build and run the app locally.
        - sql
            - Contains sql files to create the DB locally when the container with postgres is deployed.

    - ***script***
        - Contains the scripts used to test/build the application.

    - ***src***
        - main
            - java
                - Contains project source code.
            - resources
                - Contains project configuration.
        - test
            - java
                - Contains project tests source code.
            - resources
                - Contains test configuration.
                - features
                    - Contains feature files used in BDD testing with cucumber.

    - .gitignore
        - Files ignored on commit.

    - mvnw/mvnw.cmd
        - Maven unix and bat local executable.

    - pom.xml
        - Project Maven configuration (project configuration, dependencies and build plugins).

    - README.md
        - Project basic documentation.

<p align="right">(<a href="#top">back to top</a>)</p>

## ⏩ Execution

### Prerequisites

- Install Java 17

    - Windows/MacOS/Linux
        - [Manual](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
    - macOS
        - [Homebrew](https://docs.brew.sh/Installation)
          ```bash
          brew install openjdk@17
          ```
    - Linux
        - Via terminal
          ```bash
          sudo apt update
          sudo apt-get install gnupg2 software-properties-common
          sudo add-apt-repository ppa:linuxuprising/java
          sudo apt-get install oracle-java17-installer oracle-java17-set-default
          ```

- Install Docker
    - [Windows/macOS/Linux/WSL](https://www.docker.com/get-started/)

<p align="right">(<a href="#top">back to top</a>)</p>

### Installation

- Run the following command to install dependencies and compile the project:
    - Windows
      ```bash
      mvnw clean install
      ```
      or
  
      ```bash
      ./mvnw clean install
      ```
    - MacOS/Linux
      ```bash
      ./mvnw clean install
      ```

<p align="right">(<a href="#top">back to top</a>)</p>

### Testing

- Run the following command to run the tests:
    - Windows
      - Black-box tests
        ```bash
        ./black-box-tests.bat
        ```
      - White-box tests
        - Code Coverage
          ```bash
          ./white-box-code-coverage-tests.bat
          ```
        - Mutation Coverage
          ```bash
          ./white-box-mutation-coverage-tests.bat
          ```
    - MacOS/Linux
      - Black-box tests
        ```bash
        zsh ./black-box-tests.sh
        ```
      - White-box tests
        - Code Coverage
          ```bash
          zsh ./white-box-code-coverage-tests.sh
          ```
        - Mutation Coverage
          ```bash
          zsh ./white-box-mutation-coverage-tests.sh
          ```

<p align="right">(<a href="#top">back to top</a>)</p>

### Running locally

- Run the following command to start docker compose and build the databases:
    - Windows
      ```bash
      ./script/docker-up.bat
      ```
    - MacOS
      ```bash
      zsh ./script/docker-up.sh
      ```
    - Linux
      ```bash
      chmod +x ./scripts/docker-up.sh
      ./script/docker-up.sh
      ```
    - You can connect to the DB using the following credentials:
        - host: localhost
        - username: user
        - password: password
        - port: 5432


- Start the application:
    - Windows
      ```bash
      mvnw spring-boot:run
      ```
      or
  
      ```bash
      ./mvnw spring-boot:run
      ```
    - MacOS/Linux
      ```bash
      ./mvnw spring-boot:run
      ```


- To stop the application just hit `Ctrl + C`:

- To put the db down run these commands:
    - Windows
      ```bash
      ./script/docker-down.bat
      ```
    - MacOS/Linux
      ```bash
      zsh ./script/docker-down.sh
      ```
    - Linux
      ```bash
      chmod +x ./scripts/docker-up.sh
      ./script/docker-down.sh
      ```

<p align="right">(<a href="#top">back to top</a>)</p>
