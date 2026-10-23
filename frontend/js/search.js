/* =========================================================
   STUDENT PROFILE
========================================================= */

const student = {
    name: "Jasper",
    cgpa: 6.6,
    attendance: 68,
    hscPercentage: 76,
    arrears: 0,
    income: 180000
};


/* =========================================================
   APPLICABLE SCHOLARSHIPS

   These are scholarships the student is already eligible for.
   Sorted from highest amount to lowest amount.
========================================================= */

const applicableScholarships = [

    {
        name: "Foundation for Excellence",
        amount: "₹50,000 / year",
        value: 50000,
        deadline: "Check official portal",
        link: "https://scholarships.gov.in/"
    },

    {
        name: "Student Welfare Endowment Scholarship",
        amount: "₹25,000 / year",
        value: 25000,
        deadline: "Check MIT notice",
        link: "#"
    },

    {
        name: "AMITA Scholarship",
        amount: "₹20,000 / year",
        value: 20000,
        deadline: "Check Alumni notification",
        link: "#"
    },

    {
        name: "Central Sector Scheme of Scholarship",
        amount: "₹12,000 / year",
        value: 12000,
        deadline: "Check official portal",
        link: "https://scholarships.gov.in/"
    }
];


/* =========================================================
   SCHOLARSHIPS WITH ACADEMIC GAPS

   IMPORTANT:
   Only academic requirements are considered here.

   Income is displayed as information.
   Disability is NOT considered as a gap.
========================================================= */

const scholarshipRequirements = [

    {
        name: "AICTE Pragati Scholarship Scheme",
        amount: "₹50,000 / year",
        value: 50000,

        income: "Family income: ₹1.8 lakh",

        requirements: [

            {
                type: "HSC",
                required: 80,
                current: student.hscPercentage,

                requiredText: "Required HSC: 80%",
                currentText:
                    "Your HSC: " +
                    student.hscPercentage +
                    "%",

                gapText:
                    "Need " +
                    (80 - student.hscPercentage) +
                    "% more in HSC"
            },

            {
                type: "Attendance",
                required: 75,
                current: student.attendance,

                requiredText:
                    "Required attendance: 75%",

                currentText:
                    "Your attendance: " +
                    student.attendance +
                    "%",

                gapText:
                    "Need " +
                    (75 - student.attendance) +
                    "% more attendance"
            }
        ]
    },


    {
        name: "Cognizant Foundation Scholarship",
        amount: "₹45,000 / year",
        value: 45000,

        income: "Family income: ₹1.8 lakh",

        requirements: [

            {
                type: "CGPA",
                required: 7.0,
                current: student.cgpa,

                requiredText:
                    "Required CGPA: 7.0",

                currentText:
                    "Your CGPA: " +
                    student.cgpa,

                gapText:
                    "Need " +
                    (7.0 - student.cgpa).toFixed(1) +
                    " more CGPA"
            },

            {
                type: "Attendance",
                required: 70,
                current: student.attendance,

                requiredText:
                    "Required attendance: 70%",

                currentText:
                    "Your attendance: " +
                    student.attendance +
                    "%",

                gapText:
                    "Need " +
                    (70 - student.attendance) +
                    "% more attendance"
            }
        ]
    },


    {
        name: "Bank of Tokyo Scholarship",
        amount: "₹23,100 / year",
        value: 23100,

        income: "Family income: ₹1.8 lakh",

        requirements: [

            {
                type: "CGPA",
                required: 7.0,
                current: student.cgpa,

                requiredText:
                    "Required CGPA: 7.0",

                currentText:
                    "Your CGPA: " +
                    student.cgpa,

                gapText:
                    "Need " +
                    (7.0 - student.cgpa).toFixed(1) +
                    " more CGPA"
            }
        ]
    }
];


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

    const container =
        document.getElementById("applicableScholarships");

    if (!container) {
        return;
    }

    container.innerHTML = "";

    const studentId = localStorage.getItem("studentId");

    if (!studentId) {
        container.textContent =
            "Please log in or register to view your eligible scholarships.";
        return;
    }

    try {

        const response = await fetch(
            `http://localhost:8080/api/students/${studentId}/eligibility`
        );

        if (!response.ok) {
            if (response.status === 404) {
                container.textContent =
                    "Student profile not found. Please check your student ID.";
                return;
            }

            throw new Error(
                `Eligibility request failed: ${response.status}`
            );
        }

        const scholarships = await response.json();

        if (!Array.isArray(scholarships) || scholarships.length === 0) {
            container.textContent =
                "No eligible scholarships were found for your profile.";
            return;
        }

        // Sort by scholarship amount, highest first
        const sorted = [...scholarships].sort(function(a, b) {
            return Number(b.amount || 0) - Number(a.amount || 0);
        });

        sorted.forEach(function(scholarship, index) {

            const card = document.createElement("div");
            card.className = "scholarship-card";

            // PRIORITY NUMBER
            const priority = document.createElement("div");
            priority.className = "priority-number";
            priority.textContent = "#" + (index + 1);

            // MAIN CONTENT
            const main = document.createElement("div");
            main.className = "scholarship-main";

            const title = document.createElement("h3");
            title.className = "scholarship-title";
            title.textContent = scholarship.name || "Unnamed Scholarship";

            const info = document.createElement("div");
            info.className = "scholarship-info";

            const amount = document.createElement("span");
            amount.className = "money";

            const currency = scholarship.currency || "INR";
            const amountValue = Number(scholarship.amount || 0);

            amount.textContent =
                (currency === "INR" ? "₹" : currency + " ") +
                amountValue.toLocaleString("en-IN") +
                (scholarship.amountPeriod
                    ? " / " + scholarship.amountPeriod
                    : "");

            const deadline = document.createElement("span");
            deadline.textContent =
                "Deadline: " + (scholarship.deadline || "Not specified");

            info.appendChild(amount);
            info.appendChild(deadline);

            main.appendChild(title);
            main.appendChild(info);

            // OFFICIAL LINK
            const link = document.createElement("a");
            link.className = "scholarship-link";
            link.textContent = "Official Portal →";

            const officialLink =
                scholarship.officialLink || scholarship.official_link;

            if (officialLink) {
                link.href = officialLink;
                link.target = "_blank";
                link.rel = "noopener noreferrer";
            } else {
                link.removeAttribute("href");
                link.setAttribute("aria-disabled", "true");
                link.textContent = "Official link unavailable";
            }

            card.appendChild(priority);
            card.appendChild(main);
            card.appendChild(link);

            container.appendChild(card);
        });

    } catch (error) {

        console.error("Error loading eligible scholarships:", error);

        container.textContent =
            "Unable to load eligible scholarships. Please check that the backend is running.";
    }
}



/* =========================================================
   GAP ANALYSIS
========================================================= */


async function displayGapAnalysis() {
    const container = document.getElementById("gapList");

    if (!container) {
        console.error("gapList was not found in search.html");
        return;
    }

    container.innerHTML = "";

    const studentId = localStorage.getItem("studentId");

    if (!studentId) {
        container.textContent =
            "Please save your student profile before viewing gap analysis.";
        return;
    }

    try {
        // Fetch scholarships the student is already eligible for.
        const eligibleResponse = await fetch(
            `http://localhost:8080/api/students/${studentId}/eligibility`
        );

        if (!eligibleResponse.ok) {
            throw new Error(
                eligibleResponse.status === 404
                    ? "Student profile not found. Please check your student ID."
                    : "Could not load eligible scholarships."
            );
        }

        const eligibleScholarships = await eligibleResponse.json();

        const eligibleIds = new Set(
            eligibleScholarships.map(s => String(s.scholarshipId ?? s.id))
        );

        // Fetch all scholarships to identify those needing gap analysis.
        const scholarshipsResponse = await fetch(
            "http://localhost:8080/api/scholarships"
        );

        if (!scholarshipsResponse.ok) {
            throw new Error("Could not load scholarships.");
        }

        const scholarships = await scholarshipsResponse.json();

        // Gap analysis is useful for scholarships the student is not
        // already eligible for.
        const candidates = scholarships.filter(s =>
            !eligibleIds.has(String(s.scholarshipId ?? s.id))
        );

        const results = await Promise.all(
            candidates.map(async scholarship => {
                const scholarshipId = scholarship.scholarshipId ?? scholarship.id;

                try {
                    const response = await fetch(
                        `http://localhost:8080/api/students/${studentId}/scholarships/${scholarshipId}/gap-analysis`
                    );

                    if (!response.ok) {
                        console.warn(
                            `Gap analysis failed for scholarship ${scholarshipId}`
                        );
                        return null;
                    }

                    return await response.json();
                } catch (error) {
                    console.error(
                        `Error analyzing scholarship ${scholarshipId}:`,
                        error
                    );
                    return null;
                }
            })
        );

        // Keep only scholarships with at least one unmet requirement.
        const actualGaps = results
            .filter(result =>
                result &&
                Array.isArray(result.requirements) &&
                result.requirements.some(
                    requirement =>
                        String(requirement.status || "").toUpperCase() === "UNMET"
                )
            )
            .sort((a, b) => {
                const amountA = Number(
                    candidates.find(s =>
                        String(s.scholarshipId ?? s.id) === String(a.scholarshipId)
                    )?.amount ?? 0
                );

                const amountB = Number(
                    candidates.find(s =>
                        String(s.scholarshipId ?? s.id) === String(b.scholarshipId)
                    )?.amount ?? 0
                );

                return amountB - amountA;
            });

        actualGaps.forEach(scholarship => {
            const card = document.createElement("div");
            card.className = "gap-card";

            // Header
            const header = document.createElement("div");
            header.className = "gap-card-header";

            const heading = document.createElement("div");

            const title = document.createElement("h3");
            title.textContent = scholarship.scholarshipName || "Scholarship";

            const subtitle = document.createElement("p");
            subtitle.className = "gap-card-subtitle";
            subtitle.textContent =
                `${scholarship.unmetCount} unmet requirement(s)` +
                (scholarship.unknownCount
                    ? ` · ${scholarship.unknownCount} unknown`
                    : "");

            heading.appendChild(title);
            heading.appendChild(subtitle);

            const money = document.createElement("div");
            money.className = "gap-scholarship-money";

            const scholarshipInfo = candidates.find(s =>
                String(s.scholarshipId ?? s.id) === String(scholarship.scholarshipId)
            );

            if (scholarshipInfo) {
                const amount = scholarshipInfo.amount;
                const currency = scholarshipInfo.currency || "INR";
                const period = scholarshipInfo.amountPeriod
                    ? `/${scholarshipInfo.amountPeriod}`
                    : "";

                money.textContent = amount != null
                    ? `${currency === "INR" ? "₹" : currency + " "}${Number(amount).toLocaleString("en-IN")}${period}`
                    : "";
            }

            header.appendChild(heading);
            header.appendChild(money);
            card.appendChild(header);

            // Requirement details
            const requirements = document.createElement("div");
            requirements.className = "gap-requirements";

            scholarship.requirements.forEach(requirement => {
                if (
                    String(requirement.status || "").toUpperCase() !== "UNMET"
                ) {
                    return;
                }

                const item = document.createElement("div");
                item.className = "gap-item";

                const label = document.createElement("span");
                label.className = "gap-item-label";

                const attributeLabels = {
                    "IS_DIFFERENTLY_ABLED": "Disability Status",
                    "YEAR_OF_STUDY": "Year of Study",
                    "CGPA": "Cumulative GPA",
                    "MIN_CGPA": "Minimum CGPA",
                    "HSC_PERCENTAGE": "Higher Secondary Percentage",
                    "ATTENDANCE": "Attendance",
                    "FAMILY_INCOME": "Family Income",
                    "ANNUAL_FAMILY_INCOME": "Annual Family Income",
                    "COMMUNITY": "Community",
                    "GENDER": "Gender",
                    "ARREARS": "Arrears",
                    "DEPARTMENT": "Department"
                };

                const attribute = (requirement.attribute || "Requirement").trim();

                label.textContent =
                    attributeLabels[attribute.toUpperCase()] ||
                    attribute
                        .replace(/_/g, " ")
                        .toLowerCase()
                        .replace(/\b\w/g, character => character.toUpperCase());

                
                const required = document.createElement("div");
                required.className = "gap-item-value";
                required.textContent =
                    `Required value: ${requirement.requiredValue ?? "Not specified"}`;

                const current = document.createElement("div");
                current.className = "gap-card-subtitle";
                current.textContent =
                    `Your current value: ${requirement.studentValue ?? "Unknown"}`;


                
                const gap = document.createElement("div");
                gap.className = "gap-value";

                if (requirement.message && requirement.message.trim() !== "") {
                    gap.textContent = requirement.message;
                } else if (requirement.gap != null) {
                    gap.textContent = `You need to improve by ${requirement.gap}.`;
                } else {
                    gap.textContent = "This requirement is not currently satisfied.";
                }


                item.appendChild(label);
                item.appendChild(required);
                item.appendChild(current);
                item.appendChild(gap);

                requirements.appendChild(item);
            });

            card.appendChild(requirements);

            // Informational note for unknown requirements.
            if (scholarship.unknownCount > 0) {
                const note = document.createElement("p");
                note.className = "gap-income";
                note.textContent =
                    `${scholarship.unknownCount} requirement(s) could not be evaluated with the available profile data.`;
                card.appendChild(note);
            }

            container.appendChild(card);
        });

        if (actualGaps.length === 0) {
            const message = document.createElement("div");
            message.className = "gap-card";

            const title = document.createElement("h3");
            title.textContent = "No academic gaps found";

            const text = document.createElement("p");
            text.className = "gap-card-subtitle";
            text.textContent =
                "No unmet requirements were found among the scholarships analyzed.";

            message.appendChild(title);
            message.appendChild(text);
            container.appendChild(message);
        }
    } catch (error) {
        console.error("Gap analysis error:", error);

        container.innerHTML = "";

        const message = document.createElement("div");
        message.className = "gap-card";
        message.textContent =
            error.message || "Unable to load gap analysis. Please try again.";

        container.appendChild(message);
    }
}



/* =========================================================
   PRIORITY RESOLUTION
========================================================= */


async function displayCombinations() {

    const container = document.getElementById("comboList");

    if (!container) {
        return;
    }

    container.innerHTML = "";

    // Get the logged-in student's ID
    const studentId = localStorage.getItem("studentId");

    if (!studentId) {
        container.textContent =
            "Please log in or register first to view your optimized scholarship combination.";
        return;
    }

    try {

        // Fetch the real optimization result from the backend
        const response = await fetch(
            `http://localhost:8080/api/scholarships/optimize/${studentId}`
        );

        if (!response.ok) {
            throw new Error(
                `Backend request failed with status ${response.status}`
            );
        }

        const result = await response.json();

        // Display the total estimated benefit
        const totalBenefit = Number(result.totalEstimatedBenefit || 0);

        const selectedScholarships =
            result.selectedScholarships || [];

        if (selectedScholarships.length === 0) {

            container.textContent =
                "No compatible scholarship combination was found for your profile.";

            return;
        }

        const card = document.createElement("div");
        card.className = "combo-card";

        // HEADER
        const header = document.createElement("div");
        header.className = "combo-header";

        const heading = document.createElement("div");

        const label = document.createElement("span");
        label.className = "combo-label";
        label.textContent = "RECOMMENDED COMBINATION";

        const title = document.createElement("h3");
        title.textContent = "Your Optimized Scholarships";

        heading.appendChild(label);
        heading.appendChild(title);

        // TOTAL
        const total = document.createElement("div");
        total.className = "combo-total";

        const totalLabel = document.createElement("span");
        totalLabel.textContent = "Estimated total benefit";

        const totalAmount = document.createElement("strong");
        totalAmount.textContent =
            "₹" + totalBenefit.toLocaleString("en-IN");

        total.appendChild(totalLabel);
        total.appendChild(totalAmount);

        header.appendChild(heading);
        header.appendChild(total);

        // SELECTED SCHOLARSHIPS
        const scholarshipContainer = document.createElement("div");
        scholarshipContainer.className = "combo-scholarships";

        selectedScholarships.forEach(function(scholarship) {

            const item = document.createElement("div");
            item.className = "combo-item";

            const name = document.createElement("div");
            name.className = "combo-item-name";
            name.textContent = scholarship.name;

            const amount = document.createElement("div");
            amount.className = "combo-item-amount";

            const currency = scholarship.currency || "INR";
            const value = Number(scholarship.amount || 0);

            amount.textContent =
                (currency === "INR" ? "₹" : currency + " ") +
                value.toLocaleString("en-IN") +
                (scholarship.amountPeriod
                    ? " / " + scholarship.amountPeriod
                    : "");

            item.appendChild(name);
            item.appendChild(amount);

            scholarshipContainer.appendChild(item);
        });

        // REASON
        const reason = document.createElement("p");
        reason.className = "combo-reason";
        reason.textContent =
            "This combination was selected by the scholarship optimization algorithm based on eligibility, estimated remaining benefit, and scholarship compatibility.";

        // STATUS
        const status = document.createElement("span");
        status.className = "combo-status";
        status.textContent = "Calculated using the backend optimizer";

        card.appendChild(header);
        card.appendChild(scholarshipContainer);
        card.appendChild(reason);
        card.appendChild(status);

        container.appendChild(card);

    } catch (error) {

        console.error("Error loading optimized scholarships:", error);

        container.textContent =
            "Unable to load optimized scholarships. Please ensure the backend is running and your student profile is available.";
    }
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