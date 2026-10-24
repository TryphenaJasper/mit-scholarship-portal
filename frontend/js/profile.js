
const profileForm = document.getElementById("profileForm");
const successMessage = document.getElementById("successMessage");
const successText = document.getElementById("successText");


const API_URL = "https://mit-scholarship-portal.onrender.com/api/students";


function getValue(id) {
    return document.getElementById(id)?.value.trim() ?? "";
}

function getRadioValue(name) {
    return document.querySelector(
        `input[name="${name}"]:checked`
    )?.value ?? "";
}

function toBoolean(value) {
    return value === "yes";
}

function toNumber(value) {
    return value === "" ? null : Number(value);
}

function showError(message) {
    let errorMessage = document.getElementById("errorMessage");

    if (!errorMessage) {
        errorMessage = document.createElement("p");
        errorMessage.id = "errorMessage";
        errorMessage.setAttribute("role", "alert");
        errorMessage.style.color = "#c62828";
        profileForm.prepend(errorMessage);
    }

    errorMessage.textContent = message;
}

profileForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const errorMessage = document.getElementById("errorMessage");

    if (errorMessage) {
        errorMessage.textContent = "";
    }

    const registerNumber = getValue("registerNumber");
    const email = getValue("email");
    const fullName = getValue("fullName");
    const dateOfBirth = getValue("dateOfBirth");

    const branch = getValue("branch");
    const year = toNumber(getValue("year"));
    const semester = toNumber(getValue("semester"));

    const hsc = toNumber(getValue("hsc"));
    const class10 = toNumber(getValue("class10"));
    const cgpa = toNumber(getValue("cgpa"));
    const attendance = toNumber(getValue("attendance"));

    const class10InstitutionType = getValue("class10InstitutionType");
    const class12InstitutionType = getValue("class12InstitutionType");
    const class12CompletionYear = toNumber(
        getValue("class12CompletionYear")
    );

    const arrears = getRadioValue("arrears");
    const arrearCount = toNumber(getValue("arrearCount"));
    const gender = getValue("gender");
    const category = getValue("category");
    const religion = getValue("community");
    const income = toNumber(getValue("income"));

    const firstGraduate = getRadioValue("firstGraduate");
    const governmentSchool6To12 =
        getRadioValue("governmentSchool6To12");
    const governmentSchool10To12 =
        getRadioValue("governmentSchool10To12");

    const differentlyAbled = getRadioValue("differentlyAbled");
    const orphan = getRadioValue("orphan");
    const singleParent = getRadioValue("singleParent");
    const incomeCertificate = getRadioValue("incomeCertificate");
    const singleWindow = getRadioValue("singleWindow");
    const otherScholarship = getRadioValue("otherScholarship");

    // Validate that the new HTML fields are present.
    if (!email || !document.getElementById("email")) {
        showError("Please add and enter your email address.");
        return;
    }

    if (!document.getElementById("dateOfBirth") ||
        !document.getElementById("class10") ||
        !getRadioValue("orphan") ||
        !getRadioValue("singleParent") ||
        !getRadioValue("incomeCertificate") ||
        !governmentSchool6To12 ||
        !governmentSchool10To12) {
        showError(
            "Some updated profile fields are missing. " +
            "Please check profile.html before submitting."
        );
        return;
    }

    if (
        !registerNumber || !fullName || !branch ||
        !year || !semester || !gender || !category ||
        !religion || income === null ||
        !firstGraduate || !arrears || !differentlyAbled ||
        !singleWindow || !otherScholarship ||
        !class10InstitutionType ||
        !class12InstitutionType ||
        class12CompletionYear === null
    ) {
        showError("Please complete all required fields.");
        return;
    }

    if (arrears === "yes") {
    if (
        arrearCount === null ||
        !Number.isInteger(arrearCount) ||
        arrearCount < 1
    ) {
        showError("Please enter a valid number of standing arrears.");
        return;
    }
}
    // The selected year and semester must agree with each other.
    const validSemesters = {
        1: [1, 2],
        2: [3, 4],
        3: [5, 6],
        4: [7, 8]
    };

    if (!validSemesters[year]?.includes(semester)) {
        showError(
            "Please select a semester that matches your year of study."
        );
        return;
    }
    const currentYear = new Date().getFullYear();

    if (
        class12CompletionYear < 2000 ||
        class12CompletionYear > currentYear
    ) {
        showError(
            `Please enter a valid Class 12 completion year between 2000 and ${currentYear}.`
        );
        return;
    }

    if (
        governmentSchool6To12 === "yes" &&
        governmentSchool10To12 !== "yes"
    ) {
        showError(
            "If you studied in a government school from Classes 6–12, " +
            "you must also select Yes for Classes 10–12."
        );
        return;
    }

    // Convert the form's branch codes to the department names
    // expected by the backend.
    const departments = {
        CSE: "Computer Science and Engineering",
        IT: "Information Technology",
        ECE: "Electronics and Communication Engineering",
        EEE: "Electrical and Electronics Engineering",
        MECH: "Mechanical Engineering",
        AERO: "Aeronautical Engineering",
        "E&I": "Electronics and Instrumentation Engineering",
        AUTO: "Automobile Engineering"
    };

    const isGov6To12 = toBoolean(governmentSchool6To12);
    const isGov10To12 = toBoolean(governmentSchool10To12);

    const requestBody = {
        student: {
            registerNo: registerNumber,
            name: fullName,
            email: email,
            gender: gender,
            dateOfBirth: dateOfBirth || null,
            department: departments[branch] || branch,
            degree: "B.E.",
            yearOfStudy: year,
            semester: semester
        },

        background: {
            community: category,
            religion: religion,
            isFirstGraduate: toBoolean(firstGraduate),
            isDifferentlyAbled: toBoolean(differentlyAbled),
            isOrphan: toBoolean(orphan),
            hasSingleParent: toBoolean(singleParent),
            admissionMode: toBoolean(singleWindow)
                ? "SINGLE_WINDOW"
                : "OTHER"
        },

        financial: {
            parentalAnnualIncome: income,
            parentalMonthlyIncome: null,
            incomeCertificateAvailable:
                toBoolean(incomeCertificate)
        },

        education: {
            class10InstitutionType: class10InstitutionType,
            class12InstitutionType: class12InstitutionType,
            studiedGovClass6To12: isGov6To12,
            studiedGovClass10To12: isGov10To12,
            class10Percentage: class10,
            class12Percentage: hsc,
            class12CompletionYear: class12CompletionYear
        },
        academicRecords: [
            {
                semester: semester,
                cgpa: cgpa,
                attendancePercentage: attendance,
                arrearsCount: arrears === "yes" ? arrearCount : 0,
                conductGood: null,
                academicYear: null
            }
        ]
    };

    const submitButton = profileForm.querySelector(
        'button[type="submit"]'
    );

    try {
        submitButton.disabled = true;
        submitButton.textContent = "Creating profile...";

        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestBody)
        });

        const result = await response.json().catch(() => ({}));

        if (!response.ok) {
            throw new Error(
                result.error ||
                result.message ||
                `Registration failed (HTTP ${response.status}).`
            );
        }

        // Save the ID returned by the backend for My Profile.
        localStorage.setItem(
            "studentId",
            String(result.studentId)
        );

        // This answer is not currently stored in the backend.
        localStorage.setItem(
            "otherScholarship",
            otherScholarship
        );

        profileForm.style.display = "none";
        successMessage.style.display = "block";

        successText.textContent =
            `${fullName}, your scholarship profile has been created successfully.`;

    } catch (error) {
        console.error("Profile registration error:", error);

        showError(
            error.message ||
            "Unable to create your profile. Please try again."
        );
    } finally {
        if (submitButton) {
            submitButton.disabled = false;
            submitButton.textContent = "Create Profile";
        }
    }
});

const arrearsRadios = document.querySelectorAll(
    'input[name="arrears"]'
);

const arrearCountGroup = document.getElementById("arrearCountGroup");
const arrearCountInput = document.getElementById("arrearCount");

function toggleArrearCount() {
    const selectedArrears = getRadioValue("arrears");
    const showArrearCount = selectedArrears === "yes";

    if (arrearCountGroup) {
        arrearCountGroup.hidden = !showArrearCount;
    }

    if (arrearCountInput && !showArrearCount) {
        arrearCountInput.value = "";
    }
}

arrearsRadios.forEach((radio) => {
    radio.addEventListener("change", toggleArrearCount);
});

toggleArrearCount();

function goToDashboard() {
    window.location.href = "index.html";
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

    document.addEventListener("click", (event) => {
        if (
            !profileMenuButton.contains(event.target) &&
            !profileDropdown.contains(event.target)
        ) {
            profileDropdown.hidden = true;
            profileMenuButton.setAttribute("aria-expanded", "false");
        }
    });
}

// Dummy logout: closes the dropdown without logging out.
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