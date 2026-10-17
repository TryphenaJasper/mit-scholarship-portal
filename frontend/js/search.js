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

function displayApplicableScholarships() {

    const container =
        document.getElementById(
            "applicableScholarships"
        );

    if (!container) {
        return;
    }

    container.innerHTML = "";


    const sorted =
        [...applicableScholarships].sort(
            function(a, b) {
                return b.value - a.value;
            }
        );


    sorted.forEach(
        function(scholarship, index) {

            const card =
                document.createElement("div");

            card.className =
                "scholarship-card";


            /* PRIORITY NUMBER */

            const priority =
                document.createElement("div");

            priority.className =
                "priority-number";

            priority.textContent =
                "#" + (index + 1);


            /* MAIN CONTENT */

            const main =
                document.createElement("div");

            main.className =
                "scholarship-main";


            const title =
                document.createElement("h3");

            title.className =
                "scholarship-title";

            title.textContent =
                scholarship.name;


            const info =
                document.createElement("div");

            info.className =
                "scholarship-info";


            const amount =
                document.createElement("span");

            amount.className =
                "money";

            amount.textContent =
                scholarship.amount;


            const deadline =
                document.createElement("span");

            deadline.textContent =
                "Deadline: " +
                scholarship.deadline;


            info.appendChild(amount);

            info.appendChild(deadline);


            main.appendChild(title);

            main.appendChild(info);


            /* OFFICIAL LINK */

            const link =
                document.createElement("a");

            link.className =
                "scholarship-link";

            link.textContent =
                "Official Portal →";

            link.href =
                scholarship.link;


            if (scholarship.link !== "#") {

                link.target = "_blank";

                link.rel =
                    "noopener noreferrer";
            }


            card.appendChild(priority);

            card.appendChild(main);

            card.appendChild(link);


            container.appendChild(card);
        }
    );
}


/* =========================================================
   GAP ANALYSIS
========================================================= */

function displayGapAnalysis() {

    /*
       YOUR HTML uses:

       id="gapList"

       So we MUST use gapList here.
    */

    const container =
        document.getElementById("gapList");


    if (!container) {

        console.error(
            "gapList was not found in search.html"
        );

        return;
    }


    container.innerHTML = "";


    /*
       Get names of scholarships already shown
       in Applicable Scholarships.
    */

    const applicableNames =
        new Set(
            applicableScholarships.map(
                function(scholarship) {
                    return scholarship.name;
                }
            )
        );


    /*
       Remove scholarships that are already applicable.
    */

    const possibleGaps =
        scholarshipRequirements.filter(
            function(scholarship) {

                return !applicableNames.has(
                    scholarship.name
                );
            }
        );


    /*
       Now check whether the scholarship actually
       has an academic gap.

       Only:
       - CGPA
       - HSC
       - Attendance

       are considered.

       Income and disability are ignored.
    */

    const actualGaps =
        possibleGaps.filter(
            function(scholarship) {

                return scholarship.requirements.some(
                    function(requirement) {

                        return (
                            requirement.current <
                            requirement.required
                        );
                    }
                );
            }
        );


    /*
       Highest scholarship amount first.
    */

    actualGaps.sort(
        function(a, b) {

            return b.value - a.value;
        }
    );


    /* =====================================================
       CREATE GAP CARDS
    ===================================================== */

    actualGaps.forEach(
        function(scholarship) {

            const card =
                document.createElement("div");

            card.className =
                "gap-card";


            /* ---------- HEADER ---------- */

            const header =
                document.createElement("div");

            header.className =
                "gap-card-header";


            const heading =
                document.createElement("div");


            const title =
                document.createElement("h3");

            title.textContent =
                scholarship.name;


            const subtitle =
                document.createElement("p");

            subtitle.className =
                "gap-card-subtitle";

            subtitle.textContent =
                "Academic requirements to improve";


            heading.appendChild(title);

            heading.appendChild(subtitle);


            /* ---------- AMOUNT ---------- */

            const money =
                document.createElement("div");

            money.className =
                "gap-scholarship-money";

            money.textContent =
                scholarship.amount;


            header.appendChild(heading);

            header.appendChild(money);


            /* ---------- REQUIREMENTS ---------- */

            const requirements =
                document.createElement("div");

            requirements.className =
                "gap-requirements";


            scholarship.requirements.forEach(
                function(requirement) {

                    /*
                       Only display an actual gap.

                       If Jasper already satisfies
                       this requirement, don't show it.
                    */

                    if (
                        requirement.current >=
                        requirement.required
                    ) {
                        return;
                    }


                    const item =
                        document.createElement("div");

                    item.className =
                        "gap-item";


                    const label =
                        document.createElement("span");

                    label.className =
                        "gap-item-label";

                    label.textContent =
                        requirement.type;


                    const required =
                        document.createElement("div");

                    required.className =
                        "gap-item-value";

                    required.textContent =
                        requirement.requiredText;


                    const current =
                        document.createElement("div");

                    current.className =
                        "gap-card-subtitle";

                    current.textContent =
                        requirement.currentText;


                    const gap =
                        document.createElement("div");

                    gap.className =
                        "gap-value";

                    gap.textContent =
                        requirement.gapText;


                    item.appendChild(label);

                    item.appendChild(required);

                    item.appendChild(current);

                    item.appendChild(gap);


                    requirements.appendChild(item);
                }
            );


            /* ---------- INCOME ---------- */

            const income =
                document.createElement("p");

            income.className =
                "gap-income";

            income.textContent =
                scholarship.income;


            /*
               Income is displayed ONLY as information.
               It does not contribute to the gap.
            */


            /* ---------- ADD TO CARD ---------- */

            card.appendChild(header);

            card.appendChild(requirements);

            card.appendChild(income);


            container.appendChild(card);
        }
    );


    /* =====================================================
       NO GAPS MESSAGE
    ===================================================== */

    if (actualGaps.length === 0) {

        const message =
            document.createElement("div");

        message.className =
            "gap-card";


        const title =
            document.createElement("h3");

        title.textContent =
            "No academic gaps found";


        const text =
            document.createElement("p");

        text.className =
            "gap-card-subtitle";

        text.textContent =
            "You currently meet the academic requirements of all scholarships considered in the gap analysis.";


        message.appendChild(title);

        message.appendChild(text);


        container.appendChild(message);
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

    const profileButton =
        document.getElementById(
            "profileButton"
        );


    const profileDropdown =
        document.getElementById(
            "profileDropdown"
        );


    if (
        !profileButton ||
        !profileDropdown
    ) {
        return;
    }


    profileButton.addEventListener(
        "click",
        function(event) {

            event.stopPropagation();

            profileDropdown.classList.toggle(
                "show"
            );
        }
    );


    document.addEventListener(
        "click",
        function(event) {

            if (
                !event.target.closest(
                    ".profile-menu"
                )
            ) {

                profileDropdown.classList.remove(
                    "show"
                );
            }
        }
    );
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