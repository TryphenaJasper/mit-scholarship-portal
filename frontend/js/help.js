/* =========================
   PROFILE DROPDOWN
========================= */

const profileButton =
    document.getElementById("profileButton");

const profileDropdown =
    document.getElementById("profileDropdown");


profileButton.addEventListener("click", function (event) {

    event.stopPropagation();

    profileDropdown.classList.toggle("show");

});


document.addEventListener("click", function (event) {

    if (
        !profileButton.contains(event.target) &&
        !profileDropdown.contains(event.target)
    ) {

        profileDropdown.classList.remove("show");

    }

});



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