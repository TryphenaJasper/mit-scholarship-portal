
const API_URL = "http://localhost:8080/api/students";

const studentIdInput = document.getElementById("studentId");
const viewProfileButton = document.getElementById("viewProfileButton");
const statusMessage = document.getElementById("statusMessage");
const profileDetails = document.getElementById("profileDetails");

function showStatus(message, isError = false) {
    statusMessage.textContent = message;
    statusMessage.style.color = isError ? "#c62828" : "#397447";
}

function formatLabel(key) {
    return key
        .replace(/([a-z])([A-Z])/g, "$1 $2")
        .replace(/^is /, "")
        .replace(/^has /, "")
        .replace(/\b\w/g, letter => letter.toUpperCase());
}

function formatValue(value) {
    if (value === null || value === undefined || value === "") {
        return "Not provided";
    }

    if (typeof value === "boolean") {
        return value ? "Yes" : "No";
    }

    if (typeof value === "object") {
        return JSON.stringify(value);
    }

    return String(value);
}

function addDetail(container, label, value) {
    const item = document.createElement("div");
    item.className = "detail-item";

    const labelElement = document.createElement("span");
    labelElement.className = "detail-label";
    labelElement.textContent = label;

    const valueElement = document.createElement("span");
    valueElement.className = "detail-value";
    valueElement.textContent = formatValue(value);

    item.append(labelElement, valueElement);
    container.appendChild(item);
}

function renderObject(containerId, data, excludedFields = []) {
    const container = document.getElementById(containerId);
    container.replaceChildren();

    if (!data || typeof data !== "object" || Array.isArray(data)) {
        addDetail(container, "Details", "Not available");
        return;
    }

    let count = 0;

    Object.entries(data).forEach(([key, value]) => {
        if (excludedFields.includes(key)) {
            return;
        }

        addDetail(container, formatLabel(key), value);
        count++;
    });

    if (count === 0) {
        addDetail(container, "Details", "Not available");
    }
}

function renderAcademicRecords(records) {
    const container = document.getElementById("academicDetails");
    container.replaceChildren();

    if (!Array.isArray(records) || records.length === 0) {
        container.textContent = "No academic records available.";
        return;
    }

    records.forEach((record, index) => {
        const card = document.createElement("div");
        card.className = "academic-record";

        const heading = document.createElement("h3");
        heading.textContent = `Semester ${record.semester} Details`;
        card.appendChild(heading);

        if (record && typeof record === "object") {
            Object.entries(record).forEach(([key, value]) => {
                addDetail(card, formatLabel(key), value);
            });
        }

        container.appendChild(card);
    });
}

async function loadStudentProfile() {
    const studentId = studentIdInput.value.trim();

    if (!studentId || !/^[1-9]\d*$/.test(studentId)) {
        showStatus("Please enter a valid Student ID.", true);
        profileDetails.hidden = true;
        return;
    }

    viewProfileButton.disabled = true;
    viewProfileButton.textContent = "Loading...";
    profileDetails.hidden = true;
    showStatus("Fetching student details...");

    try {
        const response = await fetch(
            `${API_URL}/${encodeURIComponent(studentId)}`
        );

        if (response.status === 404) {
            showStatus("No student found with that ID.", true);
            return;
        }

        if (!response.ok) {
            throw new Error(`Request failed (HTTP ${response.status}).`);
        }

        const profile = await response.json();

        renderObject("studentDetails", profile.student);
        renderObject("backgroundDetails", profile.background);
        renderObject("financialDetails", profile.financial);
        renderObject("educationDetails", profile.education);
        renderAcademicRecords(profile.academicRecords);

        profileDetails.hidden = false;
        showStatus("Student profile loaded successfully.");
    } catch (error) {
        console.error("Error loading student profile:", error);
        showStatus(
            "Unable to load the profile. Check that the backend is running and try again.",
            true
        );
    } finally {
        viewProfileButton.disabled = false;
        viewProfileButton.textContent = "View Profile";
    }
}

viewProfileButton.addEventListener("click", loadStudentProfile);

studentIdInput.addEventListener("keydown", event => {
    if (event.key === "Enter") {
        loadStudentProfile();
    }
});

// Reuse the Student ID saved after successful registration.
const savedStudentId = localStorage.getItem("studentId");

if (savedStudentId) {
    studentIdInput.value = savedStudentId;
    loadStudentProfile();
}

// Profile dropdown
const profileMenuButton = document.getElementById("profileMenuButton");
const profileDropdown = document.getElementById("profileDropdown");
const logoutButton = document.getElementById("logoutButton");

if (profileMenuButton && profileDropdown) {
    profileMenuButton.addEventListener("click", () => {
        const isOpening = profileDropdown.hidden;
        profileDropdown.hidden = !isOpening;
        profileMenuButton.setAttribute("aria-expanded", String(isOpening));
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

if (logoutButton) {
    logoutButton.addEventListener("click", () => {
        if (profileDropdown) {
            profileDropdown.hidden = true;
        }

        if (profileMenuButton) {
            profileMenuButton.setAttribute("aria-expanded", "false");
        }
    });
}
