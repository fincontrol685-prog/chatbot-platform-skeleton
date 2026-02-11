Tooling e recomendações operacionais

Build e Dependências

- Maven (wrapper incluído): gerenciar dependências e compilação.
- Plugins: spring-boot-maven-plugin, maven-compiler-plugin, mapstruct-processor.

Migrations

- Flyway ou Liquibase (prefer Flyway para simplicidade). Colocar scripts em db/migrations.

Contêiner e Infra

- Dockerfile para backend e frontend.
- Orquestração: Kubernetes (deployment, services, ingress).

CI/CD

- GitHub Actions ou GitLab CI com etapas: build backend, build frontend, run unit tests, run static analysis, build/push images, apply migrations (optionally via job). Use feature branches e PR reviews.

Observabilidade

- Logging: Logback + JSON encoder.
- Metrics: Micrometer + Prometheus.
- Tracing: OpenTelemetry.
- Dashboards: Grafana.

Segurança e Scans

- Dependabot/Snyk para dependências.
- Static code analysis: SonarCloud.

Dev Experience

- Use Docker Compose para rodar stack local: backend + Oracle (ou usar testcontainer/embedded DB para desenvolvimento) + Redis optional.
- Scripts: ./mvnw spring-boot:run, npm start

Releases

- Versionamento semântico e changelogs automáticos (conventional commits + semantic-release).

Backup e Migrations

- Gerenciar backups do Oracle via estratégia do DBA. Scripts idempotentes e reversíveis quando possível.

Nota

- Oracle JDBC driver pode ter restrições de licenciamento. Para desenvolvimento, considerar usar H2 com modo compatível para testes rápidos, e Oracle para staging/production.
