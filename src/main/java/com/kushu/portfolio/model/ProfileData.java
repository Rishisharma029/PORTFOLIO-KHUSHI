package com.kushu.portfolio.model;

import java.util.List;

/**
 * Model representing Khushi Singh's entire portfolio data.
 * Clean, structured, and easy for a BCA student or developer to customize.
 */
public class ProfileData {
    private String name;
    private String preferredTitle;
    private String subtitle;
    private String tagline;
    private String location;
    private String linkedinUrl;
    private String githubUrl;
    private String email;
    private String bio;
    private String supportingLine;
    private String quote;
    private String portraitPath;
    private Education education;
    private List<LearningMilestone> learningJourney;
    private List<SkillCategory> skillCategories;
    private List<Certification> certifications;
    private List<Project> projects;

    public ProfileData() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPreferredTitle() { return preferredTitle; }
    public void setPreferredTitle(String preferredTitle) { this.preferredTitle = preferredTitle; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }

    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getSupportingLine() { return supportingLine; }
    public void setSupportingLine(String supportingLine) { this.supportingLine = supportingLine; }

    public String getQuote() { return quote; }
    public void setQuote(String quote) { this.quote = quote; }

    public String getPortraitPath() { return portraitPath; }
    public void setPortraitPath(String portraitPath) { this.portraitPath = portraitPath; }

    public Education getEducation() { return education; }
    public void setEducation(Education education) { this.education = education; }

    public List<LearningMilestone> getLearningJourney() { return learningJourney; }
    public void setLearningJourney(List<LearningMilestone> learningJourney) { this.learningJourney = learningJourney; }

    public List<SkillCategory> getSkillCategories() { return skillCategories; }
    public void setSkillCategories(List<SkillCategory> skillCategories) { this.skillCategories = skillCategories; }

    public List<Certification> getCertifications() { return certifications; }
    public void setCertifications(List<Certification> certifications) { this.certifications = certifications; }

    public List<Project> getProjects() { return projects; }
    public void setProjects(List<Project> projects) { this.projects = projects; }

    // Nested Model Classes

    public static class Education {
        private String institution;
        private String degree;
        private String period;
        private String activities;
        private String logoSvg;

        public Education() {}

        public Education(String institution, String degree, String period, String activities, String logoSvg) {
            this.institution = institution;
            this.degree = degree;
            this.period = period;
            this.activities = activities;
            this.logoSvg = logoSvg;
        }

        public String getInstitution() { return institution; }
        public void setInstitution(String institution) { this.institution = institution; }

        public String getDegree() { return degree; }
        public void setDegree(String degree) { this.degree = degree; }

        public String getPeriod() { return period; }
        public void setPeriod(String period) { this.period = period; }

        public String getActivities() { return activities; }
        public void setActivities(String activities) { this.activities = activities; }

        public String getLogoSvg() { return logoSvg; }
        public void setLogoSvg(String logoSvg) { this.logoSvg = logoSvg; }
    }

    public static class SkillCategory {
        private String name;
        private List<SkillItem> skills;

        public SkillCategory() {}

        public SkillCategory(String name, List<SkillItem> skills) {
            this.name = name;
            this.skills = skills;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public List<SkillItem> getSkills() { return skills; }
        public void setSkills(List<SkillItem> skills) { this.skills = skills; }
    }

    public static class SkillItem {
        private String name;
        private String iconType;
        private String badgeColor;

        public SkillItem() {}

        public SkillItem(String name, String iconType, String badgeColor) {
            this.name = name;
            this.iconType = iconType;
            this.badgeColor = badgeColor;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getIconType() { return iconType; }
        public void setIconType(String iconType) { this.iconType = iconType; }

        public String getBadgeColor() { return badgeColor; }
        public void setBadgeColor(String badgeColor) { this.badgeColor = badgeColor; }
    }

    public static class Certification {
        private String id;
        private String title;
        private String issuer;
        private String date;
        private String credentialId;
        private String logoType;
        private String certUrl;
        private String description;
        private String skills;

        public Certification() {}

        public Certification(String id, String title, String issuer, String date, String credentialId, String logoType, String certUrl, String description, String skills) {
            this.id = id;
            this.title = title;
            this.issuer = issuer;
            this.date = date;
            this.credentialId = credentialId;
            this.logoType = logoType;
            this.certUrl = certUrl;
            this.description = description;
            this.skills = skills;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getIssuer() { return issuer; }
        public void setIssuer(String issuer) { this.issuer = issuer; }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }

        public String getCredentialId() { return credentialId; }
        public void setCredentialId(String credentialId) { this.credentialId = credentialId; }

        public String getLogoType() { return logoType; }
        public void setLogoType(String logoType) { this.logoType = logoType; }

        public String getCertUrl() { return certUrl; }
        public void setCertUrl(String certUrl) { this.certUrl = certUrl; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getSkills() { return skills; }
        public void setSkills(String skills) { this.skills = skills; }
    }

    public static class Project {
        private String id;
        private String title;
        private String description;
        private String fullDescription;
        private String techStack;
        private String category; // "PERSONAL" or "COLLAB"
        private String role; // Role title (e.g., "Full Stack Developer", "Backend & ML Contributor")
        private String contribution; // Khushi's actual specific contribution
        private String githubUrl;
        private String liveUrl;
        private String previewImage;
        private boolean comingSoon;
        private List<String> highlights;

        public Project() {}

        public Project(String id, String title, String description, String fullDescription, 
                       String techStack, String category, String role, String contribution, 
                       String githubUrl, String liveUrl, String previewImage, 
                       boolean comingSoon, List<String> highlights) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.fullDescription = fullDescription;
            this.techStack = techStack;
            this.category = category;
            this.role = role;
            this.contribution = contribution;
            this.githubUrl = githubUrl;
            this.liveUrl = liveUrl;
            this.previewImage = previewImage;
            this.comingSoon = comingSoon;
            this.highlights = highlights;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getFullDescription() { return fullDescription; }
        public void setFullDescription(String fullDescription) { this.fullDescription = fullDescription; }

        public String getTechStack() { return techStack; }
        public void setTechStack(String techStack) { this.techStack = techStack; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getContribution() { return contribution; }
        public void setContribution(String contribution) { this.contribution = contribution; }

        public String getGithubUrl() { return githubUrl; }
        public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }

        public String getLiveUrl() { return liveUrl; }
        public void setLiveUrl(String liveUrl) { this.liveUrl = liveUrl; }

        public String getPreviewImage() { return previewImage; }
        public void setPreviewImage(String previewImage) { this.previewImage = previewImage; }

        public boolean isComingSoon() { return comingSoon; }
        public void setComingSoon(boolean comingSoon) { this.comingSoon = comingSoon; }

        public List<String> getHighlights() { return highlights; }
        public void setHighlights(List<String> highlights) { this.highlights = highlights; }
    }

    public static class LearningMilestone {
        private String year;
        private String title;
        private String description;
        private String tag; // e.g. "MAIN", "CONFIRMED", "COLLAB CONTRIBUTION", "DRAFT — OWNER REVIEW"
        private String dotColor; // "white", "emerald", "amber", "blue"
        private boolean draft;
        private String reviewNote;

        public LearningMilestone() {}

        public LearningMilestone(String year, String title, String description, String tag, String dotColor) {
            this(year, title, description, tag, dotColor, false, null);
        }

        public LearningMilestone(String year, String title, String description, String tag, String dotColor, boolean draft, String reviewNote) {
            this.year = year;
            this.title = title;
            this.description = description;
            this.tag = tag;
            this.dotColor = dotColor;
            this.draft = draft;
            this.reviewNote = reviewNote;
        }

        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getTag() { return tag; }
        public void setTag(String tag) { this.tag = tag; }

        public String getDotColor() { return dotColor; }
        public void setDotColor(String dotColor) { this.dotColor = dotColor; }

        public boolean isDraft() { return draft; }
        public void setDraft(boolean draft) { this.draft = draft; }

        public String getReviewNote() { return reviewNote; }
        public void setReviewNote(String reviewNote) { this.reviewNote = reviewNote; }
    }
}
