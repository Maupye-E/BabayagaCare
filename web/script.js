// Babayaga Care - Main JavaScript
// This file handles UI interactions only (not data storage)
// All data is managed by server-side JSPs and database

// ============ MODAL FUNCTIONS ============

function showAddMedication() {
    const modal = document.getElementById('medicationModal');
    if (modal) modal.style.display = 'block';
}

function showAddSymptom() {
    const modal = document.getElementById('symptomModal');
    if (modal) modal.style.display = 'block';
}

function showHelpForm() {
    const modal = document.getElementById('helpModal');
    if (modal) modal.style.display = 'block';
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) modal.style.display = 'none';
}

function showLesson(lessonId) {
    window.location.href = 'lesson.jsp?id=' + lessonId;
}

function showVolunteerView() {
    window.location.href = 'volunteer.jsp';
}

// Show demo modal form (for index.html)
function showDemoForm() {
    const modal = document.getElementById('demoModal');
    if (modal) modal.style.display = 'block';
}

// Close demo modal
function closeDemoModal() {
    const modal = document.getElementById('demoModal');
    if (modal) modal.style.display = 'none';
}

// ============ LOGOUT FUNCTION ============

function logout() {
    if (confirm('Are you sure you want to logout?')) {
        window.location.href = 'logout.jsp';
    }
}

// ============ DELETE CONFIRMATION ============

function confirmDelete(message) {
    return confirm(message);
}

// ============ MESSAGE HANDLING ============

function autoHideMessage() {
    const msgBox = document.getElementById('messageBox');
    if (msgBox) {
        setTimeout(function() {
            msgBox.style.display = 'none';
        }, 3000);
    }
}

// ============ TAB SWITCHING (for login page) ============

function showPanel(panel) {
    // Get the clicked element from event
    const clickedElement = window.event ? window.event.target : null;
    
    // Update tabs
    const tabs = document.querySelectorAll('.tab');
    tabs.forEach(function(tab) {
        tab.classList.remove('active');
    });
    
    // Add active class to clicked tab
    if (clickedElement) {
        clickedElement.classList.add('active');
    }
    
    // Update panels
    const loginPanel = document.getElementById('loginPanel');
    const registerPanel = document.getElementById('registerPanel');
    
    if (panel === 'login') {
        if (loginPanel) loginPanel.classList.add('active');
        if (registerPanel) registerPanel.classList.remove('active');
    } else {
        if (loginPanel) loginPanel.classList.remove('active');
        if (registerPanel) registerPanel.classList.add('active');
    }
}

// ============ LOGIN FORM HANDLING ============

function handleLoginForm(event) {
    event.preventDefault();
    
    const phone = document.getElementById('loginPhone');
    const password = document.getElementById('loginPassword');
    
    if (!phone || phone.value.length < 10) {
        alert('❌ Please enter a valid 10-digit phone number!');
        if (phone) phone.focus();
        return false;
    }
    
    if (!password || password.value.length < 1) {
        alert('❌ Please enter your password!');
        if (password) password.focus();
        return false;
    }
    
    event.target.submit();
    return true;
}

// ============ INITIALIZE MODAL CLOSE ON OUTSIDE CLICK ============

function initModalCloseOnOutsideClick() {
    window.onclick = function(event) {
        // Removed 'lessonModal' since it's not used (redirects to lesson.jsp)
        const modalIds = ['medicationModal', 'symptomModal', 'helpModal', 'demoModal'];
        
        modalIds.forEach(function(modalId) {
            const modal = document.getElementById(modalId);
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        });
    };
}

// ============ PHONE NUMBER VALIDATION ============

function validatePhone(phone) {
    const phoneRegex = /^[0-9]{10}$/;
    return phoneRegex.test(phone);
}

// ============ FORM VALIDATION (for login page registration) ============

function validateRegistrationForm() {
    const name = document.getElementById('regName');
    const phone = document.getElementById('regPhone');
    const password = document.getElementById('regPassword');
    const confirmPassword = document.getElementById('regConfirmPassword');
    
    if (name && name.value.trim() === '') {
        alert('❌ Please enter your full name!');
        name.focus();
        return false;
    }
    
    if (phone && !validatePhone(phone.value)) {
        alert('❌ Please enter a valid 10-digit phone number!');
        phone.focus();
        return false;
    }
    
    if (password && password.value !== confirmPassword.value) {
        alert('❌ Passwords do not match!');
        password.focus();
        return false;
    }
    
    if (password && password.value.length < 6) {
        alert('❌ Password must be at least 6 characters long!');
        password.focus();
        return false;
    }
    
    return true;
}

// ============ CHANGE PASSWORD VALIDATION ============

function validateChangePasswordForm() {
    const oldPassword = document.getElementById('oldPassword');
    const newPassword = document.getElementById('newPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    
    if (oldPassword && oldPassword.value.length < 1) {
        alert('❌ Please enter your current password!');
        if (oldPassword) oldPassword.focus();
        return false;
    }
    
    if (newPassword && newPassword.value !== confirmPassword.value) {
        alert('❌ New passwords do not match!');
        newPassword.focus();
        return false;
    }
    
    if (newPassword && newPassword.value.length < 6) {
        alert('❌ Password must be at least 6 characters long!');
        newPassword.focus();
        return false;
    }
    
    return true;
}

// ============ FORGOT PASSWORD VALIDATION ============

function validateForgotPasswordForm() {
    const phone = document.getElementById('forgotPhone');
    const newPassword = document.getElementById('forgotNewPassword');
    const confirmPassword = document.getElementById('forgotConfirmPassword');
    
    if (phone && !validatePhone(phone.value)) {
        alert('❌ Please enter a valid 10-digit phone number!');
        if (phone) phone.focus();
        return false;
    }
    
    if (newPassword && newPassword.value !== confirmPassword.value) {
        alert('❌ Passwords do not match!');
        newPassword.focus();
        return false;
    }
    
    if (newPassword && newPassword.value.length < 6) {
        alert('❌ Password must be at least 6 characters long!');
        newPassword.focus();
        return false;
    }
    
    return true;
}

// ============ ADMIN RESET PASSWORD FUNCTIONS ============

function selectAdminUser(userId, userName) {
    const selectedUserId = document.getElementById('selectedUserId');
    const selectedUserInfo = document.getElementById('selectedUserInfo');
    const selectedUserName = document.getElementById('selectedUserName');
    
    if (selectedUserId) selectedUserId.value = userId;
    if (selectedUserName) selectedUserName.innerHTML = '👤 Selected: ' + userName;
    if (selectedUserInfo) selectedUserInfo.style.display = 'block';
    
    // Remove selected class from all user items
    const items = document.querySelectorAll('.user-item');
    items.forEach(function(item) {
        item.classList.remove('selected');
    });
    
    // Add selected class to clicked item
    const clickedItem = document.querySelector('.user-item[data-id="' + userId + '"]');
    if (clickedItem) {
        clickedItem.classList.add('selected');
    }
}

function validateAdminResetForm() {
    const newPassword = document.getElementById('adminNewPassword');
    const confirmPassword = document.getElementById('adminConfirmPassword');
    const selectedUserId = document.getElementById('selectedUserId');
    
    if (!selectedUserId || !selectedUserId.value) {
        alert('❌ Please select a user first!');
        return false;
    }
    
    if (newPassword && newPassword.value !== confirmPassword.value) {
        alert('❌ Passwords do not match!');
        newPassword.focus();
        return false;
    }
    
    if (newPassword && newPassword.value.length < 6) {
        alert('❌ Password must be at least 6 characters long!');
        newPassword.focus();
        return false;
    }
    
    return true;
}

// ============ DASHBOARD STATS ANIMATION ============

function animateStats() {
    const statNumbers = document.querySelectorAll('.stat-number');
    statNumbers.forEach(function(stat) {
        const target = parseInt(stat.innerText);
        if (!isNaN(target)) {
            let current = 0;
            const increment = target / 50;
            const timer = setInterval(function() {
                current += increment;
                if (current >= target) {
                    stat.innerText = target;
                    clearInterval(timer);
                } else {
                    stat.innerText = Math.floor(current);
                }
            }, 20);
        }
    });
}

// ============ ADMIN HELP REQUESTS FILTER ============

function filterHelpRequests(status) {
    window.location.href = 'admin_help_requests.jsp?filter=' + status;
}

// ============ TOGGLE USER STATUS ============

function toggleUserStatus(userId, currentStatus) {
    const action = currentStatus === 'active' ? 'deactivate' : 'activate';
    if (confirm('Are you sure you want to ' + action + ' this user?')) {
        window.location.href = 'admin_users.jsp?action=toggleActive&userId=' + userId;
    }
}

// ============ MAKE ADMIN ============

function makeAdmin(userId, userName) {
    if (confirm('Make ' + userName + ' an admin?')) {
        window.location.href = 'admin_users.jsp?action=makeAdmin&userId=' + userId;
    }
}

// ============ REMOVE ADMIN ============

function removeAdmin(userId, userName) {
    if (confirm('Remove admin privileges from ' + userName + '?')) {
        window.location.href = 'admin_users.jsp?action=removeAdmin&userId=' + userId;
    }
}

// ============ RESET PASSWORD DIRECT ============

function resetUserPassword(userId) {
    window.location.href = 'reset_password.jsp?userId=' + userId;
}

// ============ INITIALIZATION ============

document.addEventListener('DOMContentLoaded', function() {
    // Auto-hide message box on dashboard
    autoHideMessage();
    
    // Initialize modal close on outside click
    initModalCloseOnOutsideClick();
    
    // Animate stats on dashboard (only on pages with stat numbers)
    if (document.querySelector('.stat-number')) {
        animateStats();
    }
    
    // Set default tab on login page if tabs exist
    if (document.querySelector('.tab') && !document.querySelector('.tab.active')) {
        const firstTab = document.querySelector('.tab');
        if (firstTab) firstTab.classList.add('active');
        const loginPanel = document.getElementById('loginPanel');
        if (loginPanel) loginPanel.classList.add('active');
    }
});

// ============ HELPER FUNCTION FOR REPEATING CHARACTERS (Java 8 compatible) ============

function repeatChar(char, count) {
    let result = '';
    for (let i = 0; i < count; i++) {
        result += char;
    }
    return result;
}