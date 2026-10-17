const scholarships = [

    // -------------------------
    // GOVERNMENT
    // -------------------------

    {
        name: "AICTE Saksham Scholarship Scheme for Specially Abled Student",
        value: "₹50,000 / year",
        deadline: "Check official portal",
        category: "government",
        link: "https://scholarships.gov.in/"
    },

    {
        name: "AICTE Pragati Scholarship Scheme for Girl Students",
        value: "₹50,000 / year",
        deadline: "Check official portal",
        category: "government",
        link: "https://scholarships.gov.in/"
    },

    {
        name: "Central Sector Scheme of Scholarship for College and University Students",
        value: "₹12,000 / year",
        deadline: "Check official portal",
        category: "government",
        link: "https://scholarships.gov.in/"
    },

    {
        name: "Merit-Cum-Means Scholarship for Professional and Technical Courses",
        value: "Amount not specified",
        deadline: "Check official portal",
        category: "government",
        link: "https://scholarships.gov.in/"
    },

    {
        name: "Post Matric Scholarship for Students with Disabilities",
        value: "Amount not specified",
        deadline: "Check official portal",
        category: "government",
        link: "https://scholarships.gov.in/"
    },

    {
        name: "Prime Minister's Scholarship Scheme for Central Armed Police Forces and Assam Rifles",
        value: "Amount not specified",
        deadline: "Check official portal",
        category: "government",
        link: "https://scholarships.gov.in/"
    },

    {
        name: "Prime Minister's Scholarship Scheme for Ministry of Railways",
        value: "Amount not specified",
        deadline: "Check official portal",
        category: "government",
        link: "https://scholarships.gov.in/"
    },

    {
        name: "BC/MBC & SC/ST Scholarship",
        value: "Amount not specified",
        deadline: "Check MIT scholarship notice",
        category: "government",
        link: "https://bcw.tn.gov.in/sub_page/8"
    },

    {
        name: "Financial Assistance for Education to the Wards of Beedi/Cine/IOMC/LSDM",
        value: "Amount not specified",
        deadline: "Check official portal",
        category: "government",
        link: "https://scholarships.gov.in/"
    },

    {
        name: "Pudhumai Penn Scheme",
        value: "₹1,000 / month",
        deadline: "Check official portal",
        category: "government",
        link: "https://tils.tn.gov.in/schemes"
    },

    {
        name: "PG Assistantship",
        value: "₹6,000 / month",
        deadline: "Check MIT/DOTE notice",
        category: "government",
        link: "#"
    },


    // -------------------------
    // CORPORATE
    // -------------------------

    {
        name: "Foundation for Excellence",
        value: "₹50,000 / year",
        deadline: "Check official portal",
        category: "corporate",
        link: "#"
    },

    {
        name: "Cognizant Foundation Scholarship",
        value: "₹45,000 / year",
        deadline: "Check MIT scholarship notice",
        category: "corporate",
        link: "#"
    },

    {
        name: "Student Welfare Endowment Scholarship",
        value: "₹25,000 / year",
        deadline: "Check MIT scholarship notice",
        category: "corporate",
        link: "#"
    },

    {
        name: "Bank of Tokyo Scholarship",
        value: "₹23,100 / year",
        deadline: "Check MIT scholarship notice",
        category: "corporate",
        link: "#"
    },

    {
        name: "Mugavari Scholarship",
        value: "Tuition + Hostel fees",
        deadline: "Check MIT scholarship notice",
        category: "corporate",
        link: "#"
    },

    {
        name: "AMM MurugappaChettiar Centenary Scholarship",
        value: "Amount not specified",
        deadline: "Check MIT scholarship notice",
        category: "corporate",
        link: "#"
    },

    {
        name: "Founders Family Scholarship",
        value: "₹15,000 / year",
        deadline: "Check MIT scholarship notice",
        category: "corporate",
        link: "#"
    },

    {
        name: "Bank of Hana (Korean) Scholarship",
        value: "USD $500",
        deadline: "Check MIT scholarship notice",
        category: "corporate",
        link: "#"
    },

    {
        name: "Dr. MGR & Quaide-milleth Scholarship",
        value: "₹8,000 / year",
        deadline: "Check MIT scholarship notice",
        category: "corporate",
        link: "#"
    },


    // -------------------------
    // ALUMNI
    // -------------------------

    {
        name: "AMITA Scholarship",
        value: "₹20,000 / year",
        deadline: "Check Alumni notification",
        category: "alumni",
        link: "#"
    }

];


const scholarshipList =
    document.getElementById("scholarshipList");


/* -------------------------
   DISPLAY SCHOLARSHIPS
------------------------- */

function displayScholarships(category = "all") {

    scholarshipList.innerHTML = "";

    let filteredScholarships = scholarships;

    if (category !== "all") {

        filteredScholarships =
            scholarships.filter(
                scholarship =>
                    scholarship.category === category
            );

    }


    filteredScholarships.forEach(
        (scholarship, index) => {

            const card =
                document.createElement("div");

            card.className =
                "scholarship-card";


            const number =
                String(index + 1).padStart(2, "0");


            let buttonText =
                "Official Portal →";


            if (scholarship.link === "#") {

                buttonText =
                    "Check MIT Notice →";

            }


            card.innerHTML = `

                <div class="scholarship-number">
                    #${number}
                </div>


                <div class="scholarship-info">

                    <div class="scholarship-name">
                        ${scholarship.name}
                    </div>


                    <div class="scholarship-meta">

                        <span class="scholarship-value">
                            ${scholarship.value}
                        </span>


                        <span class="scholarship-deadline">
                            Deadline: ${scholarship.deadline}
                        </span>


                        <span class="category-tag">
                            ${scholarship.category}
                        </span>

                    </div>

                </div>


                <a
                    class="portal-button"
                    href="${scholarship.link}"
                    ${scholarship.link !== "#" ? 'target="_blank"' : ""}
                >
                    ${buttonText}
                </a>

            `;


            scholarshipList.appendChild(card);

        }
    );

}


/* -------------------------
   CATEGORY FILTER
------------------------- */

const filterButtons =
    document.querySelectorAll(".filter-button");


filterButtons.forEach(button => {

    button.addEventListener("click", () => {

        filterButtons.forEach(btn => {

            btn.classList.remove("active");

        });


        button.classList.add("active");


        const category =
            button.dataset.category;


        displayScholarships(category);

    });

});


/* -------------------------
   PROFILE DROPDOWN
------------------------- */

const profileButton =
    document.getElementById("profileButton");

const profileDropdown =
    document.getElementById("profileDropdown");


profileButton.addEventListener("click", () => {

    profileDropdown.classList.toggle("show");

});


document.addEventListener("click", event => {

    if (
        !profileButton.contains(event.target) &&
        !profileDropdown.contains(event.target)
    ) {

        profileDropdown.classList.remove("show");

    }

});


/* -------------------------
   INITIAL LOAD
------------------------- */

displayScholarships();