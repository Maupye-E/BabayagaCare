// Babayaga Care - Main JavaScript (Full Functional Version)

// ============ DATA STORAGE KEYS ============
const STORAGE_KEYS = {
  USERS: "babayaga_users",
  MEDICATIONS: "babayaga_medications",
  SYMPTOMS: "babayaga_symptoms",
  HELP_REQUESTS: "babayaga_help_requests",
  LESSONS: "babayaga_lessons",
  CURRENT_USER: "babayaga_current_user",
};

// ============ INITIALIZE DEFAULT DATA ============
function initializeData() {
  // Initialize users if empty
  if (!localStorage.getItem(STORAGE_KEYS.USERS)) {
    const defaultUsers = [
      {
        id: 1,
        name: "Admin User",
        phone: "0834567890",
        password: "admin123",
        role: "admin",
        location: "Head Office",
        language: "English",
        isActive: true,
        registrationDate: new Date().toISOString().slice(0, 10),
      },
      {
        id: 2,
        name: "Mary Jones",
        phone: "0723456789",
        password: "volunteer123",
        role: "volunteer",
        location: "Khayelitsha, Cape Town",
        language: "English",
        isActive: true,
        registrationDate: new Date().toISOString().slice(0, 10),
      },
      {
        id: 3,
        name: "Thabo Nkosi",
        phone: "0712345678",
        password: "patient123",
        role: "patient",
        location: "Soweto, Johannesburg",
        language: "English",
        isActive: true,
        registrationDate: new Date().toISOString().slice(0, 10),
      },
    ];
    localStorage.setItem(STORAGE_KEYS.USERS, JSON.stringify(defaultUsers));
  }

  // Initialize medications if empty
  if (!localStorage.getItem(STORAGE_KEYS.MEDICATIONS)) {
    const defaultMeds = [
      {
        id: 1,
        userId: 3,
        name: "ARV Treatment",
        dosage: "1 tablet",
        time: "08:00",
        instructions: "Take with food",
        taken: false,
        dateAdded: new Date().toISOString().slice(0, 10),
      },
      {
        id: 2,
        userId: 3,
        name: "Multivitamin",
        dosage: "1 tablet",
        time: "20:00",
        instructions: "",
        taken: false,
        dateAdded: new Date().toISOString().slice(0, 10),
      },
      {
        id: 3,
        userId: 3,
        name: "Blood Pressure",
        dosage: "10mg",
        time: "08:00",
        instructions: "",
        taken: false,
        dateAdded: new Date().toISOString().slice(0, 10),
      },
    ];
    localStorage.setItem(STORAGE_KEYS.MEDICATIONS, JSON.stringify(defaultMeds));
  }

  // Initialize symptoms if empty
  if (!localStorage.getItem(STORAGE_KEYS.SYMPTOMS)) {
    const defaultSymptoms = [
      {
        id: 1,
        userId: 3,
        symptomName: "Headache",
        severity: 2,
        notes: "Took pain reliever",
        date: "2024-06-04",
      },
      {
        id: 2,
        userId: 3,
        symptomName: "Fatigue",
        severity: 3,
        notes: "",
        date: "2024-06-03",
      },
    ];
    localStorage.setItem(
      STORAGE_KEYS.SYMPTOMS,
      JSON.stringify(defaultSymptoms),
    );
  }

  // Initialize help requests if empty
  if (!localStorage.getItem(STORAGE_KEYS.HELP_REQUESTS)) {
    const defaultRequests = [
      {
        id: 1,
        userId: 3,
        patientName: "Thabo Nkosi",
        type: "medication",
        location: "Soweto",
        details: "Need help picking up ARV medication",
        urgent: false,
        status: "pending",
        requestDate: new Date().toISOString().slice(0, 10),
      },
    ];
    localStorage.setItem(
      STORAGE_KEYS.HELP_REQUESTS,
      JSON.stringify(defaultRequests),
    );
  }

  // Initialize lessons if empty
  if (!localStorage.getItem(STORAGE_KEYS.LESSONS)) {
    const defaultLessons = [
      {
        id: 1,
        icon: "🩸",
        title: "Understanding HIV/AIDS",
        language: "English",
        content:
          "HIV (Human Immunodeficiency Virus) is a virus that attacks the immune system. With proper treatment (ART), people with HIV can live long, healthy lives.",
        isActive: true,
      },
      {
        id: 2,
        icon: "🍬",
        title: "Diabetes Management",
        language: "English",
        content:
          "Diabetes is a chronic condition that affects how your body turns food into energy. Regular exercise and healthy eating are key.",
        isActive: true,
      },
      {
        id: 3,
        icon: "❤️",
        title: "Understanding Hypertension",
        language: "English",
        content:
          "Hypertension, or high blood pressure, is often called the 'silent killer'. Regular monitoring and medication adherence are crucial.",
        isActive: true,
      },
      {
        id: 4,
        icon: "🫁",
        title: "Tuberculosis (TB) Awareness",
        language: "English",
        content:
          "Tuberculosis (TB) is a bacterial infection that mainly affects the lungs. Complete your full 6-month treatment course.",
        isActive: true,
      },
    ];
    localStorage.setItem(STORAGE_KEYS.LESSONS, JSON.stringify(defaultLessons));
  }
}

// ============ MESSAGE DISPLAY FUNCTION ============
function showMessage(message, type) {
  const messageBox = document.getElementById("messageBox");
  if (messageBox) {
    messageBox.textContent = message;
    messageBox.className = "message " + type;
    messageBox.classList.remove("hidden");

    setTimeout(function () {
      messageBox.classList.add("hidden");
    }, 3000);
  } else {
    alert(message);
  }
}

// ============ AUTHENTICATION FUNCTIONS ============

function handleLogin(event) {
  event.preventDefault();

  const phone = document.getElementById("loginPhone").value;
  const password = document.getElementById("loginPassword").value;

  const users = JSON.parse(localStorage.getItem(STORAGE_KEYS.USERS) || "[]");
  const user = users.find(
    (u) => u.phone === phone && u.password === password && u.isActive,
  );

  if (user) {
    localStorage.setItem(STORAGE_KEYS.CURRENT_USER, JSON.stringify(user));
    showMessage("✅ Login successful! Redirecting...", "success");
    setTimeout(function () {
      window.location.href = "dashboard.html";
    }, 1000);
  } else {
    showMessage("❌ Invalid phone number or password", "error");
  }

  return false;
}

function handleRegister(event) {
  event.preventDefault();

  const name = document.getElementById("regName").value;
  const phone = document.getElementById("regPhone").value;
  const password = document.getElementById("regPassword").value;
  const confirmPassword = document.getElementById("regConfirmPassword").value;
  const language = document.getElementById("regLanguage").value;
  const location = document.getElementById("regLocation").value;
  const role = document.getElementById("regRole").value;

  if (password !== confirmPassword) {
    showMessage("❌ Passwords do not match!", "error");
    return false;
  }

  if (password.length < 6) {
    showMessage("❌ Password must be at least 6 characters!", "error");
    return false;
  }

  const users = JSON.parse(localStorage.getItem(STORAGE_KEYS.USERS) || "[]");

  if (users.find((u) => u.phone === phone)) {
    showMessage("❌ Phone number already registered!", "error");
    return false;
  }

  const newUser = {
    id: Date.now(),
    name: name,
    phone: phone,
    password: password,
    role: role,
    location: location,
    language: language,
    isActive: true,
    registrationDate: new Date().toISOString().slice(0, 10),
  };

  users.push(newUser);
  localStorage.setItem(STORAGE_KEYS.USERS, JSON.stringify(users));

  showMessage("✅ Registration successful! Please login.", "success");
  document.getElementById("registerForm").reset();
  showPanel("login");

  return false;
}

function handleLogout() {
  localStorage.removeItem(STORAGE_KEYS.CURRENT_USER);
  window.location.href = "login.html";
}

function getCurrentUser() {
  return JSON.parse(localStorage.getItem(STORAGE_KEYS.CURRENT_USER));
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

function showDemoForm() {
  const modal = document.getElementById("demoModal");
  if (modal) modal.style.display = "flex";
}

function closeDemoModal() {
  const modal = document.getElementById("demoModal");
  if (modal) modal.style.display = "none";
}

// ============ TAB SWITCHING ============
function showPanel(panel) {
  const tabs = document.querySelectorAll(".tab");
  tabs.forEach(function (tab, index) {
    tab.classList.remove("active");
  });

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

// ============ DASHBOARD FUNCTIONS ============
function loadDashboard() {
  const user = getCurrentUser();
  if (!user) {
    window.location.href = "login.html";
    return;
  }

  // Update user info
  document.getElementById("userName").innerText = user.name;
  document.getElementById("userLocation").innerText =
    user.location || "Not set";
  document.getElementById("userLanguage").innerText = user.language;
  document.getElementById("userRole").innerText = user.role;

  // Show/hide admin/volunteer buttons
  if (user.role === "admin") {
    document.getElementById("adminDashboardLink")?.classList.remove("hidden");
  }
  if (user.role === "volunteer") {
    document
      .getElementById("volunteerDashboardLink")
      ?.classList.remove("hidden");
  }

  loadMedications();
  loadSymptoms();
  loadHelpRequests();
  loadLessons();
  updateStats();
}

function loadMedications() {
  const user = getCurrentUser();
  const medications = JSON.parse(
    localStorage.getItem(STORAGE_KEYS.MEDICATIONS) || "[]",
  );
  const userMeds = medications.filter((m) => m.userId === user.id);
  const container = document.getElementById("medicationList");

  if (!container) return;

  if (userMeds.length === 0) {
    container.innerHTML =
      '<p class="empty-message">No medications yet. Click "Add Medication" to start.</p>';
    return;
  }

  container.innerHTML = userMeds
    .map(
      (med) => `
        <div class="medication-item" data-id="${med.id}">
            <div class="item-header">
                <strong>💊 ${escapeHtml(med.name)}</strong>
                <div class="item-actions">
                    <button onclick="markTaken(${med.id})" class="btn-icon" title="Mark as taken">✅</button>
                    <button onclick="deleteMedication(${med.id})" class="btn-icon" title="Delete">🗑️</button>
                </div>
            </div>
            <div class="item-details">
                <div>💊 Dosage: ${escapeHtml(med.dosage)}</div>
                <div>⏰ Time: ${med.time}</div>
                ${med.instructions ? `<div>📝 ${escapeHtml(med.instructions)}</div>` : ""}
                <div class="med-status">${med.taken ? "✅ Taken today" : "⭕ Not taken yet"}</div>
            </div>
        </div>
    `,
    )
    .join("");
}

function addMedication(event) {
  event.preventDefault();

  const user = getCurrentUser();
  const name = document.getElementById("medName").value;
  const dosage = document.getElementById("medDosage").value;
  const time = document.getElementById("medTime").value;
  const instructions = document.getElementById("medInstructions").value;

  const medications = JSON.parse(
    localStorage.getItem(STORAGE_KEYS.MEDICATIONS) || "[]",
  );

  const newMed = {
    id: Date.now(),
    userId: user.id,
    name: name,
    dosage: dosage,
    time: time,
    instructions: instructions,
    taken: false,
    dateAdded: new Date().toISOString().slice(0, 10),
  };

  medications.push(newMed);
  localStorage.setItem(STORAGE_KEYS.MEDICATIONS, JSON.stringify(medications));

  closeModal("medicationModal");
  document.getElementById("addMedicationForm").reset();
  loadMedications();
  updateStats();
  showMessage("✅ Medication added successfully!", "success");

  return false;
}

function markTaken(medId) {
  const medications = JSON.parse(
    localStorage.getItem(STORAGE_KEYS.MEDICATIONS) || "[]",
  );
  const med = medications.find((m) => m.id === medId);
  if (med) {
    med.taken = true;
    localStorage.setItem(STORAGE_KEYS.MEDICATIONS, JSON.stringify(medications));
    loadMedications();
    updateStats();
    showMessage("✅ Medication marked as taken!", "success");
  }
}

function deleteMedication(medId) {
  if (confirm("Delete this medication?")) {
    let medications = JSON.parse(
      localStorage.getItem(STORAGE_KEYS.MEDICATIONS) || "[]",
    );
    medications = medications.filter((m) => m.id !== medId);
    localStorage.setItem(STORAGE_KEYS.MEDICATIONS, JSON.stringify(medications));
    loadMedications();
    updateStats();
    showMessage("✅ Medication deleted!", "success");
  }
}

function loadSymptoms() {
  const user = getCurrentUser();
  const symptoms = JSON.parse(
    localStorage.getItem(STORAGE_KEYS.SYMPTOMS) || "[]",
  );
  const userSymptoms = symptoms.filter((s) => s.userId === user.id).reverse();
  const container = document.getElementById("symptomList");

  if (!container) return;

  if (userSymptoms.length === 0) {
    container.innerHTML =
      '<p class="empty-message">No symptoms logged yet. Click "Log Symptom" to start.</p>';
    return;
  }

  const severityMap = {
    1: "⭐",
    2: "⭐⭐",
    3: "⭐⭐⭐",
    4: "⭐⭐⭐⭐",
    5: "⭐⭐⭐⭐⭐",
  };
  const severityText = {
    1: "Mild",
    2: "Moderate",
    3: "Uncomfortable",
    4: "Severe",
    5: "Very Severe",
  };

  container.innerHTML = userSymptoms
    .slice(0, 5)
    .map(
      (sym) => `
        <div class="symptom-item" data-id="${sym.id}">
            <div class="item-header">
                <strong>🤕 ${escapeHtml(sym.symptomName)}</strong>
                <button onclick="deleteSymptom(${sym.id})" class="btn-icon" title="Delete">🗑️</button>
            </div>
            <div class="item-details">
                <div>Severity: ${severityMap[sym.severity]} (${severityText[sym.severity]})</div>
                <div>📅 ${sym.date}</div>
                ${sym.notes ? `<div>📝 ${escapeHtml(sym.notes)}</div>` : ""}
            </div>
        </div>
    `,
    )
    .join("");
}

function addSymptom(event) {
  event.preventDefault();

  const user = getCurrentUser();
  const name = document.getElementById("symptomName").value;
  const severity = parseInt(document.getElementById("symptomSeverity").value);
  const notes = document.getElementById("symptomNotes").value;

  const symptoms = JSON.parse(
    localStorage.getItem(STORAGE_KEYS.SYMPTOMS) || "[]",
  );

  const newSymptom = {
    id: Date.now(),
    userId: user.id,
    symptomName: name,
    severity: severity,
    notes: notes,
    date: new Date().toISOString().slice(0, 10),
  };

  symptoms.push(newSymptom);
  localStorage.setItem(STORAGE_KEYS.SYMPTOMS, JSON.stringify(symptoms));

  closeModal("symptomModal");
  document.getElementById("addSymptomForm").reset();
  loadSymptoms();
  updateStats();
  showMessage("✅ Symptom logged successfully!", "success");

  return false;
}

function deleteSymptom(symId) {
  if (confirm("Delete this symptom record?")) {
    let symptoms = JSON.parse(
      localStorage.getItem(STORAGE_KEYS.SYMPTOMS) || "[]",
    );
    symptoms = symptoms.filter((s) => s.id !== symId);
    localStorage.setItem(STORAGE_KEYS.SYMPTOMS, JSON.stringify(symptoms));
    loadSymptoms();
    updateStats();
    showMessage("✅ Symptom deleted!", "success");
  }
}

function loadHelpRequests() {
  const user = getCurrentUser();
  const requests = JSON.parse(
    localStorage.getItem(STORAGE_KEYS.HELP_REQUESTS) || "[]",
  );
  const userRequests = requests.filter((r) => r.userId === user.id);
  const container = document.getElementById("helpRequestList");

  if (!container) return;

  if (userRequests.length === 0) {
    container.innerHTML =
      '<p class="empty-message">No help requests yet. Click "Request Help" if you need assistance.</p>';
    return;
  }

  const typeMap = {
    medication: "💊 Medication Pickup",
    clinic: "🚗 Clinic Ride",
    food: "🍲 Food Parcel",
    care: "👵 Home Care",
    other: "📞 Other",
  };

  container.innerHTML = userRequests
    .slice(0, 3)
    .map(
      (req) => `
        <div class="help-item">
            <strong>${typeMap[req.type] || req.type}</strong>
            <div class="help-status">Status: ${req.status === "pending" ? "🟡 Pending" : req.status === "assigned" ? "🔵 Assigned" : "✅ Completed"}</div>
            <div class="help-date">📅 ${req.requestDate}</div>
        </div>
    `,
    )
    .join("");
}

function addHelpRequest(event) {
  event.preventDefault();

  const user = getCurrentUser();
  const type = document.getElementById("helpType").value;
  const location = document.getElementById("helpLocation").value;
  const details = document.getElementById("helpDetails").value;
  const urgent = document.getElementById("helpUrgent").checked;

  const requests = JSON.parse(
    localStorage.getItem(STORAGE_KEYS.HELP_REQUESTS) || "[]",
  );

  const newRequest = {
    id: Date.now(),
    userId: user.id,
    patientName: user.name,
    type: type,
    location: location,
    details: details,
    urgent: urgent,
    status: "pending",
    requestDate: new Date().toISOString().slice(0, 10),
  };

  requests.push(newRequest);
  localStorage.setItem(STORAGE_KEYS.HELP_REQUESTS, JSON.stringify(requests));

  closeModal("helpModal");
  document.getElementById("helpRequestForm").reset();
  loadHelpRequests();
  updateStats();
  showMessage(
    "✅ Help request sent! A volunteer will contact you soon.",
    "success",
  );

  return false;
}

function loadLessons() {
  const user = getCurrentUser();
  const lessons = JSON.parse(
    localStorage.getItem(STORAGE_KEYS.LESSONS) || "[]",
  );
  const container = document.getElementById("lessonList");

  if (!container) return;

  const userLessons = lessons.filter((l) => l.isActive);

  if (userLessons.length === 0) {
    container.innerHTML = '<p class="empty-message">No lessons available.</p>';
    return;
  }

  container.innerHTML = userLessons
    .map(
      (lesson) => `
        <div class="lesson-item" onclick="viewLesson(${lesson.id})">
            <h4>${lesson.icon} ${escapeHtml(lesson.title)}</h4>
            <p>${escapeHtml(lesson.content.substring(0, 100))}...</p>
            <span class="read-more">Click to read full lesson →</span>
        </div>
    `,
    )
    .join("");
}

function viewLesson(lessonId) {
  window.location.href = `lesson.html?id=${lessonId}`;
}

function updateStats() {
  const user = getCurrentUser();
  const medications = JSON.parse(
    localStorage.getItem(STORAGE_KEYS.MEDICATIONS) || "[]",
  );
  const symptoms = JSON.parse(
    localStorage.getItem(STORAGE_KEYS.SYMPTOMS) || "[]",
  );
  const requests = JSON.parse(
    localStorage.getItem(STORAGE_KEYS.HELP_REQUESTS) || "[]",
  );

  const userMeds = medications.filter((m) => m.userId === user.id);
  const userSymptoms = symptoms.filter((s) => s.userId === user.id);
  const userRequests = requests.filter((r) => r.userId === user.id);

  const takenCount = userMeds.filter((m) => m.taken).length;
  const adherenceRate =
    userMeds.length > 0 ? Math.round((takenCount / userMeds.length) * 100) : 0;

  document.getElementById("medCount").innerText = userMeds.length;
  document.getElementById("symCount").innerText = userSymptoms.length;
  document.getElementById("helpCount").innerText = userRequests.length;
  document.getElementById("adherenceRate").innerText = adherenceRate + "%";

  const fill = document.getElementById("adherenceFill");
  if (fill) fill.style.width = adherenceRate + "%";
}

function loadLessonPage() {
  const urlParams = new URLSearchParams(window.location.search);
  const lessonId = urlParams.get("id");
  const lessons = JSON.parse(
    localStorage.getItem(STORAGE_KEYS.LESSONS) || "[]",
  );
  const lesson = lessons.find((l) => l.id == lessonId);

  if (lesson) {
    document.getElementById("lessonIcon").innerText = lesson.icon;
    document.getElementById("lessonTitle").innerText = lesson.title;
    document.getElementById("lessonLanguage").innerHTML =
      `🗣️ Language: ${lesson.language}`;
    document.getElementById("lessonContent").innerHTML = lesson.content.replace(
      /\n/g,
      "<br>",
    );
    document.title = `${lesson.title} - Babayaga Care`;
  } else {
    document.getElementById("lessonTitle").innerText = "Lesson Not Found";
    document.getElementById("lessonContent").innerHTML =
      "<p>The requested lesson could not be found.</p>";
  }
}

// ============ CHANGE PASSWORD ============
function handleChangePassword(event) {
  event.preventDefault();

  const user = getCurrentUser();
  const oldPassword = document.getElementById("oldPassword").value;
  const newPassword = document.getElementById("newPassword").value;
  const confirmPassword = document.getElementById("confirmPassword").value;

  if (user.password !== oldPassword) {
    showMessage("❌ Current password is incorrect!", "error");
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

  const users = JSON.parse(localStorage.getItem(STORAGE_KEYS.USERS) || "[]");
  const userIndex = users.findIndex((u) => u.id === user.id);
  users[userIndex].password = newPassword;
  localStorage.setItem(STORAGE_KEYS.USERS, JSON.stringify(users));
  localStorage.setItem(
    STORAGE_KEYS.CURRENT_USER,
    JSON.stringify(users[userIndex]),
  );

  showMessage("✅ Password changed successfully!", "success");
  document.getElementById("changePasswordForm").reset();

  return false;
}

function handleForgotPassword(event) {
  event.preventDefault();

  const phone = document.getElementById("forgotPhone").value;
  const newPassword = document.getElementById("forgotNewPassword").value;
  const confirmPassword = document.getElementById(
    "forgotConfirmPassword",
  ).value;

  if (newPassword !== confirmPassword) {
    showMessage("❌ Passwords do not match!", "error");
    return false;
  }

  if (newPassword.length < 6) {
    showMessage("❌ Password must be at least 6 characters!", "error");
    return false;
  }

  const users = JSON.parse(localStorage.getItem(STORAGE_KEYS.USERS) || "[]");
  const user = users.find((u) => u.phone === phone);

  if (!user) {
    showMessage("❌ Phone number not found!", "error");
    return false;
  }

  user.password = newPassword;
  localStorage.setItem(STORAGE_KEYS.USERS, JSON.stringify(users));

  showMessage(
    "✅ Password reset successful! Redirecting to login...",
    "success",
  );
  setTimeout(() => {
    window.location.href = "login.html";
  }, 2000);

  return false;
}

// ============ ESCAPE HTML ============
function escapeHtml(str) {
  if (!str) return "";
  return str
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#39;");
}

// ============ INITIALIZATION ============
document.addEventListener("DOMContentLoaded", function () {
  initializeData();

  // Page-specific initialization
  if (document.getElementById("loginForm")) {
    // Login page - nothing extra needed
  }

  if (document.getElementById("userName")) {
    loadDashboard();
  }

  if (document.getElementById("lessonContent")) {
    loadLessonPage();
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
});
