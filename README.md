# GitHub Access Report

A Spring Boot application that generates comprehensive access reports for GitHub organizations. This service connects to GitHub's API, retrieves repository and collaborator information, and exposes RESTful endpoints to query user access across repositories.

## 📋 Overview

Organizations often need visibility into who has access to which repositories. This application automates that process by:
- Authenticating securely with GitHub using personal access tokens
- Fetching all repositories within an organization
- Determining which users have access to each repository
- Providing an aggregated view mapping users to their accessible repositories
- Supporting organizations with 100+ repositories and 1000+ users

## 🚀 Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven 3.6+** or use the included Maven wrapper
- **Git**
- A **GitHub Personal Access Token** with `read:org` and `repo` scopes
  - [Create a token here](https://github.com/settings/tokens/new)
  - Required permissions:
    - `read:org` - Read organization data
    - `repo` - Access repositories

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/SujalDongare-123/Github-Access-Report_Project-.git
   cd Github-Access-Report_Project-
   ```

2. **Configure GitHub credentials**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   # GitHub Configuration
   github.token=YOUR_GITHUB_PERSONAL_ACCESS_TOKEN
   github.organization=YOUR_ORGANIZATION_NAME
   github.api.base-url=https://api.github.com
   
   # Server Configuration
   server.port=8080
   ```

   **Alternative: Environment Variables**
   
   You can also set environment variables instead of modifying `application.properties`:
   ```bash
   export GITHUB_TOKEN=your_token_here
   export GITHUB_ORGANIZATION=your_org_name
   ```
   
   Then update `application.properties` to use:
   ```properties
   github.token=${GITHUB_TOKEN}
   github.organization=${GITHUB_ORGANIZATION}
   ```

3. **Build the project**
   ```bash
   mvn clean package
   ```
   
   Or using the Maven wrapper:
   ```bash
   ./mvnw clean package
   ```

### Running the Application

**Option 1: Maven**
```bash
mvn spring-boot:run
```

**Option 2: JAR file**
```bash
java -jar target/Github-Access-Report-0.0.1-SNAPSHOT.jar
```

**Option 3: Maven wrapper**
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

You can verify it's running by checking:
```bash
curl http://localhost:8080/actuator/health
```

## 🔐 Authentication Configuration

### GitHub Personal Access Token

This application uses **GitHub Personal Access Token (PAT)** for secure authentication.

#### Why PAT?
- **Better security**: Tokens can be revoked independently
- **Fine-grained permissions**: Control exactly what scopes the application has access to
- **Audit trail**: GitHub logs all API calls made with the token
- **Scalability**: Tokens have higher API rate limits than basic auth

#### How to Create a Token

1. Go to [GitHub Settings → Developer settings → Personal access tokens → Tokens (classic)](https://github.com/settings/tokens/new)
2. Click "Generate new token" → "Generate new token (classic)"
3. Name: `Github-Access-Report`
4. Select scopes:
   - ✅ `read:org` - Read organization and team membership
   - ✅ `repo` - Full control of private repositories
5. Click "Generate token"
6. **Copy the token immediately** (you won't see it again)

#### Token Security Best Practices

- ⚠️ **Never commit tokens** to version control
- 🔒 Use environment variables in production
- 🔄 Rotate tokens regularly (e.g., every 90 days)
- 🚫 Use `.gitignore` to exclude `application.properties` if it contains real tokens

#### Example `.gitignore` entry
```
# Exclude properties with sensitive data
src/main/resources/application-local.properties
.env
```

## 📡 API Endpoints

The application exposes three main REST API endpoints:

### 1. Get All Repositories

**Endpoint:** `GET /api/repositories`

**Description:** Retrieves all repositories in the configured GitHub organization.

**Response:**
```json
[
  {
    "id": 123456,
    "name": "repository-1",
    "description": "A sample repository",
    "url": "https://github.com/org/repository-1",
    "private": false
  },
  {
    "id": 123457,
    "name": "repository-2",
    "description": "Another repository",
    "url": "https://github.com/org/repository-2",
    "private": true
  }
]
```

**Example cURL:**
```bash
curl http://localhost:8080/api/repositories
```

---

### 2. Get Collaborators for a Repository

**Endpoint:** `GET /api/repositories/{repositoryName}/collaborators`

**Description:** Retrieves all collaborators (users with access) for a specific repository.

**Parameters:**
- `repositoryName` (path parameter): The name of the repository

**Response:**
```json
[
  {
    "login": "john-doe",
    "id": 987654,
    "avatarUrl": "https://avatars.githubusercontent.com/u/987654",
    "permissions": {
      "admin": false,
      "push": true,
      "pull": true
    }
  },
  {
    "login": "jane-smith",
    "id": 987655,
    "avatarUrl": "https://avatars.githubusercontent.com/u/987655",
    "permissions": {
      "admin": true,
      "push": true,
      "pull": true
    }
  }
]
```

**Example cURL:**
```bash
curl http://localhost:8080/api/repositories/my-repository/collaborators
```

---

### 3. Generate Access Report

**Endpoint:** `GET /api/report`

**Description:** Generates a comprehensive access report showing which repositories each user has access to, along with their permission levels.

**Response:**
```json
[
  {
    "repositoryName": "repo-1",
    "collaborator": "john-doe",
    "permission": "WRITE"
  },
  {
    "repositoryName": "repo-1",
    "collaborator": "jane-smith",
    "permission": "ADMIN"
  },
  {
    "repositoryName": "repo-2",
    "collaborator": "john-doe",
    "permission": "READ"
  },
  {
    "repositoryName": "repo-3",
    "collaborator": "jane-smith",
    "permission": "ADMIN"
  }
]
```

**Example cURL:**
```bash
curl http://localhost:8080/api/report
```

---

### Permission Levels

The report uses three permission levels:

| Permission | Meaning |
|-----------|---------|
| `ADMIN` | User has admin access (can manage repository settings) |
| `WRITE` | User has write/push access (can commit and push) |
| `READ` | User has read-only access (can view/pull only) |

## 🏗️ Architecture

### Project Structure

```
src/main/java/com/sujal/githubreport/
├── GithubAccessReportApplication.java    # Main Spring Boot application class
├── controller/
│   └── RepositoryController.java         # REST API endpoints
├── service/
│   └── GithubService.java               # Business logic for generating reports
├── client/
│   └── GithubClient.java                # GitHub API client
├── config/
│   └── RestClientConfig.java            # Spring RestClient configuration
└── dto/
    ├── ReportDTO.java                   # Access report DTO
    ├── RepositoryDTO.java               # Repository information DTO
    ├── CollaboratorDTO.java             # Collaborator information DTO
    └── PermissionDTO.java               # Permission details DTO
```

### Design Decisions

#### 1. **Layered Architecture**
- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic and orchestrates API calls
- **Client Layer**: Manages GitHub API communication
- **DTO Layer**: Data Transfer Objects for API responses

**Benefit**: Clear separation of concerns, easier to test and maintain

#### 2. **Spring RestClient over RestTemplate**
- Used modern `RestClient` (Spring Boot 3.0+) instead of deprecated `RestTemplate`
- **Advantage**: Type-safe, fluent API, better error handling

#### 3. **Dependency Injection**
- All components use constructor-based dependency injection
- **Advantage**: Loose coupling, easier testing with mock objects

#### 4. **Configuration via Properties**
- GitHub token and organization stored in `application.properties`
- Supports environment variable overrides for production deployments
- **Advantage**: Easy configuration management, secure credential handling

#### 5. **DTOs for API Communication**
- Uses dedicated DTOs to map GitHub API responses
- Provides clean, typed responses to clients
- **Advantage**: Decouples internal representation from external API

#### 6. **Efficient API Usage**
- Uses parallel processing capabilities where possible
- Pagination-aware API calls (GitHub API handles pagination)
- **Advantage**: Supports 100+ repositories and 1000+ users without performance issues

#### 7. **Permission Mapping**
- Maps GitHub's granular permissions (admin, push, pull) to three levels: ADMIN, WRITE, READ
- **Advantage**: Simplifies permission analysis for business users

## 📊 API Rate Limiting

GitHub API has rate limits:
- **Authenticated requests**: 5,000 requests per hour
- **Per repository**: Collaborators endpoint returns up to 30 results per page

**Considerations for Scale**:
- For 100 repositories: ~100-200 API calls (one per repo + pagination)
- For 1000+ users: Handled through pagination (30 users per page per repo)
- With 5,000 request/hour limit: Can handle multiple report generations

## 🛠️ Development

### Build
```bash
mvn clean package
```

### Run Tests
```bash
mvn test
```

### IDE Setup
1. Open project in IntelliJ IDEA or Eclipse
2. Right-click project → Maven → Update Project
3. Ensure Java 21 is selected as the project SDK

## 🔍 Assumptions

1. **GitHub Organization**: The application assumes a valid GitHub organization exists and the provided token has access to it
2. **Token Permissions**: The token must have `read:org` and `repo` scopes
3. **API Availability**: GitHub API is accessible and responding normally
4. **Organization Members**: Only users with explicit repository access are included in the report
5. **No Filtering**: The report includes all repositories and all collaborators without filtering
6. **Collaborator Definition**: In GitHub API, "collaborators" refers to users with explicit access through collaborator relationships (not org members by default)

## 🐛 Error Handling

The application handles the following scenarios:

- **Invalid Token**: Returns HTTP 401 Unauthorized
- **Repository Not Found**: Returns HTTP 404 Not Found
- **Rate Limit Exceeded**: Returns HTTP 403 Forbidden with rate limit headers
- **Network Errors**: Logs error and returns HTTP 500 Internal Server Error

## 📈 Future Enhancements

- [ ] Add caching layer to reduce API calls
- [ ] Implement report export (CSV, Excel)
- [ ] Add filtering and sorting capabilities
- [ ] Create historical reports to track access changes
- [ ] Add team-level access analysis
- [ ] Implement webhook support for real-time updates
- [ ] Add UI dashboard for visualization

## 📝 Dependencies

- **Spring Boot 4.0.8** - Web framework
- **Spring Web MVC** - REST controller support
- **Lombok** - Reduce boilerplate code
- **SpringDoc OpenAPI 3.0.2** - API documentation
- **Maven** - Build tool

See `pom.xml` for complete dependency list.

## 📄 License

This project is provided as-is for educational and commercial purposes.

## 👨‍💼 Author

**Sujal Dongare**
- GitHub: [@SujalDongare-123](https://github.com/SujalDongare-123)

## 📧 Support

For issues or questions:
1. Check existing GitHub Issues
2. Create a new GitHub Issue with detailed information
3. Include: error messages, steps to reproduce, and your environment

## 🙏 Acknowledgments

- GitHub API Documentation: https://docs.github.com/en/rest
- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Assignment provided by: [Company Name]

---

**Note**: This application is designed to run report queries on-demand. For high-frequency queries across large organizations, consider implementing caching mechanisms.
