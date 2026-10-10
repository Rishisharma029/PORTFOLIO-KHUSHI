package com.kushu.portfolio.controller;

import com.kushu.portfolio.service.PortfolioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeController.class)
@Import(PortfolioService.class)
public class StaticSiteExportTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void exportStaticSiteForGitHubPages() throws Exception {
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        String htmlContent = result.getResponse().getContentAsString();

        // Ensure relative asset paths so it works seamlessly on GitHub Pages subfolder (https://username.github.io/repo-name/)
        htmlContent = htmlContent.replace("href=\"/css/", "href=\"./css/")
                                 .replace("src=\"/js/", "src=\"./js/")
                                 .replace("src=\"/images/", "src=\"./images/")
                                 .replace("href=\"/images/", "href=\"./images/")
                                 .replace("content=\"/images/", "content=\"./images/")
                                 .replace("href=\"/favicon.ico\"", "href=\"./favicon.ico\"");

        Path distDir = Paths.get("dist");
        if (!Files.exists(distDir)) {
            Files.createDirectories(distDir);
        }

        // Write rendered index.html
        Path indexHtmlPath = distDir.resolve("index.html");
        Files.writeString(indexHtmlPath, htmlContent);

        // Copy static resources (css, js, images, favicon.ico)
        Path staticDir = Paths.get("src/main/resources/static");
        if (Files.exists(staticDir)) {
            Files.walkFileTree(staticDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetPath = distDir.resolve(staticDir.relativize(dir));
                    if (!Files.exists(targetPath)) {
                        Files.createDirectories(targetPath);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetPath = distDir.resolve(staticDir.relativize(file));
                    Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        // Create .nojekyll in dist so GitHub Pages processes underscores and static assets properly
        Files.writeString(distDir.resolve(".nojekyll"), "");

        System.out.println(">>> Successfully exported static site to " + distDir.toAbsolutePath());
    }
}
