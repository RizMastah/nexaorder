document.addEventListener("DOMContentLoaded", function () {
    if (!window.gsap) {
        return;
    }

    gsap.from("[data-animate='auth-card']", {
        duration: 0.45,
        y: 18,
        opacity: 0,
        ease: "power2.out"
    });

    gsap.from("[data-animate='page']", {
        duration: 0.4,
        y: 14,
        opacity: 0,
        ease: "power2.out"
    });

    gsap.from("[data-animate='dashboard-card']", {
        duration: 0.42,
        y: 16,
        opacity: 0,
        stagger: 0.06,
        ease: "power2.out"
    });

    gsap.from("[data-animate='product-table']", {
        duration: 0.42,
        y: 16,
        opacity: 0,
        ease: "power2.out"
    });

    gsap.from("[data-animate='product-card']", {
        duration: 0.45,
        y: 18,
        opacity: 0,
        stagger: 0.06,
        ease: "power2.out"
    });

    gsap.from("[data-animate='product-form']", {
        duration: 0.42,
        y: 16,
        opacity: 0,
        ease: "power2.out"
    });

    gsap.from("[data-animate='inventory-panel']", {
        duration: 0.42,
        y: 16,
        opacity: 0,
        stagger: 0.05,
        ease: "power2.out"
    });

    gsap.from("[data-animate='order-summary']", {
        duration: 0.42,
        y: 16,
        opacity: 0,
        ease: "power2.out"
    });

    gsap.from("[data-animate='order-list']", {
        duration: 0.42,
        y: 16,
        opacity: 0,
        stagger: 0.05,
        ease: "power2.out"
    });

    gsap.from("[data-animate='payment-card']", {
        duration: 0.42,
        y: 16,
        opacity: 0,
        stagger: 0.06,
        ease: "power2.out"
    });

    gsap.from("[data-animate='payment-panel']", {
        duration: 0.42,
        y: 14,
        opacity: 0,
        ease: "power2.out"
    });

    gsap.from("[data-animate='order-timeline'] > div", {
        duration: 0.35,
        x: -10,
        opacity: 0,
        stagger: 0.05,
        ease: "power2.out"
    });

    gsap.from("[data-animate='notification-item']", {
        duration: 0.4,
        y: 14,
        opacity: 0,
        stagger: 0.05,
        ease: "power2.out"
    });

    gsap.from("[data-animate='status-badge']", {
        duration: 0.3,
        scale: 0.96,
        opacity: 0,
        ease: "power2.out"
    });
});
