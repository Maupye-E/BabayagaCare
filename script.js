// Babayaga Care - Main JavaScript (Demo Version for GitHub Pages)
// This file handles UI interactions for the demo version

// ============ MESSAGE DISPLAY FUNCTION ============
function showMessage(message, type) {
  const messageBox = document.getElementById("messageBox");
  if (messageBox) {
    messageBox.textContent = message;
    messageBox.className = "message " + type;
    messageBox.style.display = "block";

    // Scroll to message
    messageBox.scrollIntoView({ behavior: "smooth", block: "center" });

    // Hide after 3 seconds
    setTimeout(function () {
      messageBox.style.display = "none";
    }, 3000);
  } else {
    alert(message);
  }
}

// ============ MODAL FUNCTIONS ============
function showAddMedication() {
  const modal = document.getElementById("medicationModal");
  if (modal) modal.style.display = "flex";
}

function showAddSymptom() {
  const modal = document.getElementById("symptomModal");
  if (modal) modal.style.display = "flex";
}

function showHelpForm() {
  const modal = document.getElementById("helpModal");
  if (modal) modal.style.display = "flex";
}

function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) modal.style.display = "none";
}

// Show lesson (demo version)
function showLesson(lessonId) {
  window.location.href = "lesson.html?id=" + lessonId;
}

function showLessonDemo(title) {
  showMessage(
    `📚 Lesson: ${title}\n\nIn the full app, you would see the complete health education content here.`,
    "success",
  );
}

function showVolunteerView() {
  window.location.href = "volunteer.html";
}

// Show demo modal form (for index.html)
function showDemoForm() {
  const modal = document.getElementById("demoModal");
  if (modal) modal.style.display = "flex";
}

// Close demo modal
function closeDemoModal() {
  const modal = document.getElementById("demoModal");
  if (modal) modal.style.display = "none";
}

// ============ LOGOUT FUNCTIONS ============
function logout() {
  if (confirm("Are you sure you want to logout?")) {
    sessionStorage.clear();
    localStorage.clear();
    window.location.href = "logout.html";
  }
}

function demoLogout() {
  if (confirm("Are you sure you want to logout?")) {
    sessionStorage.clear();
    localStorage.clear();
    window.location.href = "logout.html";
  }
}

// ============ DELETE CONFIRMATION ============
function confirmDelete(message) {
  return confirm(message);
}

// ============ MESSAGE HANDLING ============
function autoHideMessage() {
  const msgBox = document.getElementById("messageBox");
  if (msgBox) {
    setTimeout(function () {
      msgBox.style.display = "none";
    }, 3000);
  }
}

// ============ TAB SWITCHING (for login page) ============
function showPanel(panel) {
  // Update tabs
  const tabs = document.querySelectorAll(".tab");
  tabs.forEach(function (tab, index) {
    tab.classList.remove("active");
  });

  // Update panels
  const loginPanel = document.getElementById("loginPanel");
  const registerPanel = document.getElementById("registerPanel");

  if (panel === "login") {
    if (loginPanel) loginPanel.classList.add("active");
    if (registerPanel) registerPanel.classList.remove("active");
    if (tabs[0]) tabs[0].classList.add("active");
  } else {
    if (loginPanel) loginPanel.classList.remove("active");
    if (registerPanel) registerPanel.classList.add("active");
    if (tabs[1]) tabs[1].classList.add("active");
  }
}

// ============ DEMO LOGIN HANDLER ============
function handleDemoLogin(event) {
  event.preventDefault();

  const phone = document.getElementById("loginPhone").value;
  const password = document.getElementById("loginPassword").value;

  if (phone && password) {
    // Determine user role based on phone number (demo logic)
    let userRole = "patient";
    let userName = "Demo User";
    let userLocation = "Soweto, Johannesburg";

    // Demo: specific phone numbers for different roles
    if (phone === "0834567890") {
      userRole = "admin";
      userName = "Admin User";
      userLocation = "Head Office";
    } else if (phone === "0723456789") {
      userRole = "volunteer";
      userName = "Mary Jones";
      userLocation = "Khayelitsha, Cape Town";
    } else if (phone === "0745678901") {
      userRole = "volunteer";
      userName = "Sarah Mbeki";
      userLocation = "Pretoria";
    } else {
      userRole = "patient";
      userName = "Thabo Nkosi";
      userLocation = "Soweto, Johannesburg";
    }

    // Save user data to session storage
    const userData = {
      id: 1,
      name: userName,
      phone: phone,
      role: userRole,
      location: userLocation,
      language: "English",
    };
    sessionStorage.setItem("demoUser", JSON.stringify(userData));

    showMessage(
      "✅ Demo login successful! Redirecting to dashboard...",
      "success",
    );

    setTimeout(function () {
      window.location.href = "dashboard.html";
    }, 1500);
  } else {
    showMessage("❌ Please enter phone number and password", "error");
  }

  return false;
}

// ============ DEMO REGISTER HANDLER ============
function handleDemoRegister(event) {
  event.preventDefault();

  const name = document.getElementById("regName").value;
  const phone = document.getElementById("regPhone").value;
  const password = document.getElementById("regPassword").value;
  const confirmPassword = document.getElementById("regConfirmPassword").value;

  if (password !== confirmPassword) {
    showMessage("❌ Passwords do not match!", "error");
    return false;
  }

  if (password.length < 6) {
    showMessage("❌ Password must be at least 6 characters long!", "error");
    return false;
  }

  if (!validatePhone(phone)) {
    showMessage("❌ Please enter a valid 10-digit phone number!", "error");
    return false;
  }

  if (name && phone && password) {
    showMessage("✅ Demo registration successful! Please login.", "success");

    // Clear form
    document.getElementById("registerForm").reset();

    // Switch to login tab
    setTimeout(function () {
      showPanel("login");
    }, 1500);
  } else {
    showMessage("❌ Please fill in all required fields", "error");
  }

  return false;
}

// ============ DEMO FORGOT PASSWORD HANDLER ============
function handleDemoForgotPassword(event) {
  event.preventDefault();

  const phone = document.getElementById("forgotPhone").value;
  const newPassword = document.getElementById("forgotNewPassword").value;
  const confirmPassword = document.getElementById(
    "forgotConfirmPassword",
  ).value;

  if (!validatePhone(phone)) {
    showMessage("❌ Please enter a valid 10-digit phone number!", "error");
    return false;
  }

  if (newPassword !== confirmPassword) {
    showMessage("❌ Passwords do not match!", "error");
    return false;
  }

  if (newPassword.length < 6) {
    showMessage("❌ Password must be at least 6 characters long!", "error");
    return false;
  }

  showMessage(
    "✅ Demo: Password reset successful! Redirecting to login...",
    "success",
  );

  document.getElementById("forgotPasswordForm").reset();

  setTimeout(function () {
    window.location.href = "login.html";
  }, 2000);

  return false;
}

// ============ DEMO CHANGE PASSWORD HANDLER ============
function handleDemoChangePassword(event) {
  event.preventDefault();

  const oldPassword = document.getElementById("oldPassword").value;
  const newPassword = document.getElementById("newPassword").value;
  const confirmPassword = document.getElementById("confirmPassword").value;

  if (!oldPassword) {
    showMessage("❌ Please enter your current password", "error");
    return false;
  }

  if (newPassword !== confirmPassword) {
    showMessage("❌ New passwords do not match!", "error");
    return false;
  }

  if (newPassword.length < 6) {
    showMessage("❌ New password must be at least 6 characters!", "error");
    return false;
  }

  showMessage(
    "✅ Password changed successfully! Redirecting to login...",
    "success",
  );

  document.getElementById("changePasswordForm").reset();

  setTimeout(function () {
    window.location.href = "login.html";
  }, 2000);

  return false;
}

// ============ PHONE NUMBER VALIDATION ============
function validatePhone(phone) {
  const phoneRegex = /^[0-9]{10}$/;
  return phoneRegex.test(phone);
}

// ============ DASHBOARD FUNCTIONS ============

// Medication functions
function markTaken(button) {
  const medItem = button.closest(".medication-item");
  const statusDiv = medItem.querySelector(".med-status");
  statusDiv.innerHTML = "✅ Taken today";
  statusDiv.style.color = "green";
  button.disabled = true;
  showMessage("✅ Medication marked as taken!", "success");
}

function deleteMedication(button) {
  if (confirm("Delete this medication?")) {
    const medItem = button.closest(".medication-item");
    medItem.remove();
    updateMedicationCount();
    showMessage("✅ Medication deleted!", "success");
  }
}

function updateMedicationCount() {
  const count = document.querySelectorAll(
    "#medicationList .medication-item",
  ).length;
  const medCountElem = document.getElementById("medCount");
  if (medCountElem) medCountElem.innerText = count;
}

function addMedicationDemo(event) {
  event.preventDefault();

  const name = document.getElementById("medName").value;
  const dosage = document.getElementById("medDosage").value;
  const time = document.getElementById("medTime").value;
  const instructions = document.getElementById("medInstructions").value;

  const medicationList = document.getElementById("medicationList");

  const newMed = document.createElement("div");
  newMed.className = "medication-item";
  newMed.innerHTML = `
        <div class="item-header">
            <strong>💊 ${escapeHtml(name)}</strong>
            <div class="item-actions">
                <button onclick="markTaken(this)" class="btn-icon" title="Mark as taken">✅</button>
                <button onclick="deleteMedication(this)" class="btn-icon" title="Delete">🗑️</button>
            </div>
        </div>
        <div class="item-details">
            <div>💊 Dosage: ${escapeHtml(dosage)}</div>
            <div>⏰ Time: ${escapeHtml(time)}</div>
            ${instructions ? `<div>📝 ${escapeHtml(instructions)}</div>` : ""}
            <div class="med-status">⭕ Not taken yet</div>
        </div>
    `;

  medicationList.appendChild(newMed);
  updateMedicationCount();
  closeModal("medicationModal");
  document.getElementById("addMedicationForm").reset();
  showMessage("✅ Medication added successfully!", "success");

  return false;
}

// Symptom functions
function deleteSymptom(button) {
  if (confirm("Delete this symptom record?")) {
    const symptomItem = button.closest(".symptom-item");
    symptomItem.remove();
    updateSymptomCount();
    showMessage("✅ Symptom deleted!", "success");
  }
}

function updateSymptomCount() {
  const count = document.querySelectorAll("#symptomList .symptom-item").length;
  const symCountElem = document.getElementById("symCount");
  if (symCountElem) symCountElem.innerText = count;
}

function addSymptomDemo(event) {
  event.preventDefault();

  const name = document.getElementById("symptomName").value;
  const severity = document.getElementById("symptomSeverity").value;
  const notes = document.getElementById("symptomNotes").value;

  const severityMap = {
    1: "⭐ (Mild)",
    2: "⭐⭐ (Moderate)",
    3: "⭐⭐⭐ (Uncomfortable)",
    4: "⭐⭐⭐⭐ (Severe)",
    5: "⭐⭐⭐⭐⭐ (Very Severe)",
  };

  const symptomList = document.getElementById("symptomList");
  const today = new Date().toISOString().slice(0, 10);

  const newSymptom = document.createElement("div");
  newSymptom.className = "symptom-item";
  newSymptom.innerHTML = `
        <div class="item-header">
            <strong>🤕 ${escapeHtml(name)}</strong>
            <button onclick="deleteSymptom(this)" class="btn-icon" title="Delete">🗑️</button>
        </div>
        <div class="item-details">
            <div>Severity: ${severityMap[severity]}</div>
            <div>📅 ${today}</div>
            ${notes ? `<div>📝 ${escapeHtml(notes)}</div>` : ""}
        </div>
    `;

  symptomList.appendChild(newSymptom);
  updateSymptomCount();
  closeModal("symptomModal");
  document.getElementById("addSymptomForm").reset();
  showMessage("✅ Symptom logged successfully!", "success");

  return false;
}

// Help request functions
function addHelpRequestDemo(event) {
  event.preventDefault();

  const type = document.getElementById("helpType").value;
  const location = document.getElementById("helpLocation").value;
  const details = document.getElementById("helpDetails").value;
  const urgent = document.getElementById("helpUrgent").checked;

  const typeMap = {
    medication: "💊 Medication Pickup",
    clinic: "🚗 Clinic/Hospital Ride",
    food: "🍲 Food Parcel",
    care: "👵 Home Care Check-in",
    other: "📞 Other",
  };

  const helpList = document.getElementById("helpRequestList");
  const today = new Date().toISOString().slice(0, 10);
  const urgentText = urgent ? " (Urgent)" : "";

  const newRequest = document.createElement("div");
  newRequest.className = "help-item";
  newRequest.innerHTML = `
        <strong>${typeMap[type]}${urgentText}</strong>
        <div class="help-status">Status: Pending</div>
        <div class="help-date">📅 ${today}</div>
    `;

  helpList.appendChild(newRequest);
  updateHelpCount();
  closeModal("helpModal");
  document.getElementById("helpRequestForm").reset();
  showMessage(
    "✅ Help request sent! A volunteer will contact you soon.",
    "success",
  );

  return false;
}

function updateHelpCount() {
  const count = document.querySelectorAll("#helpRequestList .help-item").length;
  const helpCountElem = document.getElementById("helpCount");
  if (helpCountElem) helpCountElem.innerText = count;
}

// ============ ESCAPE HTML TO PREVENT XSS ============
function escapeHtml(str) {
  if (!str) return "";
  return str
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#39;");
}

// ============ VOLUNTEER DASHBOARD FUNCTIONS ============

let openRequestsData = [
  {
    id: 101,
    type: "medication",
    typeIcon: "💊",
    typeText: "Medication Pickup",
    location: "Soweto, Johannesburg",
    details: "Need help picking up ARV medication from clinic.",
    requestDate: "2024-06-05",
    urgent: true,
    status: "open",
  },
  {
    id: 102,
    type: "clinic",
    typeIcon: "🚗",
    typeText: "Clinic/Hospital Ride",
    location: "Khayelitsha, Cape Town",
    details: "Need a ride to the clinic for my monthly check-up.",
    requestDate: "2024-06-05",
    urgent: false,
    status: "open",
  },
  {
    id: 103,
    type: "food",
    typeIcon: "🍲",
    typeText: "Food Parcel",
    location: "Durban",
    details: "Need food parcel for a family of 4.",
    requestDate: "2024-06-04",
    urgent: true,
    status: "open",
  },
];

let myRequestsData = [
  {
    id: 201,
    type: "medication",
    typeIcon: "💊",
    typeText: "Medication Pickup",
    location: "Soweto, Johannesburg",
    details: "Help picking up medication from clinic.",
    requestDate: "2024-06-02",
    urgent: false,
    status: "assigned",
  },
];

function renderOpenRequests() {
  const container = document.getElementById("openRequestsContainer");
  if (!container) return;

  if (openRequestsData.length === 0) {
    container.innerHTML = "<p>No open requests at the moment.</p>";
    return;
  }

  container.innerHTML = openRequestsData
    .map(
      (req) => `
        <div class="request-card ${req.urgent ? "urgent" : ""}">
            <div class="request-header">
                <div class="request-info">
                    <strong>${req.typeIcon} ${req.typeText}</strong>
                    ${req.urgent ? '<span class="badge badge-urgent">🚨 URGENT</span>' : ""}
                    <span class="badge badge-open">open</span>
                </div>
                <button onclick="acceptRequest(${req.id})" class="btn btn-primary btn-small">Accept</button>
            </div>
            <div class="request-details">
                <div>📍 ${escapeHtml(req.location)}</div>
                <div>📝 ${escapeHtml(req.details)}</div>
                <div>📅 ${req.requestDate}</div>
            </div>
        </div>
    `,
    )
    .join("");
}

function renderMyRequests() {
  const container = document.getElementById("myRequestsContainer");
  if (!container) return;

  if (myRequestsData.length === 0) {
    container.innerHTML = "<p>You haven't accepted any requests yet.</p>";
    return;
  }

  container.innerHTML = myRequestsData
    .map(
      (req) => `
        <div class="request-card">
            <div class="request-header">
                <div class="request-info">
                    <strong>${req.typeIcon} ${req.typeText}</strong>
                    <span class="badge badge-assigned">${req.status}</span>
                </div>
                ${req.status === "assigned" ? `<button onclick="completeRequest(${req.id})" class="btn btn-success btn-small">Mark Complete</button>` : ""}
            </div>
            <div class="request-details">
                <div>📍 ${escapeHtml(req.location)}</div>
                <div>📝 ${escapeHtml(req.details)}</div>
                <div>📅 ${req.requestDate}</div>
            </div>
        </div>
    `,
    )
    .join("");
}

function acceptRequest(requestId) {
  const request = openRequestsData.find((r) => r.id === requestId);
  if (
    request &&
    confirm(
      `Accept this request?\n\n${request.typeIcon} ${request.typeText}\n📍 ${request.location}`,
    )
  ) {
    openRequestsData = openRequestsData.filter((r) => r.id !== requestId);
    myRequestsData.push({ ...request, status: "assigned" });
    renderOpenRequests();
    renderMyRequests();
    showMessage(
      `✅ You have accepted the ${request.typeText} request!`,
      "success",
    );
  }
}

function completeRequest(requestId) {
  const request = myRequestsData.find((r) => r.id === requestId);
  if (request && confirm(`Mark this request as completed?`)) {
    request.status = "completed";
    renderMyRequests();
    showMessage(
      `✅ Request marked as completed! Thank you for your help! 🎉`,
      "success",
    );
  }
}

// ============ LESSON FUNCTIONS ============

const lessonsDatabase = {
  1: {
    id: 1,
    icon: "🩸",
    title: "Understanding HIV/AIDS",
    language: "English",
    content:
      "HIV (Human Immunodeficiency Virus) is a virus that attacks the immune system...",
  },
  2: {
    id: 2,
    icon: "🍬",
    title: "Diabetes Management",
    language: "English",
    content:
      "Diabetes is a chronic condition that affects how your body turns food into energy...",
  },
  3: {
    id: 3,
    icon: "❤️",
    title: "Understanding Hypertension",
    language: "English",
    content:
      "Hypertension, or high blood pressure, is often called the 'silent killer'...",
  },
  4: {
    id: 4,
    icon: "🫁",
    title: "Tuberculosis (TB) Awareness",
    language: "English",
    content:
      "Tuberculosis (TB) is a bacterial infection that mainly affects the lungs...",
  },
};

function getLessonIdFromUrl() {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get("id");
}

function loadLesson() {
  const lessonId = getLessonIdFromUrl();
  if (!lessonId) {
    window.location.href = "dashboard.html";
    return;
  }

  const lesson = lessonsDatabase[lessonId];
  if (!lesson) {
    document.getElementById("lessonTitle").innerText = "Lesson Not Found";
    document.getElementById("lessonContent").innerHTML =
      "<p>The requested lesson could not be found.</p>";
    return;
  }

  document.getElementById("lessonIcon").innerText = lesson.icon;
  document.getElementById("lessonTitle").innerText = lesson.title;
  document.getElementById("lessonLanguage").innerHTML =
    `🗣️ Language: ${lesson.language}`;
  document.getElementById("lessonContent").innerHTML = lesson.content.replace(
    /\n/g,
    "<br>",
  );
  document.title = `${lesson.title} - Babayaga Care`;
}

// ============ ADMIN FUNCTIONS ============

let usersData = [
  {
    id: 1,
    name: "Admin User",
    phone: "0834567890",
    location: "Head Office",
    role: "admin",
    isActive: true,
    registrationDate: "2024-01-15",
  },
  {
    id: 2,
    name: "Thabo Nkosi",
    phone: "0712345678",
    location: "Soweto",
    role: "patient",
    isActive: true,
    registrationDate: "2024-02-20",
  },
  {
    id: 3,
    name: "Mary Jones",
    phone: "0723456789",
    location: "Khayelitsha",
    role: "volunteer",
    isActive: true,
    registrationDate: "2024-03-10",
  },
];

function renderUsersTable() {
  const tbody = document.getElementById("usersTableBody");
  if (!tbody) return;
  if (usersData.length === 0) {
    tbody.innerHTML = '<tr><td colspan="8">No users found</td></tr>';
    return;
  }

  tbody.innerHTML = usersData
    .map(
      (user) => `
        <tr>
            <td>${user.id}</td><td>${escapeHtml(user.name)}</td><td>${user.phone}</td>
            <td>${user.location || "Not set"}</td>
            <td><span class="role-badge role-${user.role}">${user.role === "admin" ? "👑 Admin" : user.role === "volunteer" ? "🤝 Volunteer" : "👤 Patient"}</span></td>
            <td class="${user.isActive ? "status-active" : "status-inactive"}">${user.isActive ? "🟢 Active" : "🔴 Inactive"}</td>
            <td>${user.registrationDate}</td>
            <td class="action-buttons">
                ${user.role !== "admin" ? `<button onclick="makeAdmin(${user.id})" class="action-btn action-btn-success">👑 Make Admin</button>` : user.id !== 1 ? `<button onclick="removeAdmin(${user.id})" class="action-btn action-btn-warning">⬇️ Remove</button>` : '<span class="note-text">(Yourself)</span>'}
                <button onclick="resetUserPassword(${user.id})" class="action-btn action-btn-primary">🔐 Reset</button>
                ${user.role !== "admin" ? `<button onclick="toggleUserStatus(${user.id})" class="action-btn action-btn-danger">${user.isActive ? "🔴 Deactivate" : "🟢 Activate"}</button>` : ""}
            </td>
        </tr>
    `,
    )
    .join("");
}

function makeAdmin(userId) {
  const user = usersData.find((u) => u.id === userId);
  if (user && confirm(`Make ${user.name} an admin?`)) {
    user.role = "admin";
    renderUsersTable();
    showMessage(`✅ ${user.name} has been promoted to Admin!`, "success");
  }
}

function removeAdmin(userId) {
  const user = usersData.find((u) => u.id === userId);
  if (
    user &&
    user.id !== 1 &&
    confirm(`Remove admin privileges from ${user.name}?`)
  ) {
    user.role = "patient";
    renderUsersTable();
    showMessage(`✅ Admin privileges removed from ${user.name}.`, "success");
  } else if (user && user.id === 1) {
    showMessage("❌ You cannot remove your own admin privileges!", "error");
  }
}

function toggleUserStatus(userId) {
  const user = usersData.find((u) => u.id === userId);
  if (
    user &&
    confirm(`${user.isActive ? "Deactivate" : "Activate"} user "${user.name}"?`)
  ) {
    user.isActive = !user.isActive;
    renderUsersTable();
    showMessage(
      `✅ User ${user.name} has been ${user.isActive ? "activated" : "deactivated"}!`,
      "success",
    );
  }
}

function resetUserPassword(userId) {
  const user = usersData.find((u) => u.id === userId);
  if (user && confirm(`Reset password for ${user.name}?`)) {
    showMessage(
      `✅ Password reset for ${user.name}. Temporary password sent to ${user.phone}.`,
      "success",
    );
  }
}

// ============ HELP REQUESTS DATA ============
let helpRequestsData = [
  {
    id: 101,
    patientName: "Thabo Nkosi",
    type: "medication",
    location: "Soweto",
    status: "open",
    urgent: true,
    requestDate: "2024-06-05",
  },
  {
    id: 102,
    patientName: "Mary Jones",
    type: "clinic",
    location: "Khayelitsha",
    status: "assigned",
    urgent: false,
    requestDate: "2024-06-04",
  },
];

function renderHelpRequestsTable() {
  const tbody = document.getElementById("requestsTableBody");
  if (!tbody) return;
  if (helpRequestsData.length === 0) {
    tbody.innerHTML = '<tr><td colspan="8">No help requests found</td></tr>';
    return;
  }

  tbody.innerHTML = helpRequestsData
    .map(
      (req) => `
        <tr class="${req.urgent && req.status === "open" ? "urgent-row" : ""}">
            <td>${req.id}</td><td>${escapeHtml(req.patientName)}</td>
            <td>${req.type === "medication" ? "💊 Medication" : "🚗 Ride"}</td>
            <td>${escapeHtml(req.location)}</td>
            <td><span class="status-badge status-${req.status}">${req.status}</span></td>
            <td>${req.urgent ? "🚨 Yes" : "No"}</td>
            <td>${req.requestDate}</td>
            <td class="action-buttons">
                ${req.status === "open" ? `<button onclick="assignRequest(${req.id})" class="btn-small btn-assign">Assign</button>` : ""}
                <button onclick="viewRequestDetails(${req.id})" class="btn-small btn-view">View</button>
            </td>
        </tr>
    `,
    )
    .join("");
}

function assignRequest(requestId) {
  const request = helpRequestsData.find((r) => r.id === requestId);
  if (request && confirm(`Assign request #${requestId} to a volunteer?`)) {
    request.status = "assigned";
    renderHelpRequestsTable();
    showMessage(`✅ Request #${requestId} has been assigned.`, "success");
  }
}

function viewRequestDetails(requestId) {
  const request = helpRequestsData.find((r) => r.id === requestId);
  if (request)
    alert(
      `📋 REQUEST DETAILS\n\nID: ${request.id}\nPatient: ${request.patientName}\nType: ${request.type}\nLocation: ${request.location}\nStatus: ${request.status}\nUrgent: ${request.urgent ? "Yes" : "No"}\nDate: ${request.requestDate}`,
    );
}

// ============ LESSONS MANAGEMENT ============
let lessonsData = [
  {
    id: 1,
    icon: "🩸",
    title: "Understanding HIV/AIDS",
    language: "English",
    isActive: true,
    shortContent: "HIV is a virus that attacks the immune system...",
  },
  {
    id: 2,
    icon: "🍬",
    title: "Diabetes Management",
    language: "English",
    isActive: true,
    shortContent: "Tips for managing blood sugar...",
  },
];

function renderLessonsTable() {
  const tbody = document.getElementById("lessonsTableBody");
  if (!tbody) return;
  if (lessonsData.length === 0) {
    tbody.innerHTML = '<tr><td colspan="7">No lessons found</td></tr>';
    return;
  }

  tbody.innerHTML = lessonsData
    .map(
      (lesson) => `
        <tr><td>${lesson.id}</td><td class="lesson-icon-cell">${lesson.icon}</td>
        <td>${escapeHtml(lesson.title)}</td><td>${lesson.language}</td>
        <td class="lesson-content-preview">${escapeHtml(lesson.shortContent)}</td>
        <td class="${lesson.isActive ? "status-active" : "status-inactive"}">${lesson.isActive ? "🟢 Active" : "🔴 Inactive"}</td>
        <td class="action-buttons">
            <button onclick="editLesson(${lesson.id})" class="action-btn action-btn-primary">✏️ Edit</button>
            <button onclick="toggleLessonStatus(${lesson.id})" class="action-btn action-btn-warning">${lesson.isActive ? "🔴 Deactivate" : "🟢 Activate"}</button>
            <button onclick="deleteLesson(${lesson.id})" class="action-btn action-btn-danger">🗑️ Delete</button>
        </td></tr>
    `,
    )
    .join("");
}

function editLesson(id) {
  showMessage(
    `✏️ Edit lesson ${id} - In full app, you would edit here.`,
    "success",
  );
}
function toggleLessonStatus(id) {
  const lesson = lessonsData.find((l) => l.id === id);
  if (lesson) {
    lesson.isActive = !lesson.isActive;
    renderLessonsTable();
    showMessage(
      `Lesson ${lesson.isActive ? "activated" : "deactivated"}!`,
      "success",
    );
  }
}
function deleteLesson(id) {
  if (confirm("Delete this lesson?")) {
    lessonsData = lessonsData.filter((l) => l.id !== id);
    renderLessonsTable();
    showMessage("Lesson deleted!", "success");
  }
}

// ============ INITIALIZATION ============
document.addEventListener("DOMContentLoaded", function () {
  autoHideMessage();

  // Initialize page-specific functions
  if (document.getElementById("openRequestsContainer")) {
    renderOpenRequests();
    renderMyRequests();
  }
  if (document.getElementById("usersTableBody")) {
    renderUsersTable();
  }
  if (document.getElementById("requestsTableBody")) {
    renderHelpRequestsTable();
  }
  if (document.getElementById("lessonsTableBody")) {
    renderLessonsTable();
  }

  // Modal close on outside click
  window.onclick = function (event) {
    const modals = [
      "medicationModal",
      "symptomModal",
      "helpModal",
      "demoModal",
    ];
    modals.forEach(function (id) {
      const modal = document.getElementById(id);
      if (event.target === modal) modal.style.display = "none";
    });
  };

  // Set default tab on login page
  if (
    document.querySelector(".tab") &&
    !document.querySelector(".tab.active")
  ) {
    const firstTab = document.querySelector(".tab");
    if (firstTab) firstTab.classList.add("active");
    const loginPanel = document.getElementById("loginPanel");
    if (loginPanel) loginPanel.classList.add("active");
  }

  // Load lesson if on lesson page
  if (document.getElementById("lessonContent")) loadLesson();
});
