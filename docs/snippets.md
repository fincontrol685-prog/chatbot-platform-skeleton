Trechos de código exemplares

1) pom.xml - dependências principais (trecho)

<dependencies>
  <!-- Spring Boot Starter -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>

  <!-- Security -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
  </dependency>

  <!-- JPA + Oracle JDBC -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  <dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc8</artifactId>
    <version>19.3.0.0</version>
  </dependency>

  <!-- Flyway -->
  <dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
  </dependency>

  <!-- MapStruct -->
  <dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.5.Final</version>
  </dependency>

</dependencies>

2) Exemplo Entity: Bot

package com.br.chatbotplatformskeleton.domain;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "BOT")
public class Bot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "bot_key", unique = true)
    private String key;

    private Boolean enabled = true;

    @Lob
    private String config;

    // getters/setters
}

3) Exemplo Security Config (simplificado)

package com.br.chatbotplatformskeleton.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .csrf().disable()
          .authorizeHttpRequests((authz) -> authz
              .requestMatchers("/api/auth/**").permitAll()
              .requestMatchers("/api/admin/**").hasRole("ADMIN")
              .anyRequest().authenticated()
          )
          .oauth2ResourceServer().jwt();

        return http.build();
    }
}

4) Angular - AuthService outline (typescript)

export class AuthService {
  private api = '/api/auth';

  login(username: string, password: string) {
    return this.http.post(`${this.api}/login`, { username, password });
  }

  refresh(refreshToken: string) {
    return this.http.post(`${this.api}/refresh`, { refreshToken });
  }
}

5) Angular - AuthGuard outline

export class AuthGuard implements CanActivate {
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}
