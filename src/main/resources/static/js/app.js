window.nexaLayout = function () {
    return {
        sidebarOpen: false,
        profileOpen: false,
        notificationsOpen: false,
        confirmModalOpen: false,
        toggleSidebar: function () {
            this.sidebarOpen = !this.sidebarOpen;
        },
        closeSidebar: function () {
            this.sidebarOpen = false;
        }
    };
};

window.nexaAuth = function () {
    return {
        showPassword: false,
        showConfirmPassword: false
    };
};

window.productImagePreview = function (initialUrl) {
    return {
        previewUrl: initialUrl || "",
        previewImage: function (event) {
            var file = event.target.files && event.target.files[0];
            if (!file) {
                this.previewUrl = initialUrl || "";
                return;
            }

            this.previewUrl = URL.createObjectURL(file);
        }
    };
};

window.orderQuantity = function (maxStock, price, initialQuantity) {
    return {
        maxStock: Number(maxStock || 0),
        price: Number(price || 0),
        quantity: Number(initialQuantity || 1),
        increase: function () {
            if (this.quantity < this.maxStock) {
                this.quantity += 1;
            }
        },
        decrease: function () {
            if (this.quantity > 1) {
                this.quantity -= 1;
            }
        },
        isValid: function () {
            return this.quantity > 0 && this.quantity <= this.maxStock;
        },
        subtotal: function () {
            return this.price * Math.max(this.quantity || 0, 0);
        },
        formattedSubtotal: function () {
            return new Intl.NumberFormat("id-ID", {
                style: "currency",
                currency: "IDR",
                minimumFractionDigits: 2
            }).format(this.subtotal());
        }
    };
};

document.addEventListener("DOMContentLoaded", function () {
    var currentPath = window.location.pathname;
    document.querySelectorAll("[data-nav-link]").forEach(function (link) {
        var target = link.getAttribute("data-nav-link");
        if (target && currentPath.indexOf(target) === 0) {
            link.classList.add("nexa-nav-link-active");
        }
    });
});
