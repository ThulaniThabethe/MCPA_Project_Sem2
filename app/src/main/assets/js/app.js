document.addEventListener('DOMContentLoaded', function() {
    // This is the main entry point for your web app logic
    console.log('Woolworths Home Page loaded successfully within the Android WebView.');
    
    // Example: Add a simple click handler to the menu toggle
    const menuToggle = document.querySelector('.menu-toggle');
    if (menuToggle) {
        menuToggle.addEventListener('click', function(event) {
            event.preventDefault(); // Stop the link from navigating
            alert('Menu button clicked! (Will open a side drawer later)');
            // In a real app, this would trigger a side menu or navigation action
        });
    }
});

// Any other future JavaScript functions (like banner sliding logic) will go here.

// 5. FOOTER ACCORDION LOGIC
document.addEventListener('DOMContentLoaded', function() {
    const accordionHeaders = document.querySelectorAll('.accordion-header');

    accordionHeaders.forEach(header => {
        header.addEventListener('click', function() {
            // Toggle active class on the header
            this.classList.toggle('active');

            // Get the content panel
            const content = this.nextElementSibling;

            // Toggle the max-height to create the expand/collapse effect
            if (content.style.maxHeight) {
                content.style.maxHeight = null; // Collapse
            } else {
                content.style.maxHeight = content.scrollHeight + "px"; // Expand
            }
        });
    });
});