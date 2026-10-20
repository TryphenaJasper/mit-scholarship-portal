/* =========================================================
   BACKEND API CONFIGURATION
========================================================= */

const API_BASE_URL = "http://localhost:8080/api";
const studentId = localStorage.getItem("studentId");

let applicableScholarships = [];
let scholarshipRequirements = [];
let allScholarships = [];




/* =========================================================
   PRIORITY RESOLUTION
========================================================= */

const combinations = [

    {
        label: "BEST COMBINATION",

        name:
            "Foundation for Excellence + AMITA Scholarship",

        scholarships: [

            {
                name: "Foundation for Excellence",
                amount: "₹50,000 / year"
            },

            {
                name: "AMITA Scholarship",
                amount: "₹20,000 / year"
            }
        ],

        total: "₹70,000 / year",

        reason:
            "Highest-value combination among the scholarships currently considered compatible."
    },


    {
        label: "COMBO OPTION",

        name:
            "Student Welfare Endowment + AMITA Scholarship",

        scholarships: [

            {
                name:
                    "Student Welfare Endowment Scholarship",

                amount:
                    "₹25,000 / year"
            },

            {
                name:
                    "AMITA Scholarship",

                amount:
                    "₹20,000 / year"
            }
        ],

        total: "₹45,000 / year",

        reason:
            "Lower-value alternative combination."
    }
];

/* =========================================================
   BACKEND RESPONSE HELPERS
========================================================= */

function getScholarshipId(scholarship) {
    return scholarship.scholarshipId ?? scholarship.id;
}

function getScholarshipName(scholarship) {
    return scholarship.scholarshipName
        ?? scholarship.name
        ?? "Unnamed Scholarship";
}

function getScholarshipAmount(scholarship) {
    const amount = scholarship.amount
        ?? scholarship.scholarshipAmount
        ?? scholarship.awardAmount;

    if (amount == null) {
        return "Amount not specified";
    }

    const number = Number(amount);

    return Number.isFinite(number)
        ? number.toLocaleString("en-IN", {
            style: "currency",
            currency: "INR",
            maximumFractionDigits: 2
        })
        : String(amount);
}

async function fetchBackend(url) {
    const response = await fetch(url);

    if (!response.ok) {
        throw new Error(`API request failed: ${response.status}`);
    }

    return response.json();
}
/* =========================================================
   CURRENT DATE
========================================================= */

function displayCurrentDate() {

    const dateElement =
        document.getElementById("currentDate");

    if (!dateElement) {
        return;
    }

    const today = new Date();

    dateElement.textContent =
        today.toLocaleDateString(
            "en-IN",
            {
                day: "numeric",
                month: "short",
                year: "numeric"
            }
        );
}


/* =========================================================
   APPLICABLE SCHOLARSHIPS
========================================================= */

async function displayApplicableScholarships() {
    const container = document.getElementById("applicableScholarships");

    if (!container) return;

    container.replaceChildren();

    if (!studentId) {
        container.textContent =
            "Please create your student profile first.";
        return;
    }

    try {
        const scholarships = await fetchBackend(
            `${API_BASE_URL}/students/${encodeURIComponent(studentId)}/eligibility`
        );

        if (!Array.isArray(scholarships)) {
            throw new Error("Unexpected eligibility response.");
        }

        applicableScholarships = scholarships;

        scholarships.sort((a, b) => {
            const amountA = Number(
                a.amount ?? a.scholarshipAmount ?? a.awardAmount ?? 0
            );
            const amountB = Number(
                b.amount ?? b.scholarshipAmount ?? b.awardAmount ?? 0
            );

            return amountB - amountA;
        });

        if (scholarships.length === 0) {
            container.textContent =
                "No scholarships currently meet all mandatory eligibility requirements.";
            return;
        }

        scholarships.forEach((scholarship, index) => {
            const card = document.createElement("div");
            card.className = "scholarship-card";

            const priority = document.createElement("div");
            priority.className = "priority-number";
            priority.textContent = `#${index + 1}`;

            const main = document.createElement("div");
            main.className = "scholarship-main";

            const title = document.createElement("h3");
            title.className = "scholarship-title";
            title.textContent = getScholarshipName(scholarship);

            const info = document.createElement("div");
            info.className = "scholarship-info";

            const amount = document.createElement("span");
            amount.className = "money";
            amount.textContent = getScholarshipAmount(scholarship);

            const deadline = document.createElement("span");
            deadline.textContent =
                "Deadline: " +
                (scholarship.deadline ?? "Check official notice");

            info.append(amount, deadline);
            main.append(title, info);

            const link = document.createElement("a");
            link.className = "scholarship-link";
            link.textContent = "Official Portal →";

            const url = scholarship.officialUrl ?? scholarship.link;

            if (url) {
                link.href = url;
                link.target = "_blank";
                link.rel = "noopener noreferrer";
            } else {
                link.href = "#";
                link.addEventListener("click", event => {
                    event.preventDefault();
                });
            }

            card.append(priority, main, link);
            container.appendChild(card);
        });

    } catch (error) {
        console.error("Eligibility API error:", error);

        container.textContent =
            "Unable to load eligible scholarships. Please check the backend.";
    }
}


/* =========================================================
   GAP ANALYSIS
========================================================= */


async function displayGapAnalysis() {
    const container = document.getElementById("gapList");
    if (!container) return;

    container.replaceChildren();

    if (!studentId) {
        container.textContent =
            "Please create your student profile first.";
        return;
    }

    try {
        const scholarships = await fetchBackend(
            `${API_BASE_URL}/scholarships`
        );

        if (!Array.isArray(scholarships)) {
            throw new Error("Unexpected scholarships API response.");
        }

        const academicAttributes = [
            "cgpa",
            "hsc",
            "class_12_percentage",
            "class_10_percentage",
            "marks_percentage",
            "attendance_percentage"
        ];

        const academicGaps = [];
        const eligibilityRestrictions = [];
        const missingInformation = [];

        for (const scholarship of scholarships) {
            const scholarshipId = getScholarshipId(scholarship);
            if (scholarshipId == null) continue;

            try {
                const result = await fetchBackend(
                    `${API_BASE_URL}/students/${encodeURIComponent(studentId)}` +
                    `/scholarships/${encodeURIComponent(scholarshipId)}/gap-analysis`
                );

                const requirements = Array.isArray(result.requirements)
                    ? result.requirements
                    : [];

                for (const req of requirements) {
                    
const item = {
    scholarship:
        result.scholarshipName ??
        getScholarshipName(scholarship),
    attribute: req.attribute ?? "Requirement",
    required: req.requiredValue ?? "Not specified",
    current: req.studentValue ?? "Not available",
    gap: req.gap,
    message: req.message ?? "",
    status: req.status
};


                    if (req.status === "UNKNOWN") {
                        missingInformation.push(item);
                    } else if (req.status === "UNMET") {
                        const attribute =
                            String(req.attribute ?? "").toLowerCase();

                        const isAcademic = academicAttributes.some(key =>
                            attribute.includes(key)
                        );

                        if (isAcademic) {
                            academicGaps.push(item);
                        } else {
                            eligibilityRestrictions.push(item);
                        }
                    }
                }
            } catch (error) {
                console.error(
                    `Gap analysis failed for ${getScholarshipName(scholarship)}:`,
                    error
                );
            }
        }

        function createSection(titleText, descriptionText, items, emptyText) {
            const section = document.createElement("section");
            section.className = "gap-card";

            const title = document.createElement("h3");
            title.textContent = titleText;

            const description = document.createElement("p");
            description.className = "gap-card-subtitle";
            description.textContent = descriptionText;

            section.append(title, description);

            if (items.length === 0) {
                const empty = document.createElement("p");
                empty.textContent = emptyText;
                section.appendChild(empty);
                return section;
            }

            items.forEach(item => {
                const card = document.createElement("div");
                card.className = "gap-item";

                const scholarship = document.createElement("h4");
                scholarship.textContent = item.scholarship;

                const attribute = document.createElement("div");
                attribute.className = "gap-item-label";
                attribute.textContent = item.attribute.replaceAll("_", " ");

                const required = document.createElement("div");
                required.className = "gap-item-value";
                required.textContent = `Required: ${item.required}`;

                const current = document.createElement("div");
                current.className = "gap-card-subtitle";
                current.textContent = `Your value: ${item.current}`;

                const message = document.createElement("p");
                message.textContent = item.message;

                
card.append(
    scholarship,
    attribute,
    required,
    current
);

if (
    item.gap !== null &&
    item.gap !== undefined &&
    item.gap !== ""
) {
    const gapDisplay = document.createElement("div");
    gapDisplay.className = "gap-value";
    gapDisplay.textContent = `Gap: ${item.gap}`;
    card.appendChild(gapDisplay);
}

card.appendChild(message);
section.appendChild(card);

            });

            return section;
        }

        container.append(
            createSection(
                "1. Academic Improvement Gaps",
                "Academic requirements that may be improved over time.",
                academicGaps,
                "No unmet academic requirements were identified in the results returned by the backend."
            ),
            createSection(
                "2. Eligibility Restrictions",
                "Other unmet eligibility conditions. These are not academic improvement gaps.",
                eligibilityRestrictions,
                "No unmet non-academic eligibility restrictions were identified."
            ),
            createSection(
                "3. Information Needed",
                "Complete your profile to help evaluate these requirements.",
                missingInformation,
                "No missing requirement information was reported."
            )
        );

    } catch (error) {
        console.error("Gap Analysis API error:", error);
        container.textContent =
            "Unable to load Gap Analysis. Check that the backend is running.";
    }
}


/* =========================================================
   PRIORITY RESOLUTION
========================================================= */

function displayCombinations() {

    const container =
        document.getElementById("comboList");


    if (!container) {
        return;
    }


    container.innerHTML = "";


    combinations.forEach(
        function(combo) {

            const card =
                document.createElement("div");

            card.className =
                "combo-card";


            /* HEADER */

            const header =
                document.createElement("div");

            header.className =
                "combo-header";


            const heading =
                document.createElement("div");


            const label =
                document.createElement("span");

            label.className =
                "combo-label";

            label.textContent =
                combo.label;


            const title =
                document.createElement("h3");

            title.textContent =
                combo.name;


            heading.appendChild(label);

            heading.appendChild(title);


            /* TOTAL */

            const total =
                document.createElement("div");

            total.className =
                "combo-total";


            const totalLabel =
                document.createElement("span");

            totalLabel.textContent =
                "Total value";


            const totalAmount =
                document.createElement("strong");

            totalAmount.textContent =
                combo.total;


            total.appendChild(totalLabel);

            total.appendChild(totalAmount);


            header.appendChild(heading);

            header.appendChild(total);


            /* SCHOLARSHIPS */

            const scholarshipContainer =
                document.createElement("div");

            scholarshipContainer.className =
                "combo-scholarships";


            combo.scholarships.forEach(
                function(scholarship) {

                    const item =
                        document.createElement("div");

                    item.className =
                        "combo-item";


                    const name =
                        document.createElement("div");

                    name.className =
                        "combo-item-name";

                    name.textContent =
                        scholarship.name;


                    const amount =
                        document.createElement("div");

                    amount.className =
                        "combo-item-amount";

                    amount.textContent =
                        scholarship.amount;


                    item.appendChild(name);

                    item.appendChild(amount);


                    scholarshipContainer.appendChild(
                        item
                    );
                }
            );


            /* REASON */

            const reason =
                document.createElement("p");

            reason.className =
                "combo-reason";

            reason.textContent =
                combo.reason;


            /* STATUS */

            const status =
                document.createElement("span");

            status.className =
                "combo-status";

            status.textContent =
                "Prototype compatibility result";


            card.appendChild(header);

            card.appendChild(
                scholarshipContainer
            );

            card.appendChild(reason);

            card.appendChild(status);


            container.appendChild(card);
        }
    );
}


/* =========================================================
   TOGGLE BUTTONS

   YOUR HTML USES:

   data-section="applicable"
   data-section="gap"
   data-section="resolution"
========================================================= */

function setupToggles() {

    const buttons =
        document.querySelectorAll(
            ".toggle-button"
        );


    const sections = {

        applicable:
            document.getElementById(
                "applicableSection"
            ),

        gap:
            document.getElementById(
                "gapSection"
            ),

        resolution:
            document.getElementById(
                "resolutionSection"
            )
    };


    buttons.forEach(
        function(button) {

            button.addEventListener(
                "click",
                function() {


                    /* Remove active from buttons */

                    buttons.forEach(
                        function(item) {

                            item.classList.remove(
                                "active"
                            );
                        }
                    );


                    /* Hide all sections */

                    Object.values(sections).forEach(
                        function(section) {

                            if (section) {

                                section.classList.remove(
                                    "active-section"
                                );
                            }
                        }
                    );


                    /* Activate clicked button */

                    button.classList.add(
                        "active"
                    );


                    /*
                       Get:

                       applicable
                       gap
                       resolution
                    */

                    const selected =
                        button.getAttribute(
                            "data-section"
                        );


                    /*
                       Show corresponding section.
                    */

                    if (sections[selected]) {

                        sections[selected]
                            .classList.add(
                                "active-section"
                            );
                    }
                }
            );
        }
    );
}


/* =========================================================
   PROFILE DROPDOWN
========================================================= */


function setupProfileDropdown() {
    const profileMenuButton =
        document.getElementById("profileMenuButton");

    const profileDropdown =
        document.getElementById("profileDropdown");

    const logoutButton =
        document.getElementById("logoutButton");

    if (!profileMenuButton || !profileDropdown) {
        return;
    }

    profileMenuButton.addEventListener("click", function(event) {
        event.stopPropagation();

        const isOpening = profileDropdown.hidden;

        profileDropdown.hidden = !isOpening;

        profileMenuButton.setAttribute(
            "aria-expanded",
            String(isOpening)
        );
    });

    document.addEventListener("click", function(event) {
        if (
            !profileMenuButton.contains(event.target) &&
            !profileDropdown.contains(event.target)
        ) {
            profileDropdown.hidden = true;

            profileMenuButton.setAttribute(
                "aria-expanded",
                "false"
            );
        }
    });

    if (logoutButton) {
        logoutButton.addEventListener("click", function() {
            profileDropdown.hidden = true;

            profileMenuButton.setAttribute(
                "aria-expanded",
                "false"
            );
        });
    }
}



/* =========================================================
   INITIALIZE SEARCH PAGE
========================================================= */

function initializeSearchPage() {

    displayCurrentDate();

    displayApplicableScholarships();

    displayGapAnalysis();

    displayCombinations();

    setupToggles();

    setupProfileDropdown();
}


initializeSearchPage();