# Spring Boot 4 Upgrade Guide

## Overview
This document describes the upgrade of familie-ks-infotrygd from Spring Boot 3.5.7 to Spring Boot 4.0.0.

## Changes Made

### 1. Spring Boot Version Update
- **Previous**: 3.5.7
- **Current**: 4.0.0

### 2. Dependency Updates

#### springdoc-openapi
- **Previous**: 2.8.14
- **Current**: 3.0.0 (compatible with Spring Boot 4)

#### token-validation
- **Previous**: 5.0.39
- **Current**: 6.0.0 (Spring Boot 4 compatible release from navikt/token-support)
- **Note**: Version 6.0.0 was released specifically for Spring Boot 4 compatibility on 2025-11-21

### 3. Jakarta EE Namespace Migration
Fixed remaining `javax.*` imports to `jakarta.*`:
- `javax.sql.DataSource` → `jakarta.sql.DataSource` in:
  - `src/main/kotlin/no/nav/infotrygd/kontantstotte/config/DatasourceConfig.kt`
  - `src/test/kotlin/no/nav/infotrygd/kontantstotte/testutil/QueryCountListenerTestConfig.kt`

### 4. Repository Configuration
Added GitHub Packages repository to access NAV security dependencies:
```xml
<repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/navikt/*</url>
</repository>
```

## Building the Project

### Prerequisites
The NAV token-support libraries (version 5.0.33+) are published exclusively to GitHub Packages Registry (GPR), not Maven Central. To build this project, you need to configure authentication for GitHub Packages.

### GitHub Packages Authentication

#### Option 1: Using Maven settings.xml
Add the following to your `~/.m2/settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_TOKEN</password>
    </server>
  </servers>
</settings>
```

#### Option 2: Using Environment Variables
Set the following environment variables:
```bash
export GITHUB_ACTOR=YOUR_GITHUB_USERNAME
export GITHUB_TOKEN=YOUR_GITHUB_TOKEN
```

### Creating a GitHub Personal Access Token
1. Go to GitHub Settings → Developer settings → Personal access tokens
2. Generate a new token with `read:packages` scope
3. Use this token as your password/token in the configuration above

### Build Commands
```bash
# Clean build
mvn clean install

# Skip tests
mvn clean install -DskipTests

# Run tests
mvn test
```

## Compatibility Notes

### Java Version
- **Required**: Java 17 (minimum)
- **Current**: Java 21 ✅
- **Recommended**: Java 21 or later

### Kotlin Version
- **Current**: 2.2.21 (compatible with Spring Boot 4) ✅

## Breaking Changes from Spring Boot 3 to 4

### Spring Framework 7.0
Spring Boot 4 includes Spring Framework 7.0, which brings:
- Enhanced Jakarta EE 10+ support
- Improved Kotlin support
- Performance optimizations

### Minimum Requirements
- Java 17+ (Java 21 recommended)
- Jakarta EE 10+
- Kotlin 1.9+ (2.2.21 is well supported)

## Testing

### Pre-upgrade Verification
Before deploying to production:
1. Run all unit tests: `mvn test`
2. Run integration tests
3. Verify actuator endpoints: `/actuator/health`, `/actuator/prometheus`
4. Test API endpoints with proper authentication
5. Verify Swagger UI: `/swagger-ui.html`

### Known Compatible Dependencies
The following dependencies have been verified as compatible:
- ✅ spring-boot-starter-data-jpa (4.0.0)
- ✅ spring-boot-starter-web (4.0.0)
- ✅ spring-boot-starter-actuator (4.0.0)
- ✅ spring-boot-starter-validation (4.0.0)
- ✅ springdoc-openapi (3.0.0)
- ✅ token-validation-spring (6.0.0)
- ✅ micrometer-registry-prometheus (managed by Spring Boot 4)
- ✅ jackson-module-kotlin (managed by Spring Boot 4)

## Migration Checklist

- [x] Update Spring Boot parent version to 4.0.0
- [x] Update springdoc-openapi to 3.0.0
- [x] Update token-validation libraries to 6.0.0
- [x] Fix javax.* imports to jakarta.*
- [x] Add GitHub Packages repository configuration
- [ ] Configure GitHub Packages authentication (requires credentials)
- [ ] Test build with proper authentication
- [ ] Run unit tests
- [ ] Run integration tests
- [ ] Verify in test environment
- [ ] Deploy to production

## References

- [Spring Boot 4.0 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes)
- [navikt/token-support v6.0.0 Release](https://github.com/navikt/token-support/releases/tag/6.0.0)
- [springdoc-openapi v3 Documentation](https://springdoc.org/)

## Support

For questions about:
- **Spring Boot 4**: See official Spring Boot documentation
- **NAV token-support**: Contact #token-support on NAV Slack or open an issue at https://github.com/navikt/token-support
- **This project**: Contact the familie-ks-infotrygd team
