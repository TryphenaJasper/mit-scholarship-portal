
const API_URL = "https://mit-scholarship-portal.onrender.com/api/scholarships";

const scholarshipList = document.getElementById("scholarshipList");

let scholarships = [];

/* -------------------------
   FORMAT SCHOLARSHIP DATA
------------------------- */

function formatAmount(scholarship) {
    if (scholarship.amount == null) {
        return "Amount not specified";
    }

    const currency = scholarship.currency || "INR";
    const amount = Number(scholarship.amount);

    const formattedAmount = new Intl.NumberFormat("en-IN", {
        maximumFractionDigits: 2
    }).format(amount);

    const currencySymbol = currency === "INR" ? "₹" : currency;

    let value = `${currencySymbol} ${formattedAmount}`;

    if (scholarship.amountPeriod) {
        value += ` / ${scholarship.amountPeriod.toLowerCase()}`;
    }

    return value;
}

function formatDeadline(deadline) {
    if (!deadline) {
        return "Check official portal";
    }

    const date = new Date(`${deadline}T00:00:00`);

    if (Number.isNaN(date.getTime())) {
        return deadline;
    }

    return date.toLocaleDateString("en-IN", {
        day: "2-digit",
        month: "short",
        year: "numeric"
    });
}

function getPortalLink(scholarship) {
    const url = scholarship.officialUrl;

    if (!url || url === "#") {
        return "#";
    }

    try {
        const parsedUrl = new URL(url);

        if (parsedUrl.protocol === "https:" ||
            parsedUrl.protocol === "http:") {
            return parsedUrl.href;
        }
    } catch (error) {
        // Invalid URLs use the fallback below.
    }

    return "#";
}

/* -------------------------
   FETCH FROM BACKEND API
------------------------- */

async function loadScholarships() {
    scholarshipList.textContent = "Loading scholarships...";

    try {
        const response = await fetch(API_URL);

        if (!response.ok) {
            throw new Error(`Server returned ${response.status}`);
        }

        const data = await response.json();

        if (!Array.isArray(data)) {
            throw new Error("Unexpected scholarship data format.");
        }

        scholarships = data.map(scholarship => ({
            ...scholarship,
            category: String(scholarship.category || "").toLowerCase(),
            value: formatAmount(scholarship),
            deadline: formatDeadline(scholarship.deadline),
            link: getPortalLink(scholarship)
        }));

        displayScholarships();

    } catch (error) {
        console.error("Unable to load scholarships:", error);

        scholarshipList.textContent =
            "Unable to load scholarships. Please check that the backend is running and try refreshing the page.";
    }
}

/* -------------------------
   DISPLAY SCHOLARSHIPS
------------------------- */

function displayScholarships(category = "all") {
    scholarshipList.replaceChildren();

    let filteredScholarships = scholarships;

    if (category !== "all") {
        filteredScholarships = scholarships.filter(
            scholarship => scholarship.category === category
        );
    }

    if (filteredScholarships.length === 0) {
        scholarshipList.textContent = scholarships.length === 0
            ? "No scholarships available."
            : "No scholarships found in this category.";
        return;
    }

    filteredScholarships.forEach((scholarship, index) => {
        const card = document.createElement("div");
        card.className = "scholarship-card";

        const number = String(index + 1).padStart(2, "0");

        const numberElement = document.createElement("div");
        numberElement.className = "scholarship-number";
        numberElement.textContent = `#${number}`;

        const info = document.createElement("div");
        info.className = "scholarship-info";

        const name = document.createElement("div");
        name.className = "scholarship-name";
        name.textContent = scholarship.name || "Unnamed Scholarship";

        const meta = document.createElement("div");
        meta.className = "scholarship-meta";

        const value = document.createElement("span");
        value.className = "scholarship-value";
        value.textContent = scholarship.value;

        const deadline = document.createElement("span");
        deadline.className = "scholarship-deadline";
        deadline.textContent = `Deadline: ${scholarship.deadline}`;

        const categoryTag = document.createElement("span");
        categoryTag.className = "category-tag";
        categoryTag.textContent = scholarship.category;

        meta.append(value, deadline, categoryTag);
        info.append(name, meta);

        const portalButton = document.createElement("a");
        portalButton.className = "portal-button";
        portalButton.href = scholarship.link;
        portalButton.textContent = scholarship.link === "#"
            ? "Check MIT Notice →"
            : "Official Portal →";

        if (scholarship.link !== "#") {
            portalButton.target = "_blank";
            portalButton.rel = "noopener noreferrer";
        } else {
            portalButton.addEventListener("click", event => {
                event.preventDefault();
            });
        }

        card.append(numberElement, info, portalButton);
        scholarshipList.appendChild(card);
    });
}

/* -------------------------
   CATEGORY FILTER
------------------------- */

const filterButtons = document.querySelectorAll(".filter-button");

filterButtons.forEach(button => {
    button.addEventListener("click", () => {
        filterButtons.forEach(btn => btn.classList.remove("active"));
        button.classList.add("active");

        displayScholarships(button.dataset.category || "all");
    });
});


/* -------------------------
   PROFILE DROPDOWN
------------------------- */

const profileMenuButton = document.getElementById("profileMenuButton");
const profileDropdown = document.getElementById("profileDropdown");
const logoutButton = document.getElementById("logoutButton");

if (profileMenuButton && profileDropdown) {
    profileMenuButton.addEventListener("click", event => {
        event.stopPropagation();

        const isOpening = profileDropdown.hidden;

        profileDropdown.hidden = !isOpening;
        profileMenuButton.setAttribute(
            "aria-expanded",
            String(isOpening)
        );
    });

    document.addEventListener("click", event => {
        if (
            !profileMenuButton.contains(event.target) &&
            !profileDropdown.contains(event.target)
        ) {
            profileDropdown.hidden = true;
            profileMenuButton.setAttribute("aria-expanded", "false");
        }
    });
}

/* Dummy logout: close the dropdown only */
if (logoutButton) {
    logoutButton.addEventListener("click", () => {
        profileDropdown.hidden = true;

        if (profileMenuButton) {
            profileMenuButton.setAttribute("aria-expanded", "false");
        }
    });
}

/* -------------------------
   INITIAL LOAD
------------------------- */

loadScholarships();
