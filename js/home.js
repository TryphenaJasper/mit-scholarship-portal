
const student = {
    name: "Jasper",
    cgpa: 9.8,
    attendance: 82,
    arrears: 0
};

const activeScholarships = [
    {
        name: "Cognizant Foundation Scholarship",
        amount: "₹45,000 / year",
        status: "Under Review",
        statusClass: "review",
        deadline: "18 Sept 2026",
        requirements: [
            "CGPA ≥ 7.0",
            "Attendance ≥ 70%",
            "No arrears"
        ]
    },
    {
        name: "Student Welfare Endowment Scholarship",
        amount: "₹25,000 / year",
        status: "Applied",
        statusClass: "applied",
        deadline: "25 Sept 2026",
        requirements: [
            "No arrears for renewal",
            "Single Window admission"
        ]
    }
];

const deadlines = [
    {
        name: "Cognizant Foundation Scholarship",
        type: "Application deadline",
        date: "18 Sept",
        remaining: "10 days"
    },
    {
        name: "Student Welfare Endowment Scholarship",
        type: "Application deadline",
        date: "25 Sept",
        remaining: "17 days"
    },
    {
        name: "Scholarship document submission",
        type: "Document deadline",
        date: "30 Sept",
        remaining: "22 days"
    }
];

function displayCurrentDate() {
    const dateElement = document.getElementById("currentDate");

    const today = new Date();

    const options = {
        day: "numeric",
        month: "short",
        year: "numeric"
    };

    dateElement.textContent =
        today.toLocaleDateString("en-IN", options);
}

function displayActiveScholarships() {
    const container =
        document.getElementById("activeScholarships");

    container.innerHTML = "";

    activeScholarships.forEach(scholarship => {
        const card = document.createElement("div");

        card.className = "scholarship-card";

        card.innerHTML = `
            <div>
                <h3 class="scholarship-title">
                    ${scholarship.name}
                </h3>

                <div class="scholarship-meta">
                    <span class="scholarship-amount">
                        ${scholarship.amount}
                    </span>

                    <span>
                        Deadline: ${scholarship.deadline}
                    </span>
                </div>

                <span class="status ${scholarship.statusClass}">
                    ${scholarship.status}
                </span>

                <div class="maintenance">
                    ${scholarship.requirements.map(requirement => `
                        <div class="requirement">
                            ✓ ${requirement}
                        </div>
                    `).join("")}
                </div>
            </div>
        `;

        container.appendChild(card);
    });
}

function displayDeadlines() {
    const container =
        document.getElementById("deadlineList");

    container.innerHTML = "";

    deadlines.forEach(deadline => {
        const item = document.createElement("div");

        item.className = "deadline-item";

        item.innerHTML = `
            <div class="deadline-info">
                <h4>${deadline.name}</h4>
                <p>${deadline.type}</p>
            </div>

            <div class="deadline-date">
                <strong>${deadline.date}</strong>
                <span>${deadline.remaining}</span>
            </div>
        `;

        container.appendChild(item);
    });
}

function updateSummary() {
    document.getElementById("appliedCount")
        .textContent = 4;

    document.getElementById("activeCount")
        .textContent = activeScholarships.length;

    document.getElementById("deadlineCount")
        .textContent = deadlines.length;
}

const profileButton =
    document.getElementById("profileButton");

const profileDropdown =
    document.getElementById("profileDropdown");

profileButton.addEventListener("click", function () {
    profileDropdown.classList.toggle("show");
});

document.addEventListener("click", function(event) {
    if (!event.target.closest(".profile-menu")) {
        profileDropdown.classList.remove("show");
    }
});

function initializeHomePage() {
    displayCurrentDate();
    displayActiveScholarships();
    displayDeadlines();
    updateSummary();
}

initializeHomePage();

