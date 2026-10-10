# 🌸 Khushi Singh's Portfolio — Easy Customization & Owner Guide

Welcome! This entire portfolio is built so that **you can easily update all your content in one single file** without touching complex HTML or backend code.

---

## 📁 The Only File You Need to Edit

All your profile details, skills, projects, milestones, and certifications live in:
```
src/main/resources/portfolio-data.json
```

Whenever you add or change anything in this file, your website updates automatically across the entire site (hero section, cards, interactive modals, timeline, and footer).

---

## 🚀 Quick Reference: How to Update Everything

### 1. Update Profile & Contact Details
Open `portfolio-data.json` and change:
```json
"name": "Khushi Singh",
"preferredTitle": "Java Full Stack Developer",
"subtitle": "BCA Student | Aspiring Java Full Stack Developer",
"tagline": "Learning · Building · Growing",
"quote": "Turning ideas into meaningful projects through code.",
"location": "Faridabad, Haryana, India",
"linkedinUrl": "https://www.linkedin.com/in/khushi-singh-0a9a9738a/",
"githubUrl": "https://github.com/your-username",
"email": "khushi@example.com"
```

---

### 2. Add a New Project
In `portfolio-data.json`, find the `"projects"` array and add a new item:

```json
{
  "id": "proj-my-new-app",
  "title": "My Awesome Project Name",
  "description": "Short 1-line summary shown on the card.",
  "fullDescription": "Detailed overview of the project architecture and what it does.",
  "techStack": "Java 21 • Spring Boot • PostgreSQL • React",
  "category": "PERSONAL", 
  "role": "Lead Developer",
  "contribution": "Explain what you built and designed here.",
  "githubUrl": "https://github.com/your-username/my-project",
  "liveUrl": "https://my-live-demo.com",
  "previewImage": null,
  "comingSoon": false,
  "highlights": [
    "High-performance REST API endpoints",
    "Clean layered Spring MVC architecture",
    "Responsive modern user interface"
  ]
}
```

> **Note on Categories:**
> - Set `"category": "PERSONAL"` for solo projects you designed and built.
> - Set `"category": "COLLAB"` for group/team projects where you contributed.

---

### 3. Add or Update Learning Journey Milestones
In `portfolio-data.json`, find the `"learningJourney"` array and add or edit milestones:

```json
{
  "year": "2026",
  "title": "Full-Stack Project Launch",
  "description": "Built and deployed my first full-stack application using Spring Boot.",
  "tag": "MILESTONE",
  "dotColor": "emerald",
  "draft": false,
  "reviewNote": null
}
```

> **Dot Color Options:**
> - `"emerald"`: Green glowing dot (for core accomplishments & personal builds)
> - `"blue"`: Cyan/blue glowing dot (for collaborative team contributions)
> - `"white"`: White glowing dot (for academic & early foundations)
> - `"amber"`: Yellow/amber dot (for drafts under review)

---

### 4. Add a New Certification
In `portfolio-data.json`, find the `"certifications"` array and add:

```json
{
  "id": "cert-aws-cloud",
  "title": "AWS Certified Cloud Practitioner",
  "issuer": "Amazon Web Services",
  "date": "May 2026",
  "credentialId": "AWS-123456789",
  "logoType": "aws",
  "certUrl": "https://aws.amazon.com/verification",
  "description": "Cloud computing essentials, security, core services, and deployment models.",
  "skills": "Cloud Computing • AWS Services • Architecture • Security"
}
```

---

### 5. Add or Update Skills
In `portfolio-data.json`, update `"skillCategories"`:

```json
{
  "name": "Programming Languages",
  "skills": [
    {
      "name": "Java (Core & OOP)",
      "iconType": "java",
      "badgeColor": "#f89820"
    }
  ]
}
```

---

## 💻 How to Run & Preview Locally

Open a terminal in this folder and run:
```bash
.\mvnw.cmd spring-boot:run
```

Open your browser at:
```
http://localhost:8080
```

---

## 🚀 1-Click Free Deployment with GitHub Pages

This portfolio includes an **automated GitHub Actions workflow** (`.github/workflows/deploy.yml`):

1. Go to your GitHub repository -> **Settings** -> **Pages**.
2. Under **Build and deployment > Source**, choose **GitHub Actions**.
3. Every time you push changes to `portfolio-data.json`, GitHub automatically deploys your live site at:
   `https://<your-username>.github.io/<repo-name>/`
4. Want to export static HTML files locally? Just run `.\export-static.bat` (or `./export-static.sh` on Mac/Linux) and check the `./dist` folder!

---

## 🛠️ Tech Stack Overview
- **Backend:** Java 21, Spring Boot 3.3.4, Spring MVC, Jackson
- **Frontend:** Thymeleaf 3, HTML5, Vanilla CSS3 (Custom Glassmorphic Tokens), Vanilla JS
- **Build Tool:** Apache Maven (`./mvnw`)
- **Tests:** JUnit 5, Spring Boot Test (`.\mvnw.cmd test`)
