/**
 * Khushi Singh — Portfolio JavaScript
 * Responsive navigation, active link tracking, project tab filtering, clipboard share & toasts.
 */

document.addEventListener('DOMContentLoaded', () => {
    initContentProtection();
    loadObfuscatedAssets();
    initNavigation();
    initScrollReveals();
    initProjectTabs();
    initProjectModal();
    initCertModal();
    initThankYouModal();
    initShareButton();
    initResumeButton();
    initEasterEgg();
});

/**
 * Loads obfuscated theme assets (portraits/graphics) without exposing raw image files
 */
async function loadObfuscatedAssets() {
    const heroImg = document.getElementById('heroPortraitImg');
    const aboutImg = document.getElementById('aboutPortraitImg');
    if (!heroImg && !aboutImg) return;

    try {
        // Resolve data path relative to current URL for both local and GitHub Pages hosting
        const dataUrl = new URL('data/theme-assets.dat', window.location.href.split('?')[0].replace(/[^/]*$/, '')).href;
        const res = await fetch(dataUrl);
        if (!res.ok) return;

        const buffer = await res.arrayBuffer();
        const bytes = new Uint8Array(buffer);
        const key = [75, 104, 117, 115, 104, 105, 80, 111, 114, 116, 102, 111, 108, 105, 111, 65, 115, 115, 101, 116, 86, 97, 117, 108, 116, 50, 48, 50, 54];
        
        for (let i = 0; i < bytes.length; i++) {
            bytes[i] ^= key[i % key.length];
        }

        const decodedText = new TextDecoder('utf-8').decode(bytes);
        const vault = JSON.parse(decodedText);

        if (heroImg && vault.hero) {
            heroImg.src = vault.hero;
            heroImg.classList.add('loaded');
        }
        if (aboutImg && vault.about) {
            aboutImg.src = vault.about;
            aboutImg.classList.add('loaded');
        }
    } catch (err) {
        console.debug('Asset vault load skipped or error:', err);
    }
}

/**
 * Navigation Bar & Active Section Tracker
 */
function initNavigation() {
    const toggleBtn = document.getElementById('navToggleBtn');
    const linksWrap = document.getElementById('navLinksWrap');
    const navItems = document.querySelectorAll('.nav-item');
    const sections = document.querySelectorAll('header[id], section[id]');

    if (toggleBtn && linksWrap) {
        toggleBtn.addEventListener('click', () => {
            const isOpened = linksWrap.classList.toggle('open');
            toggleBtn.setAttribute('aria-expanded', isOpened);
        });

        // Close on link click
        navItems.forEach(item => {
            item.addEventListener('click', () => {
                linksWrap.classList.remove('open');
                toggleBtn.setAttribute('aria-expanded', 'false');
            });
        });

        // Close on outside click
        document.addEventListener('click', (e) => {
            if (!linksWrap.contains(e.target) && !toggleBtn.contains(e.target)) {
                linksWrap.classList.remove('open');
                toggleBtn.setAttribute('aria-expanded', 'false');
            }
        });
    }

    // Scroll spy for active navbar state
    window.addEventListener('scroll', () => {
        let activeId = '';
        const scrollPosition = window.scrollY + 180;

        sections.forEach(sec => {
            const top = sec.offsetTop;
            const height = sec.offsetHeight;
            if (scrollPosition >= top && scrollPosition < top + height) {
                activeId = sec.getAttribute('id');
            }
        });

        navItems.forEach(item => {
            item.classList.remove('active');
            if (item.getAttribute('href') === `#${activeId}`) {
                item.classList.add('active');
            }
        });
    }, { passive: true });
}

/**
 * Project Tabs Filtering (Personal / Collab / All)
 */
function initProjectTabs() {
    const tabButtons = document.querySelectorAll('.proj-tab-btn');
    const projectCards = document.querySelectorAll('.project-item-card');

    if (!tabButtons.length || !projectCards.length) return;

    // Filter projects based on category
    function filterCategory(category) {
        projectCards.forEach(card => {
            const cardCat = card.getAttribute('data-category');
            if (category === 'all') {
                card.classList.remove('hidden');
            } else if (category === 'personal' && cardCat === 'PERSONAL') {
                card.classList.remove('hidden');
            } else if (category === 'collab' && cardCat === 'COLLAB') {
                card.classList.remove('hidden');
            } else {
                card.classList.add('hidden');
            }
        });
    }

    tabButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            tabButtons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            const targetTab = btn.getAttribute('data-tab');
            filterCategory(targetTab);
        });
    });

    // Default to 'collab' initially (or whatever tab is marked active)
    const initialActive = document.querySelector('.proj-tab-btn.active');
    if (initialActive) {
        filterCategory(initialActive.getAttribute('data-tab'));
    }
}

/**
 * Toast Notification System
 */
function triggerToast(message, duration = 3000) {
    const toast = document.getElementById('toastAlert');
    if (!toast) return;

    toast.textContent = message;
    toast.classList.add('show');

    setTimeout(() => {
        toast.classList.remove('show');
    }, duration);
}

/**
 * Share / Copy Profile Link (Native Web Share on Mobile, Clipboard on Desktop)
 */
function initShareButton() {
    const shareBtn = document.getElementById('shareBtn');
    if (!shareBtn) return;

    shareBtn.addEventListener('click', async () => {
        const shareData = {
            title: 'Khushi Singh — Java Full Stack Developer',
            text: 'Check out Khushi Singh’s Developer Portfolio!',
            url: window.location.href
        };

        // Mobile Native Share Sheet (WhatsApp, LinkedIn, AirDrop, Messages, etc.)
        if (navigator.share) {
            try {
                await navigator.share(shareData);
                return;
            } catch (err) {
                if (err.name === 'AbortError') return; // User canceled share dialog
            }
        }

        // Desktop Clipboard Fallback
        if (navigator.clipboard) {
            try {
                await navigator.clipboard.writeText(window.location.href);
                triggerToast('✨ Link copied to clipboard!');
            } catch (e) {
                triggerToast('✨ Portfolio: ' + window.location.href);
            }
        } else {
            triggerToast('✨ Portfolio: ' + window.location.href);
        }
    });
}

/**
 * Download Resume Button Alert
 */
function initResumeButton() {
    const resumeBtn = document.getElementById('resumeActionBtn');
    if (!resumeBtn) return;

    resumeBtn.addEventListener('click', () => {
        triggerToast('📄 Opening Khushi Singh’s LinkedIn profile...');
    });
}

/**
 * Interactive Project Details Modal System
 */
function initProjectModal() {
    const modal = document.getElementById('projectModal');
    const closeBtn = document.getElementById('modalCloseBtn');
    const dismissBtn = document.getElementById('modalDismissBtn');
    const projectCards = document.querySelectorAll('.clickable-project-card');

    if (!modal || !projectCards.length) return;

    // Elements inside modal
    const catBadge = document.getElementById('modalCategoryBadge');
    const statusBadge = document.getElementById('modalStatusBadge');
    const titleElem = document.getElementById('modalTitle');
    const techElem = document.getElementById('modalTechStack');
    const roleElem = document.getElementById('modalRole');
    const contribElem = document.getElementById('modalContribution');
    const descElem = document.getElementById('modalDescription');
    const highlightsSection = document.getElementById('modalHighlightsSection');
    const highlightsList = document.getElementById('modalHighlightsList');
    const githubBtn = document.getElementById('modalGithubBtn');
    const liveBtn = document.getElementById('modalLiveBtn');

    function openModal(card) {
        const title = card.getAttribute('data-title') || 'Project Details';
        const category = card.getAttribute('data-category') || 'COLLAB';
        const role = card.getAttribute('data-role') || 'Developer & Collaborator';
        const contribution = card.getAttribute('data-contribution') || '';
        const description = card.getAttribute('data-description') || '';
        const tech = card.getAttribute('data-tech') || '';
        const githubUrl = card.getAttribute('data-github') || '';
        const liveUrl = card.getAttribute('data-live') || '';
        const comingSoon = card.getAttribute('data-coming') === 'true';

        // Set Badges
        if (catBadge) {
            if (category === 'PERSONAL') {
                catBadge.textContent = '🌟 Personal Project';
                catBadge.className = 'modal-category-badge badge-personal';
            } else {
                catBadge.textContent = '🤝 Collaborative Project';
                catBadge.className = 'modal-category-badge badge-collab';
            }
        }

        if (statusBadge) {
            if (comingSoon) {
                statusBadge.textContent = 'In Progress';
                statusBadge.className = 'modal-status-badge badge-inprogress';
            } else {
                statusBadge.textContent = 'Active Showcase';
                statusBadge.className = 'modal-status-badge badge-active';
            }
        }

        // Set Title & Tech Stack
        if (titleElem) titleElem.textContent = title;
        if (techElem) techElem.textContent = tech;

        // Set Khushi's Role & Contribution
        if (roleElem) roleElem.textContent = role;
        if (contribElem) contribElem.textContent = contribution || 'Contributed actively to development and system testing.';

        // Set Description
        if (descElem) descElem.textContent = description || 'Detailed project overview.';

        // Render Highlights
        if (highlightsList && highlightsSection) {
            highlightsList.innerHTML = '';
            const hiddenItems = card.querySelectorAll('.project-hidden-highlights li');
            if (hiddenItems && hiddenItems.length > 0) {
                hiddenItems.forEach(item => {
                    const li = document.createElement('li');
                    li.textContent = item.textContent;
                    highlightsList.appendChild(li);
                });
                highlightsSection.style.display = 'block';
            } else {
                highlightsSection.style.display = 'none';
            }
        }

        // Set Action Buttons
        if (githubBtn) {
            if (githubUrl && githubUrl.trim() !== '') {
                githubBtn.href = githubUrl;
                githubBtn.style.display = 'inline-flex';
            } else {
                githubBtn.style.display = 'none';
            }
        }

        if (liveBtn) {
            if (liveUrl && liveUrl.trim() !== '') {
                liveBtn.href = liveUrl;
                liveBtn.style.display = 'inline-flex';
            } else {
                liveBtn.style.display = 'none';
            }
        }

        // Show Modal
        modal.classList.add('open');
        modal.setAttribute('aria-hidden', 'false');
        document.body.style.overflow = 'hidden';
    }

    function closeModal() {
        modal.classList.remove('open');
        modal.setAttribute('aria-hidden', 'true');
        document.body.style.overflow = '';
    }

    // Attach click listeners to cards
    projectCards.forEach(card => {
        card.addEventListener('click', (e) => {
            // Prevent opening if clicking direct repo / live buttons
            if (e.target.closest('.project-direct-links') || e.target.closest('.proj-link-btn')) {
                return;
            }
            openModal(card);
        });
    });

    // Close button events
    if (closeBtn) closeBtn.addEventListener('click', closeModal);
    if (dismissBtn) dismissBtn.addEventListener('click', closeModal);

    // Click outside modal container (on backdrop)
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            closeModal();
        }
    });

    // Escape key press
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && modal.classList.contains('open')) {
            closeModal();
        }
    });
}

/**
 * Interactive Certification Details Modal System
 */
function initCertModal() {
    const certModal = document.getElementById('certModal');
    const closeBtn = document.getElementById('certModalCloseBtn');
    const dismissBtn = document.getElementById('certModalDismissBtn');
    const certCards = document.querySelectorAll('.clickable-cert-card');

    if (!certModal || !certCards.length) return;

    const titleElem = document.getElementById('certModalTitle');
    const issuerElem = document.getElementById('certModalIssuer');
    const dateElem = document.getElementById('certModalDate');
    const credIdElem = document.getElementById('certModalCredId');
    const copyIdBtn = document.getElementById('certCopyIdBtn');
    const skillsBlock = document.getElementById('certModalSkillsBlock');
    const skillsWrap = document.getElementById('certModalSkills');
    const descElem = document.getElementById('certModalDesc');
    const viewBtn = document.getElementById('certModalViewBtn');

    let currentCredId = '';

    function openCertModal(card) {
        const title = card.getAttribute('data-title') || 'Certificate Details';
        const issuer = card.getAttribute('data-issuer') || 'Issuing Authority';
        const date = card.getAttribute('data-date') || 'Completion Date';
        const credId = card.getAttribute('data-credential-id') || '';
        const url = card.getAttribute('data-url') || '';
        const skills = card.getAttribute('data-skills') || '';
        const description = card.getAttribute('data-description') || '';

        currentCredId = credId;

        if (titleElem) titleElem.textContent = title;
        if (issuerElem) issuerElem.textContent = issuer;
        if (dateElem) dateElem.textContent = `Issued: ${date}`;

        // Credential ID Handling
        if (credIdElem) {
            if (credId && credId.trim() !== '') {
                credIdElem.textContent = credId;
                if (copyIdBtn) copyIdBtn.style.display = 'inline-flex';
            } else {
                credIdElem.textContent = 'Institutional Completion Record';
                if (copyIdBtn) copyIdBtn.style.display = 'none';
            }
        }

        // Skills Pill Badges
        if (skillsWrap && skillsBlock) {
            skillsWrap.innerHTML = '';
            if (skills && skills.trim() !== '') {
                const skillsList = skills.split('•').map(s => s.trim()).filter(Boolean);
                skillsList.forEach(s => {
                    const span = document.createElement('span');
                    span.className = 'tech-tag cert-skill-tag';
                    span.textContent = s;
                    skillsWrap.appendChild(span);
                });
                skillsBlock.style.display = 'block';
            } else {
                skillsBlock.style.display = 'none';
            }
        }

        // Description
        if (descElem) {
            descElem.textContent = description || 'Verified technical certification and learning assessment.';
        }

        // Action View/Verify Button
        if (viewBtn) {
            if (url && url.trim() !== '') {
                viewBtn.href = url;
                viewBtn.style.display = 'inline-flex';
            } else {
                viewBtn.style.display = 'none';
            }
        }

        // Show Modal
        certModal.classList.add('open');
        certModal.setAttribute('aria-hidden', 'false');
        document.body.style.overflow = 'hidden';
    }

    function closeCertModal() {
        certModal.classList.remove('open');
        certModal.setAttribute('aria-hidden', 'true');
        document.body.style.overflow = '';
    }

    // Attach click listeners to cards
    certCards.forEach(card => {
        card.addEventListener('click', () => {
            openCertModal(card);
        });
    });

    // Copy Credential ID button
    if (copyIdBtn) {
        copyIdBtn.addEventListener('click', async (e) => {
            e.stopPropagation();
            if (!currentCredId) return;
            if (navigator.clipboard) {
                try {
                    await navigator.clipboard.writeText(currentCredId);
                    triggerToast(`📋 Copied Credential ID: ${currentCredId}`);
                } catch (err) {
                    triggerToast(`Credential ID: ${currentCredId}`);
                }
            } else {
                triggerToast(`Credential ID: ${currentCredId}`);
            }
        });
    }

    // Close button events
    if (closeBtn) closeBtn.addEventListener('click', closeCertModal);
    if (dismissBtn) dismissBtn.addEventListener('click', closeCertModal);

    // Click outside modal container (on backdrop)
    certModal.addEventListener('click', (e) => {
        if (e.target === certModal) {
            closeCertModal();
        }
    });

    // Escape key press
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && certModal.classList.contains('open')) {
            closeCertModal();
        }
    });
}

/**
 * Subtle Easter Egg Star Interaction
 */
function initEasterEgg() {
    const starBtn = document.getElementById('easterEggStar');
    const eggCard = document.getElementById('easterEggCard');
    if (!starBtn || !eggCard) return;

    let hideTimer = null;

    function toggleEasterEgg(e) {
        e.stopPropagation();
        const isOpen = eggCard.classList.toggle('show');
        if (isOpen) {
            clearTimeout(hideTimer);
            hideTimer = setTimeout(() => {
                eggCard.classList.remove('show');
            }, 6000);
        }
    }

    starBtn.addEventListener('click', toggleEasterEgg);

    // Close when clicking elsewhere
    document.addEventListener('click', (e) => {
        if (!eggCard.contains(e.target) && e.target !== starBtn) {
            eggCard.classList.remove('show');
        }
    });

    // Close on Escape key
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && eggCard.classList.contains('show')) {
            eggCard.classList.remove('show');
        }
    });
}

/**
 * Interactive Thank You / Connection Modal Dialog
 */
function initThankYouModal() {
    const triggerBtn = document.getElementById('contactSendMsgBtn');
    const thankYouModal = document.getElementById('thankYouModal');
    const closeBtn = document.getElementById('thankYouModalCloseBtn');
    const closeActionBtn = document.getElementById('thankYouModalCloseActionBtn');

    if (!thankYouModal) return;

    function openThankYouModal() {
        thankYouModal.classList.add('open');
        thankYouModal.setAttribute('aria-hidden', 'false');
        document.body.style.overflow = 'hidden';
    }

    function closeThankYouModal() {
        thankYouModal.classList.remove('open');
        thankYouModal.setAttribute('aria-hidden', 'true');
        document.body.style.overflow = '';
    }

    if (triggerBtn) {
        triggerBtn.addEventListener('click', (e) => {
            e.preventDefault();
            openThankYouModal();
        });
    }

    if (closeBtn) closeBtn.addEventListener('click', closeThankYouModal);
    if (closeActionBtn) closeActionBtn.addEventListener('click', closeThankYouModal);

    // Click outside modal container (on backdrop)
    thankYouModal.addEventListener('click', (e) => {
        if (e.target === thankYouModal) {
            closeThankYouModal();
        }
    });

    // Escape key press
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && thankYouModal.classList.contains('open')) {
            closeThankYouModal();
        }
    });
}

/**
 * Content Protection System
 * Disables right click (context menu), text selection, dragging, and copy/inspect shortcuts.
 */
function initContentProtection() {
    // 1. Disable Right-Click Context Menu
    document.addEventListener('contextmenu', (e) => {
        e.preventDefault();
        return false;
    });

    // 2. Disable Text & Element Selection Start
    document.addEventListener('selectstart', (e) => {
        e.preventDefault();
        return false;
    });

    // 3. Disable Dragging (Images, Links, Cards)
    document.addEventListener('dragstart', (e) => {
        e.preventDefault();
        return false;
    });

    // 4. Disable Direct Copy & Cut
    document.addEventListener('copy', (e) => {
        e.preventDefault();
        return false;
    });

    document.addEventListener('cut', (e) => {
        e.preventDefault();
        return false;
    });

    // 5. Disable Common Copy & Inspect Shortcuts
    document.addEventListener('keydown', (e) => {
        const key = e.key.toLowerCase();
        const isCtrlOrCmd = e.ctrlKey || e.metaKey;

        // Prevent Ctrl+A (Select All), Ctrl+C (Copy), Ctrl+X (Cut), Ctrl+U (View Source), Ctrl+S (Save), Ctrl+P (Print)
        if (isCtrlOrCmd && ['a', 'c', 'x', 'u', 's', 'p'].includes(key)) {
            e.preventDefault();
            return false;
        }

        // Prevent F12 (DevTools)
        if (e.key === 'F12') {
            e.preventDefault();
            return false;
        }

        // Prevent Ctrl+Shift+I, Ctrl+Shift+J, Ctrl+Shift+C (DevTools inspect)
        if (isCtrlOrCmd && e.shiftKey && ['i', 'j', 'c'].includes(key)) {
            e.preventDefault();
            return false;
        }
    });
}

/**
 * Scroll-Triggered Reveals (IntersectionObserver)
 * - Gradually fades in & moves upward: About, Skills, Education, Certifications
 * - Learning Journey timeline reveals each milestone in sequence
 */
function initScrollReveals() {
    // Respect user's accessibility preferences
    if (window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
        return;
    }

    // Graceful fallback if IntersectionObserver is not supported
    if (!('IntersectionObserver' in window)) {
        return;
    }

    // Enable CSS styling hook only when JS & IntersectionObserver are active
    document.documentElement.classList.add('js-reveal-active');

    // 1. Reveal Sections: About, Skills, Education, Certifications
    const revealSections = document.querySelectorAll('#about, #skills, #education, #certifications, .scroll-reveal');
    if (revealSections.length) {
        const sectionObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('is-revealed');
                    observer.unobserve(entry.target);
                }
            });
        }, {
            threshold: 0.12,
            rootMargin: '0px 0px -40px 0px'
        });

        revealSections.forEach(section => {
            sectionObserver.observe(section);
        });
    }

    // 2. Learning Journey Timeline: Sequential reveal of milestones
    const timelineRows = document.querySelectorAll('#journey .timeline-row');
    if (timelineRows.length) {
        const sequenceQueue = [];
        let isProcessing = false;

        function processQueue() {
            if (!sequenceQueue.length) {
                isProcessing = false;
                return;
            }
            isProcessing = true;
            const nextRow = sequenceQueue.shift();
            if (nextRow && !nextRow.classList.contains('is-revealed')) {
                nextRow.classList.add('is-revealed');
            }
            setTimeout(processQueue, 120);
        }

        const timelineObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    observer.unobserve(entry.target);
                    sequenceQueue.push(entry.target);
                    if (!isProcessing) {
                        processQueue();
                    }
                }
            });
        }, {
            threshold: 0.15,
            rootMargin: '0px 0px -30px 0px'
        });

        timelineRows.forEach(row => {
            timelineObserver.observe(row);
        });
    }
}
