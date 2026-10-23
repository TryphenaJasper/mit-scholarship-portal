
/* =========================
   PROFILE DROPDOWN
========================= */

const profileMenuButton =
    document.getElementById("profileMenuButton");

const profileDropdown =
    document.getElementById("profileDropdown");

const logoutButton =
    document.getElementById("logoutButton");

if (profileMenuButton && profileDropdown) {

    profileMenuButton.addEventListener("click", function (event) {
        event.stopPropagation();

        const isOpening = profileDropdown.hidden;
        profileDropdown.hidden = !isOpening;

        profileMenuButton.setAttribute(
            "aria-expanded",
            String(isOpening)
        );
    });

    document.addEventListener("click", function (event) {
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
        logoutButton.addEventListener("click", function () {
            profileDropdown.hidden = true;

            profileMenuButton.setAttribute(
                "aria-expanded",
                "false"
            );
        });
    }
}



/* =========================
   FAQ ACCORDION
========================= */

const faqQuestions =
    document.querySelectorAll(".faq-question");


faqQuestions.forEach(function (question) {

    question.addEventListener("click", function () {

        const currentItem =
            question.parentElement;


        /*
         * Close other FAQ items
         */

        document
            .querySelectorAll(".faq-item")
            .forEach(function (item) {

                if (item !== currentItem) {

                    item.classList.remove("open");

                }

            });


        /*
         * Toggle current FAQ
         */

        currentItem.classList.toggle("open");

    });

});