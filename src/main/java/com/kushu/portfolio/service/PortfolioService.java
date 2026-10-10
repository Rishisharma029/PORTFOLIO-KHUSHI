package com.kushu.portfolio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kushu.portfolio.model.ProfileData;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Service that supplies Khushi Singh's portfolio data.
 *
 * HOW TO UPDATE:
 * - All profile details, projects, milestones, skills, and certifications
 *   can be edited in ONE simple place: src/main/resources/portfolio-data.json
 * - This service automatically loads and parses that file.
 */
@Service
public class PortfolioService {

    private static final Logger log = LoggerFactory.getLogger(PortfolioService.class);
    private final ObjectMapper objectMapper;
    private ProfileData cachedProfile;

    public PortfolioService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        refreshData();
    }

    /**
     * Loads or reloads the portfolio data from src/main/resources/portfolio-data.json
     */
    public synchronized ProfileData refreshData() {
        try {
            ClassPathResource resource = new ClassPathResource("portfolio-data.json");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    cachedProfile = objectMapper.readValue(is, ProfileData.class);
                    log.info("Successfully loaded portfolio data from portfolio-data.json ({} projects, {} milestones, {} certs)",
                            cachedProfile.getProjects() != null ? cachedProfile.getProjects().size() : 0,
                            cachedProfile.getLearningJourney() != null ? cachedProfile.getLearningJourney().size() : 0,
                            cachedProfile.getCertifications() != null ? cachedProfile.getCertifications().size() : 0);
                    return cachedProfile;
                }
            } else {
                log.warn("portfolio-data.json not found on classpath, falling back to programmatic defaults.");
            }
        } catch (Exception e) {
            log.error("Failed to load portfolio-data.json: {}", e.getMessage(), e);
        }

        cachedProfile = createFallbackProfile();
        return cachedProfile;
    }

    public ProfileData getProfileData() {
        if (cachedProfile == null) {
            return refreshData();
        }
        return cachedProfile;
    }

    private ProfileData createFallbackProfile() {
        ProfileData profile = new ProfileData();

        // 1. Core Profile Details
        profile.setName("Khushi Singh");
        profile.setPreferredTitle("Java Full Stack Developer");
        profile.setSubtitle("BCA Student | Aspiring Java Full Stack Developer");
        profile.setTagline("Learning · Building · Growing");
        profile.setSupportingLine("Learning, building, and growing through code.");
        profile.setQuote("Turning ideas into meaningful projects through code.");
        profile.setLocation("Faridabad, Haryana, India");
        profile.setLinkedinUrl("https://www.linkedin.com/in/khushi-singh-0a9a9738a/");
        profile.setGithubUrl(null);
        profile.setEmail(null);
        profile.setPortraitPath("/images/khushi-portrait.jpg");

        // 2. About Bio
        profile.setBio("I am a BCA student with a strong interest in software development and programming. "
                + "I am currently working towards becoming a Java Full Stack Developer.\n\n"
                + "I have learned the basics of Java, Python, and C, and I am currently developing my skills "
                + "in web development, databases, Git/GitHub, and full-stack technologies.\n\n"
                + "I enjoy coding and building practical projects that solve real-world problems. "
                + "I am continuously learning new technologies and looking for opportunities to improve my technical and problem-solving skills.");

        // 3. Education
        ProfileData.Education education = new ProfileData.Education(
                "Satyug Darshan Institute of Engineering & Technology",
                "Bachelor of Computer Applications (BCA)",
                "Aug 2025 - Jun 2028",
                "Activities and societies: Coding, college projects, technical presentations, hackathons, and technology-related learning activities.",
                "sdiet-crest"
        );
        profile.setEducation(education);

        // 4. Learning Journey Roadmap (Reviewed Milestones & Verified Roles)
        List<ProfileData.LearningMilestone> journey = new ArrayList<>();

        journey.add(new ProfileData.LearningMilestone(
                "2025",
                "Started Coding & Web Basics",
                "Began journey in computer science, learning foundational programming concepts, HTML5, CSS3, and JavaScript.",
                "FOUNDATIONS",
                "white",
                false,
                null
        ));

        journey.add(new ProfileData.LearningMilestone(
                "2025",
                "Completed 12th Grade",
                "Graduated senior secondary education (CBSE Board) with focus on science and computers, choosing to pursue BCA for software development.",
                "ACADEMIC",
                "white",
                false,
                null
        ));

        journey.add(new ProfileData.LearningMilestone(
                "2025",
                "Enrolled in BCA at SDIET",
                "Enrolled in Bachelor of Computer Applications (BCA) at Satyug Darshan Institute of Engineering & Technology (SDIET), focusing on computer science, core programming, and algorithms.",
                "EDUCATION",
                "white",
                false,
                null
        ));

        // --- 2. Verified Java & Core Technical Milestones ---
        journey.add(new ProfileData.LearningMilestone(
                "2026",
                "Core Java & OOP Specialization",
                "Deepened knowledge in Core Java 21, Object-Oriented Programming (OOP), Collections, Multithreading, and Exception Handling through coursework and LetsUpgrade bootcamp certification.",
                "CORE SKILL",
                "emerald",
                false,
                null
        ));

        journey.add(new ProfileData.LearningMilestone(
                "2026",
                "Java Full Stack Developer Portfolio",
                "Designed and engineered personal developer portfolio application using Java 21, Spring Boot 3.3 MVC, Thymeleaf, and responsive design tokens.",
                "PERSONAL PROJECT",
                "emerald",
                false,
                null
        ));

        // --- 3. Collaborative Contributions (Team Projects — Lead Developer: Rishi Sharma) ---
        journey.add(new ProfileData.LearningMilestone(
                "2026",
                "CampusOS AI (Collaborative Contribution)",
                "Contributed to student portal frontend UI and gatepass workflow integration within the collaborative campus platform led by developer Rishi Sharma.",
                "COLLAB CONTRIBUTION",
                "blue",
                false,
                "Collaborative contribution to team repository (Lead: Rishi Sharma). Not Khushi's independent work."
        ));

        journey.add(new ProfileData.LearningMilestone(
                "2026",
                "EvalSync System (Collaborative Contribution)",
                "Contributed to teacher evaluation workflow forms and mark tabulation logic for the academic grading platform led by developer Rishi Sharma.",
                "COLLAB CONTRIBUTION",
                "blue",
                false,
                "Collaborative contribution to team repository (Lead: Rishi Sharma). Not Khushi's independent work."
        ));

        journey.add(new ProfileData.LearningMilestone(
                "2026",
                "Handwritten Digital Recognition (Collaborative Contribution)",
                "Assisted with image dataset preprocessing and validation testing for the handwritten OCR digitizer project led by developer Rishi Sharma.",
                "COLLAB CONTRIBUTION",
                "blue",
                false,
                "Collaborative contribution to team repository (Lead: Rishi Sharma). Invented ResNet/CTC claims removed."
        ));

        // --- 4. Unconfirmed Milestones (Marked as Drafts for Owner Review — Invented Claims Removed) ---
        journey.add(new ProfileData.LearningMilestone(
                "Draft",
                "Distributed Systems / Traffic Management Concept",
                "Exploratory study into networking and backend traffic distribution concepts. (Removed unverified L4/L7 production claims).",
                "DRAFT — OWNER REVIEW",
                "amber",
                true,
                "⚠️ Draft Milestone: Requires owner review and verification of actual learning scope before publishing."
        ));

        journey.add(new ProfileData.LearningMilestone(
                "Draft",
                "System Architecture Visualizer Concept",
                "Exploratory study into cloud architecture and automated diagram tooling.",
                "DRAFT — OWNER REVIEW",
                "amber",
                true,
                "⚠️ Draft Milestone: Unconfirmed entry marked as draft for owner review."
        ));

        journey.add(new ProfileData.LearningMilestone(
                "Draft",
                "Developer Code Inspection Tool Concept",
                "Exploratory study into static analysis, linter patterns, and developer productivity tooling.",
                "DRAFT — OWNER REVIEW",
                "amber",
                true,
                "⚠️ Draft Milestone: Unconfirmed entry marked as draft for owner review."
        ));

        journey.add(new ProfileData.LearningMilestone(
                "Draft",
                "AI Resume Analysis Concept",
                "Exploratory project concept for automated resume critique and scoring.",
                "DRAFT — OWNER REVIEW",
                "amber",
                true,
                "⚠️ Draft Milestone: Unconfirmed entry marked as draft for owner review."
        ));

        profile.setLearningJourney(journey);

        // 5. Skills Categorized (Java only for now)
        List<ProfileData.SkillCategory> categories = new ArrayList<>();

        // Java Core & Programming
        List<ProfileData.SkillItem> programming = new ArrayList<>();
        programming.add(new ProfileData.SkillItem("Java", "java", "#f89820"));
        categories.add(new ProfileData.SkillCategory("Programming Languages", programming));

        profile.setSkillCategories(categories);

        // 5. Certifications & Licenses (6 interactive items)
        List<ProfileData.Certification> certs = new ArrayList<>();
        certs.add(new ProfileData.Certification(
                "cert-ibm-ai",
                "Getting Started with Artificial Intelligence",
                "IBM SkillsBuild",
                "Sep 2026",
                null,
                "ibm",
                "https://skillsbuild.org/",
                "Foundations of Artificial Intelligence, Machine Learning fundamentals, ethical AI principles, neural networks, and modern prompt engineering practices.",
                "Artificial Intelligence • Machine Learning • Neural Networks • Prompt Engineering"
        ));
        certs.add(new ProfileData.Certification(
                "cert-java-bootcamp",
                "Java Bootcamp",
                "LetsUpgrade",
                "Apr 2026",
                "LUEJAVAMAR12641",
                "letsupgrade-java",
                "https://letsupgrade.in/verify?id=LUEJAVAMAR12641",
                "Comprehensive hands-on training covering Core Java, Object-Oriented Design, Collections Framework, Multithreading, Exception Handling, and building modular Java applications.",
                "Java 21 • Object-Oriented Programming (OOP) • Collections • Multithreading"
        ));
        certs.add(new ProfileData.Certification(
                "cert-python-bootcamp",
                "Python Bootcamp",
                "LetsUpgrade",
                "Apr 2026",
                "LUEPYTAPR12681",
                "letsupgrade-python",
                "https://letsupgrade.in/verify?id=LUEPYTAPR12681",
                "Intensive training in Python scripting, functional programming, data manipulation, file handling, and REST microservices development.",
                "Python 3 • Scripting • Data Structures • Backend Automation"
        ));
        certs.add(new ProfileData.Certification(
                "cert-design-matrix",
                "Design Matrix",
                "Satyug Darshan Institute of Engg. & Tech.",
                "Jan 2026",
                null,
                "sdiet",
                "https://satyug.edu.in/",
                "Inter-college design and technological innovation workshop focusing on modern UI/UX design systems, user journey mapping, wireframing, and component hierarchy.",
                "UI/UX Design • Design Systems • Wireframing • User Centered Design"
        ));
        certs.add(new ProfileData.Certification(
                "cert-german-1",
                "German-1",
                "NPTEL",
                "Apr 2026",
                "NPTEL26HS78S254602701",
                "nptel",
                "https://nptel.ac.in/noc/",
                "Introductory German Language and Cultural Competency certification covering German grammar, vocabulary, conversational skills, and technical communication.",
                "German A1 • Technical Communication • Cross-cultural Collaboration"
        ));
        certs.add(new ProfileData.Certification(
                "cert-ai-tools",
                "AI Tools Workshop",
                "United Latino Students Association",
                "Feb 2026",
                "0270772f-3809-4400-b29b-1e1c61cd09971081964",
                "ulsa",
                "https://www.unitedlatinos.org/",
                "Applied AI tools workshop focusing on modern generative AI tooling, developer productivity workflows, API integrations, and practical prompt architecture.",
                "Generative AI • AI Developer Tools • Workflow Automation • LLM APIs"
        ));
        profile.setCertifications(certs);

        // 6. Projects (Personal & Collaborative with Real Roles & Contributions)
        List<ProfileData.Project> projects = new ArrayList<>();

        // --- Personal Projects (Self / Personal Focus) ---
        projects.add(new ProfileData.Project(
                "proj-portfolio",
                "Khushi Singh — Official Developer Portfolio",
                "Robust server-rendered web application architecture built with Java 21, Spring Boot, MVC patterns, and custom responsive design tokens.",
                "Designed and built a high-performance personal developer portfolio website using Spring Boot 3.3.4 and Thymeleaf 3.x. Features a customized 100vh hero layout, interactive alternating timeline for her learning journey, dynamic project modals, and automated JUnit test verification without third-party heavy JS frameworks.",
                "Java 21 • Spring Boot 3.3.4 • Thymeleaf • HTML5/CSS3 • Maven",
                "PERSONAL",
                "Lead Developer & Architect",
                "Architected the entire backend application, engineered the Spring MVC controllers and service layer, handcrafted custom dark plum glassmorphism UI styles, and automated continuous test suites.",
                null,
                "http://localhost:8080",
                "/images/khushi-photo-crop.jpg",
                false,
                List.of("Java 21 & Spring Boot 3.3.4 MVC backend", "Thymeleaf 3 server-side HTML rendering", "Custom plum & radiant violet bento design tokens", "Zero bloated client frameworks — 100% clean vanilla JS")
        ));

        projects.add(new ProfileData.Project(
                "proj-java-oop",
                "Java OOP & Data Structures Suite",
                "Structured collection of object-oriented architecture patterns, custom collections, and algorithmic problem-solving implementations.",
                "A comprehensive exploration of core Java programming paradigms: abstract factory architectures, inheritance hierarchies, custom generic data structures, search & sorting algorithms, and multi-threaded file processors.",
                "Java 21 • OOP Architecture • Data Structures • Collections",
                "PERSONAL",
                "Independent Developer",
                "Authored clean, documented Java classes modeling real-world domain objects, unit-tested core algorithms, and implemented custom exception handlers.",
                null,
                null,
                null,
                false,
                List.of("Encapsulation, Polymorphism & Abstraction patterns", "Custom generic Collections & Map implementations", "Algorithmic sorting & graph traversal benchmarks", "JUnit 5 test coverage for core logic")
        ));

        // --- Collaborative Projects (Collab with clearly labeled roles) ---
        projects.add(new ProfileData.Project(
                "proj-campusos",
                "CampusOS AI",
                "AI-powered university operating system covering campus operations, administration, student analytics, and AI assistants.",
                "Comprehensive campus governance portal managing real-time student profiling, automated gatepass approval workflows, attendance tracking, and AI-driven administrative helpdesks.",
                "React • TypeScript • FastAPI • Tailwind CSS",
                "COLLAB",
                "Frontend & Service Integration Contributor",
                "Collaborated on student dashboard UI components, gatepass request state machines, and FastAPI backend integration endpoints.",
                "https://github.com/Rishisharma029/campusOS",
                "https://github.com/Rishisharma029/campusOS",
                null,
                false,
                List.of("Real-time student profiling & attendance trackers", "Automated digital gatepass generation & approval", "FastAPI microservices integration", "Responsive role-based access control UI")
        ));

        projects.add(new ProfileData.Project(
                "proj-careergraph",
                "CareerGraph AI",
                "Transforms career documents into an interactive knowledge graph, career timeline, AI search experience, and resume insights.",
                "An intelligent career analysis platform that ingests unstructured resumes and portfolios, parsing skills and milestones into an interactive D3/knowledge graph visualization with semantic RAG search.",
                "React • TypeScript • AI/RAG • Knowledge Graph",
                "COLLAB",
                "Knowledge Graph UI & Timeline Contributor",
                "Contributed to the interactive career timeline visualizer, skill graph node layouts, and testing semantic search response accuracy.",
                "https://github.com/Rishisharma029/careergraph-ai",
                "https://rishisharma029.github.io/careergraph-ai/",
                null,
                false,
                List.of("Dynamic graph visualization of skills & career history", "Semantic retrieval-augmented generation (RAG) query engine", "Interactive timeline milestone explorer", "Actionable resume scoring & gap analysis")
        ));

        projects.add(new ProfileData.Project(
                "proj-ocr",
                "Handwritten Digital Recognition System",
                "An OCR platform for converting handwritten notes and documents into editable, searchable digital text.",
                "End-to-end computer vision and OCR pipeline combining text bounding-box detection with deep learning character recognizers (TrOCR & EasyOCR) to digitize handwritten student notes and exams.",
                "Python • FastAPI • React • TrOCR • EasyOCR",
                "COLLAB",
                "ML Data Preprocessing & Validation Contributor",
                "Assisted in dataset preprocessing, image normalization pipelines, bounding-box accuracy evaluation, and frontend upload flow integration.",
                "https://github.com/Rishisharma029/handwritten-digital-recognition-system",
                "https://rishisharma029.github.io/handwritten-digital-recognition-system/",
                null,
                false,
                List.of("Multi-engine OCR pipeline with TrOCR transformer models", "FastAPI asynchronous batch image inference", "Interactive side-by-side text editor & PDF exporter", "High-accuracy handwriting transcription benchmark")
        ));

        projects.add(new ProfileData.Project(
                "proj-evalsync",
                "EvalSync-System",
                "Academic evaluation and CBSE paper-checking platform with grading workflows, analytics, and student records.",
                "Secure, synchronized evaluation ecosystem designed for educational institutions to coordinate multi-evaluator paper checking, mark tabulation, grade sheet exports, and discrepancy flags.",
                "React • Node.js • Express • MongoDB",
                "COLLAB",
                "Full Stack Workflow Contributor",
                "Contributed to teacher evaluation workflow forms, mark tallying calculations, student record schemas in MongoDB, and PDF report generation.",
                "https://github.com/Rishisharma029/EvalSync-System",
                null,
                null,
                false,
                List.of("Role-based grading dashboards for teachers & head evaluators", "Automated marksheet aggregation and discrepancy alert system", "MongoDB flexible schema for dynamic subject criteria", "Exportable audit logs & CBSE compliance tracking")
        ));

        projects.add(new ProfileData.Project(
                "proj-resqnet",
                "ResQNet",
                "Emergency-response platform for incident management, volunteer dispatch, GIS mapping, and shelter tracking.",
                "Real-time disaster management dispatch network facilitating emergency SOS broadcasts, automated nearest-responder routing, live GIS hazard overlays, and shelter capacity monitoring.",
                "React • Node.js • GIS • Socket.IO",
                "COLLAB",
                "GIS Mapping & Realtime Dispatch Contributor",
                "Contributed to incident logging forms, Mapbox GIS layer overlays, live volunteer beacon tracking, and Socket.IO broadcast handlers.",
                "https://github.com/Rishisharma029/resqnet",
                null,
                null,
                false,
                List.of("Live bidirectional SOS alerting via WebSockets", "Geospatial hazard mapping with safe shelter radii", "Volunteer dispatch coordination & status dashboard", "Offline-first incident caching and sync")
        ));

        projects.add(new ProfileData.Project(
                "proj-teachercheck",
                "Teacher-Check",
                "Foundational-learning assessment and academic checking project.",
                "Educational monitoring platform enabling standardized assessment checking, learning progress rubrics, and diagnostic teacher feedback.",
                "Python • Flask • React • EdTech",
                "COLLAB",
                "Academic Assessment Platform Contributor",
                "Contributed to assessment rubric scoring forms, student benchmark metrics, and automated reporting views.",
                "https://github.com/Rishisharma029/teacher-check",
                null,
                null,
                false,
                List.of("Standardized rubric scoring logic", "Classroom analytics dashboard", "Student milestone mastery charts")
        ));

        projects.add(new ProfileData.Project(
                "proj-bhoomilens",
                "BhoomiLens",
                "Land analysis, geospatial satellite & imagery exploration platform.",
                "Geospatial satellite imagery processing interface for agricultural boundary mapping, land cover classification, and terrain change detection.",
                "Geospatial GIS • Python • Satellite Data",
                "COLLAB",
                "Geospatial Interface Contributor",
                "Assisted in satellite image layer controls, polygon selection tools, and UI visualization for terrain classification.",
                "https://github.com/Rishisharma029/bhoomilens",
                null,
                null,
                false,
                List.of("Multispectral satellite layer rendering", "Polygon demarcation and area calculation", "Land classification visual explorer")
        ));

        projects.add(new ProfileData.Project(
                "proj-aiworkshop",
                "AI Engineering Workshop",
                "AI engineering workshop repository covering LLMs, generative agents, and practical AI implementations.",
                "Collection of hands-on experimental implementations exploring prompt engineering frameworks, autonomous LLM tool-calling agents, and vector embeddings.",
                "Python • AI / ML • LLMs",
                "COLLAB",
                "Workshop Researcher & Contributor",
                "Participated in agentic workflow design, tested prompt evaluation benchmarks, and documented LLM reasoning patterns.",
                "https://github.com/Rishisharma029/ai-engineering-workshop",
                null,
                null,
                false,
                List.of("Autonomous tool-calling agents implementation", "RAG vector retrieval benchmarks", "Structured JSON output extraction pipelines")
        ));

        projects.add(new ProfileData.Project(
                "proj-travia",
                "TRAVIA",
                "AI-powered smart tourism ecosystem with travel discovery and location-based features.",
                "Smart tourism application providing personalized itinerary recommendations, localized cultural highlights, and multi-city tour planning.",
                "AI • Smart Tourism • React • Maps API",
                "COLLAB",
                "Smart Tourism UI Contributor",
                "Collaborated on destination discovery search filters, itinerary timeline builders, and location mapping UI.",
                null,
                null,
                null,
                true,
                List.of("AI personalized itinerary generation", "Interactive map route plotting", "Local event & culture curation")
        ));

        projects.add(new ProfileData.Project(
                "proj-leafsense",
                "LeafSense AI",
                "Crop disease detection using a MobileNetV2-based image classification approach, with symptom analysis and treatment guidance.",
                "Edge-compatible mobile plant pathology platform that diagnoses crop leaf diseases from photos and recommends organic or chemical remedies.",
                "MobileNetV2 • Deep Learning • Vision • Python",
                "COLLAB",
                "Classification & Dataset Contributor",
                "Participated in leaf disease dataset curation, MobileNetV2 validation testing, and treatment remedy data mapping.",
                null,
                null,
                null,
                true,
                List.of("MobileNetV2 lightweight vision model", "Offline crop disease diagnostic classifier", "Actionable treatment and prevention guides")
        ));

        projects.add(new ProfileData.Project(
                "proj-solarflare",
                "SolarFlare-AI",
                "Solar-flare forecasting research project using solar observations and machine-learning models.",
                "Scientific machine-learning research system analyzing solar magnetic field indices to forecast extreme space weather and solar flare eruptions.",
                "Python • ML • Scientific Computing • Pandas",
                "COLLAB",
                "Scientific Data Modeling Contributor",
                "Assisted in astronomical time-series dataset cleaning, feature correlation analysis, and model accuracy benchmarking.",
                null,
                null,
                null,
                true,
                List.of("Time-series solar magnetogram analysis", "Predictive classification of M/X class flares", "Validation against historical heliophysics observations")
        ));

        projects.add(new ProfileData.Project(
                "proj-w3a1",
                "W3A1",
                "Blockchain-enabled service marketplace with provider workflows and a self-healing application concept.",
                "Decentralized service marketplace leveraging smart contracts for milestone-escrow agreements, decentralized identity, and resilient service discovery.",
                "Web3 • Blockchain • Distributed Systems • React",
                "COLLAB",
                "Decentralized Workflow Contributor",
                "Contributed to provider workflow specifications, smart contract interface testing, and frontend wallet interaction views.",
                null,
                null,
                null,
                true,
                List.of("Smart-contract escrow payment milestones", "Decentralized service provider registry", "Self-healing distributed application design")
        ));

        profile.setProjects(projects);

        return profile;
    }
}
