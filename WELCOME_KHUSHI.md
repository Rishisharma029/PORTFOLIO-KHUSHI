# 💜 Welcome to Your Portfolio Codebase, Khushi!

Congratulations! This entire project is now **100% yours**. 

You own the complete source code, the design, the data, and every single line of configuration. You are free to modify it, expand it, rewrite parts of it, or use it as a sandbox to learn and experiment with Java, Spring Boot, and full-stack development.

---

## 🧭 3-Minute Quick Start Guide

### Step 1: Install Prerequisites
To run this application on your computer, you only need one thing:
- **Java 21 (JDK 21)** or higher ([Download JDK 21 from Adoptium / Temurin](https://adoptium.net/temurin/releases/)).

*(You do **not** need to install Maven separately — the project includes the `./mvnw` wrapper).*

---

### Step 2: Run Your Website Locally
Open your terminal (PowerShell, Command Prompt, or VS Code Terminal) inside this project folder and type:

```powershell
.\mvnw.cmd spring-boot:run
```

*(On Mac or Linux, use `./mvnw spring-boot:run`)*

Once you see `Started PortfolioApplication in X.XX seconds`, open your web browser and go to:
👉 **[http://localhost:8080](http://localhost:8080)**

---

### Step 3: Edit Your Content (No HTML Required!)
All your data (bio, skills, projects, certifications, and learning journey milestones) is stored in **one single file**:
```
src/main/resources/portfolio-data.json
```

Whenever you want to add a new project, update a skill, or change your contact links, simply open `portfolio-data.json`, update the text, and refresh your browser! 

*(For copy-paste examples of adding new projects and certifications, check [PORTFOLIO_GUIDE.md](PORTFOLIO_GUIDE.md)).*

---

## 🚀 How to Put This on Your Own GitHub Account

To take full ownership and make this repository appear under your personal GitHub profile:

1. **Create a new repository on GitHub**:
   - Go to [GitHub](https://github.com) -> Click the `+` icon -> **New repository**.
   - Name it `khushi-portfolio` or `portfolio`.
   - Leave it empty (do **not** check "Initialize with README").

2. **Connect this project to your new GitHub repository**:
   Open PowerShell in this folder and run:
   ```bash
   git remote remove origin
   git remote add origin https://github.com/YOUR-GITHUB-USERNAME/YOUR-REPO-NAME.git
   git branch -M main
   git push -u origin main
   ```

3. Now all your code is safely saved on your personal GitHub!

---

## 🌐 How to Deploy Your Portfolio Live on the Internet (Free)

We have pre-configured **automatic GitHub Pages deployment** using GitHub Actions! You don't need to create accounts on third-party hosting services.

### 🌟 Option A: GitHub Pages (Automatic & 100% Free Forever)
Whenever you push your code or edit `portfolio-data.json` on GitHub, our automated workflow compiles the site and publishes it to GitHub Pages automatically.

1. On your GitHub repository page, click **Settings** (top right tab).
2. On the left sidebar, click **Pages**.
3. Under **Build and deployment** -> **Source**, select:
   👉 **GitHub Actions**
4. That's it! Every time you `git push` to `main`, GitHub will automatically build your portfolio and host it at:
   👉 `https://YOUR-GITHUB-USERNAME.github.io/YOUR-REPO-NAME/`

---

### Option B: Render or Railway (If you want a live Spring Boot backend server)
If you later add dynamic server features (like a live MySQL database or custom backend REST APIs):
- **Render.com**: Connect your GitHub repo, select Java, Build: `./mvnw clean package -DskipTests`, Run: `java -jar target/kushu-portfolio-0.0.1-SNAPSHOT.jar`.
- **Railway.app**: Connect your GitHub repo and click deploy.
1. Sign up at [Railway.app](https://railway.app) with GitHub.
2. Click **New Project** -> **Deploy from GitHub repo**.
3. Railway automatically detects Spring Boot and deploys it in under 2 minutes.

---

## 🛠️ Project Structure Map (Where Everything Lives)

```text
kushu-portfolio/
│
├── src/main/resources/
│   ├── portfolio-data.json          ⭐ YOUR CONTENT: Edit your bio, skills, and projects here
│   ├── templates/
│   │   └── index.html               🎨 HTML TEMPLATE: Thymeleaf markup that displays your data
│   └── static/
│       ├── css/style.css            💄 STYLES: Colors, layouts, animations, and dark plum theme
│       ├── js/main.js               ✨ SCRIPTS: Mobile menu, project filters, and Easter egg
│       └── images/                  🖼️ IMAGES: Your photos, logos, favicons, and social previews
│
├── src/main/java/com/kushu/portfolio/
│   ├── PortfolioApplication.java    🚀 SPRING BOOT: Main Java application entrypoint
│   ├── controller/HomeController.java  🎮 CONTROLLER: Sends data to the web page
│   ├── model/ProfileData.java       📦 DATA MODEL: Java classes representing your profile
│   └── service/PortfolioService.java ⚙️ SERVICE: Reads portfolio-data.json into Java
│
├── PORTFOLIO_GUIDE.md               📖 Quick copy-paste snippet guide for editing data
└── README.md                        📘 Full technical documentation and system architecture
```

---

## 💡 Ideas for Future Learning & Enhancements

As you continue your journey as a Java Full Stack Developer, here are exciting features you can build into this portfolio:

1. **Add a Working Contact Form with Spring Boot & Email / Database**:
   - Create a `@PostMapping("/contact")` in `HomeController.java`.
   - Save visitor messages into an H2 / MySQL database with Spring Data JPA.
   - Or send an automated email notification to your inbox using `spring-boot-starter-mail`.

2. **Add a Live Blog / Articles Section**:
   - Write articles about Java concepts you are learning (e.g. Streams, Multithreading, Spring Boot REST APIs).
   - Render them dynamically with Thymeleaf.

3. **Create REST API Endpoints**:
   - Add a `@RestController` at `/api/v1/projects` so other developers can consume your portfolio data via JSON.

4. **Add Spring Security / Admin Dashboard**:
   - Build a password-protected `/admin` page where you can edit your portfolio data directly from a web UI!

---

## 💜 A Parting Note

Every senior developer was once a beginner. The best way to learn is to break things, experiment, build projects you love, and never stop being curious.

*Keep learning. Keep building. Your journey is just getting started.* ✦
