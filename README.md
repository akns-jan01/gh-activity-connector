# GitHub Activity Connector

A Spring Boot-based tool that connects to the GitHub API to fetch public repositories and their recent commits for any GitHub user or organization.

It authenticates using a GitHub Personal Access Token and provides structured insights including commit message, author, and timestamp.

---

## ✨ Features

- 🔐 Authenticates with GitHub using a Personal Access Token  
- 🗃 Fetches all public repositories for a given GitHub username or organization  
- 🔁 Paginates through repositories (if more than 100)  
- 📜 Retrieves the **20 most recent commits** per repository  
- 🧾 Commit info includes message, author, and timestamp  
- 🚀 Exposes a REST endpoint to trigger the fetch  

---

## 🛠 Tech Stack

- Java 17+  
- Spring Boot 3.x  
- RestTemplate (blocking HTTP client)  
- Maven  

---

## 📦 Getting Started

### 1. Clone the Repository

\`\`\`bash
git clone https://github.com/your-username/gh-activity-connector.git
cd gh-activity-connector
\`\`\`

### 2. Configure GitHub Token

Create the file \`src/main/resources/application.properties\` with the following content:

\`\`\`properties
spring.application.name=gh-activity-connector
server.port=8080

github.token=ghp_your_personal_access_token
github.api.url=https://api.github.com
\`\`\`

> 💡 **Note:** Replace \`ghp_your_personal_access_token\` with your actual GitHub token.

### 3. Build and Run

\`\`\`bash
./mvnw spring-boot:run
\`\`\`

---

## 🧪 Usage

### REST Endpoint

Fetch recent activity by username:

\`\`\`
GET http://localhost:8080/github/activity/{username}
\`\`\`

### Example:

\`\`\`bash
curl http://localhost:8080/github/activity/torvalds
\`\`\`

### Sample JSON Response

\`\`\`json
[
  {
    "name": "linux",
    "fullName": "torvalds/linux",
    "commits": [
      {
        "message": "Merge tag 'mm-stable-2024-07-15-17-44'...",
        "author": "Linus Torvalds",
        "timestamp": "2024-07-15T17:44:11Z"
      }
    ]
  }
]
\`\`\`

---

## 🔒 Security & Best Practices

- Do **not** commit your GitHub token to version control.  
- Use environment variables or a secrets manager in production.  
- Handle GitHub API rate limits gracefully (currently basic error logging is in place). 