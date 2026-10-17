const profileForm = document.getElementById("profileForm");
const successMessage = document.getElementById("successMessage");
const successText = document.getElementById("successText");

profileForm.addEventListener("submit", function(event) {
    event.preventDefault();

    const profile = {
        registerNumber: document.getElementById("registerNumber").value,
        password: document.getElementById("password").value,
        fullName: document.getElementById("fullName").value,

        branch: document.getElementById("branch").value,
        year: document.getElementById("year").value,
        semester: document.getElementById("semester").value,
        hsc: document.getElementById("hsc").value,
        cgpa: document.getElementById("cgpa").value,
        attendance: document.getElementById("attendance").value,

        arrears: document.querySelector(
            'input[name="arrears"]:checked'
        ).value,

        gender: document.getElementById("gender").value,
        category: document.getElementById("category").value,
        community: document.getElementById("community").value,
        income: document.getElementById("income").value,

        firstGraduate: document.querySelector(
            'input[name="firstGraduate"]:checked'
        ).value,

        governmentSchool: document.querySelector(
            'input[name="governmentSchool"]:checked'
        ).value,

        differentlyAbled: document.querySelector(
            'input[name="differentlyAbled"]:checked'
        ).value,

        singleWindow: document.querySelector(
            'input[name="singleWindow"]:checked'
        ).value,

        otherScholarship: document.querySelector(
            'input[name="otherScholarship"]:checked'
        ).value
    };

    localStorage.setItem(
        "studentProfile",
        JSON.stringify(profile)
    );

    profileForm.style.display = "none";

    successMessage.style.display = "block";

    successText.textContent =
        `${profile.fullName}, your scholarship profile has been created successfully.`;
});

function goToDashboard() {
    window.location.href = "index.html";
}