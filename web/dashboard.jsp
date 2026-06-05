<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="za.ac.tut.model.entity.User"%>
<%@page import="za.ac.tut.model.service.MedicationService"%>
<%@page import="za.ac.tut.model.service.SymptomService"%>
<%@page import="za.ac.tut.model.service.HelpRequestService"%>
<%@page import="za.ac.tut.model.service.HealthLessonService"%>
<%@page import="za.ac.tut.model.entity.Medication"%>
<%@page import="za.ac.tut.model.entity.Symptom"%>
<%@page import="za.ac.tut.model.entity.HelpRequest"%>
<%@page import="za.ac.tut.model.entity.HealthLesson"%>
<%@page import="java.util.List"%>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    Long userId = user.getId();
    MedicationService medicationService = new MedicationService();
    SymptomService symptomService = new SymptomService();
    HelpRequestService helpRequestService = new HelpRequestService();
    HealthLessonService lessonService = new HealthLessonService();
    
    String action = request.getParameter("action");
    String message = "";
    String messageType = "success";
    
    if ("addMedication".equals(action)) {
        Medication med = medicationService.addMedication(userId, 
            request.getParameter("name"), 
            request.getParameter("dosage"), 
            request.getParameter("time"), 
            request.getParameter("instructions"));
        if (med != null) {
            message = "✅ Medication added successfully!";
        } else {
            message = "❌ Failed to add medication";
            messageType = "error";
        }
    }
    
    if ("deleteMedication".equals(action)) {
        boolean deleted = medicationService.deleteMedication(Long.parseLong(request.getParameter("id")));
        message = deleted ? "✅ Medication deleted!" : "❌ Failed to delete medication";
        if (!deleted) messageType = "error";
    }
    
    if ("markTaken".equals(action)) {
        medicationService.markMedicationAsTaken(Long.parseLong(request.getParameter("id")));
        message = "✅ Medication marked as taken!";
    }
    
    if ("addSymptom".equals(action)) {
        Symptom sym = symptomService.logSymptom(userId, 
            request.getParameter("symptomName"), 
            Integer.parseInt(request.getParameter("severity")), 
            request.getParameter("notes"));
        if (sym != null) {
            message = "✅ Symptom logged successfully!";
        } else {
            message = "❌ Failed to log symptom";
            messageType = "error";
        }
    }
    
    if ("deleteSymptom".equals(action)) {
        boolean deleted = symptomService.deleteSymptom(Long.parseLong(request.getParameter("id")));
        message = deleted ? "✅ Symptom deleted!" : "❌ Failed to delete symptom";
        if (!deleted) messageType = "error";
    }
    
    if ("requestHelp".equals(action)) {
        HelpRequest helpReq = helpRequestService.createHelpRequest(userId, 
            request.getParameter("requestType"), 
            request.getParameter("location"), 
            request.getParameter("details"), 
            request.getParameter("urgent") != null);
        if (helpReq != null) {
            message = "✅ Help request sent! A volunteer will contact you soon.";
        } else {
            message = "❌ Failed to send help request";
            messageType = "error";
        }
    }
    
    List<Medication> medications = medicationService.getUserMedications(userId);
    List<Symptom> symptoms = symptomService.getUserSymptoms(userId);
    List<HelpRequest> helpRequests = helpRequestService.getPatientRequests(userId);
    List<HealthLesson> lessons = lessonService.getLessonsByLanguage(user.getLanguage());
    double adherenceRate = medicationService.getAdherenceRate(userId);
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Babayaga Care</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="container">
        <div class="dashboard-header">
            <div>
                <h2>👋 Welcome, <%= user.getName() %>!</h2>
                <p>📍 <%= user.getLocation() != null ? user.getLocation() : "Not set" %> | 🗣️ <%= user.getLanguage() %> | 👥 <%= user.getRole() %></p>
            </div>
            <div class="header-buttons">
                <% if (user.isAdmin()) { %>
                    <button onclick="window.location.href='admin_dashboard.jsp'" class="btn btn-primary">👑 Admin Dashboard</button>
                <% } %>
                <% if (user.isVolunteer()) { %>
                    <button onclick="window.location.href='volunteer.jsp'" class="btn btn-primary">🤝 Volunteer Dashboard</button>
                <% } %>
                <button onclick="window.location.href='change_password.jsp'" class="btn btn-secondary">🔐 Change Password</button>
                <button onclick="logout()" class="btn btn-secondary">🚪 Logout</button>
            </div>
        </div>
        
        <% if (!message.isEmpty()) { %>
            <div id="messageBox" class="message <%= messageType %>"><%= message %></div>
        <% } %>
        
        <div class="stats-grid">
            <div class="stat-card"><h3><%= medications.size() %></h3><p>💊 Medications</p></div>
            <div class="stat-card"><h3><%= symptoms.size() %></h3><p>📝 Symptoms</p></div>
            <div class="stat-card"><h3><%= helpRequests.size() %></h3><p>🤝 Help Requests</p></div>
            <div class="stat-card">
                <h3><%= String.format("%.0f", adherenceRate) %>%</h3>
                <p>📊 Adherence</p>
                <div class="adherence-bar"><div class="adherence-fill" style="width: <%= adherenceRate %>%"></div></div>
            </div>
        </div>
        
        <div class="dashboard-grid">
            <!-- Medications Card -->
            <div class="dashboard-card">
                <h3>💊 My Medications</h3>
                <div id="medicationList">
                    <% if (medications.isEmpty()) { %>
                        <p class="empty-message">No medications yet. Click "Add Medication" to start.</p>
                    <% } else { %>
                        <% for (Medication med : medications) { %>
                            <div class="medication-item">
                                <div class="item-header">
                                    <strong>💊 <%= med.getName() %></strong>
                                    <div class="item-actions">
                                        <a href="dashboard.jsp?action=markTaken&id=<%= med.getId() %>" class="btn-icon" title="Mark as taken">✅</a>
                                        <a href="dashboard.jsp?action=deleteMedication&id=<%= med.getId() %>" class="btn-icon" onclick="return confirmDelete('Delete this medication?')" title="Delete">🗑️</a>
                                    </div>
                                </div>
                                <div class="item-details">
                                    <div>💊 Dosage: <%= med.getDosage() %></div>
                                    <div>⏰ Time: <%= med.getTime() %></div>
                                    <%= med.getInstructions() != null ? "<div>📝 " + med.getInstructions() + "</div>" : "" %>
                                    <div class="med-status"><%= med.isTaken() ? "✅ Taken today" : "⭕ Not taken yet" %></div>
                                </div>
                            </div>
                        <% } %>
                    <% } %>
                </div>
                <button onclick="showAddMedication()" class="btn btn-primary full-width">+ Add Medication</button>
            </div>
            
            <!-- Symptoms Card -->
            <div class="dashboard-card">
                <h3>📝 Symptom Tracker</h3>
                <div id="symptomList">
                    <% if (symptoms.isEmpty()) { %>
                        <p class="empty-message">No symptoms logged yet. Click "Log Symptom" to start.</p>
                    <% } else { %>
                        <% for (int i = 0; i < Math.min(5, symptoms.size()); i++) { 
                            Symptom sym = symptoms.get(i); %>
                            <div class="symptom-item">
                                <div class="item-header">
                                    <strong><%= sym.getSeverityEmoji() %> <%= sym.getSymptomName() %></strong>
                                    <a href="dashboard.jsp?action=deleteSymptom&id=<%= sym.getId() %>" class="btn-icon" onclick="return confirmDelete('Delete this symptom record?')" title="Delete">🗑️</a>
                                </div>
                                <div class="item-details">
                                    <div>Severity: <%= sym.getSeverityStars() %> (<%= sym.getSeverityText() %>)</div>
                                    <div>📅 <%= sym.getSymptomDate() %></div>
                                    <%= sym.getNotes() != null ? "<div>📝 " + sym.getNotes() + "</div>" : "" %>
                                </div>
                            </div>
                        <% } %>
                        <% if (symptoms.size() > 5) { %>
                            <p class="more-items">+ <%= symptoms.size() - 5 %> more symptoms</p>
                        <% } %>
                    <% } %>
                </div>
                <button onclick="showAddSymptom()" class="btn btn-primary full-width">+ Log Symptom</button>
            </div>
            
            <!-- Health Lessons Card -->
            <div class="dashboard-card">
                <h3>📚 Health Education</h3>
                <div id="lessonList">
                    <% if (lessons.isEmpty()) { %>
                        <p class="empty-message">No lessons available in your language.</p>
                    <% } else { %>
                        <% for (HealthLesson lesson : lessons) { %>
                            <div class="lesson-item" onclick="showLesson('<%= lesson.getId() %>')">
                                <h4><%= lesson.getIcon() %> <%= lesson.getTitle() %></h4>
                                <p><%= lesson.getShortContent(80) %></p>
                                <span class="read-more">Click to read full lesson →</span>
                            </div>
                        <% } %>
                    <% } %>
                </div>
            </div>
            
            <!-- Community Support Card -->
            <div class="dashboard-card">
                <h3>🤝 Community Support</h3>
                <div id="helpRequestList">
                    <% if (helpRequests.isEmpty()) { %>
                        <p class="empty-message">No help requests yet. Click "Request Help" if you need assistance.</p>
                    <% } else { %>
                        <% for (int i = 0; i < Math.min(3, helpRequests.size()); i++) { 
                            HelpRequest req = helpRequests.get(i); %>
                            <div class="help-item">
                                <strong><%= req.getRequestTypeIcon() %> <%= req.getRequestTypeText() %></strong>
                                <div class="help-status">Status: <%= req.getStatusBadge() %></div>
                                <div class="help-date">📅 <%= req.getRequestDate() %></div>
                            </div>
                        <% } %>
                        <% if (helpRequests.size() > 3) { %>
                            <p class="more-items">+ <%= helpRequests.size() - 3 %> more requests</p>
                        <% } %>
                    <% } %>
                </div>
                <button onclick="showHelpForm()" class="btn btn-primary full-width">📢 Request Help</button>
                <% if (user.isVolunteer()) { %>
                    <button onclick="window.location.href='volunteer.jsp'" class="btn btn-secondary full-width" style="margin-top: 10px;">👥 View Open Requests</button>
                <% } %>
            </div>
        </div>
        
        <div class="health-tip">
            <p>💡 <strong>Daily Health Tip:</strong> Take your medication at the same time every day. Set an alarm on your phone!</p>
        </div>
    </div>
    
    <!-- Add Medication Modal -->
    <div id="medicationModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Add Medication</h3>
                <span class="close" onclick="closeModal('medicationModal')">&times;</span>
            </div>
            <form action="dashboard.jsp" method="post">
                <input type="hidden" name="action" value="addMedication">
                <div class="form-group">
                    <label>💊 Medication Name *</label>
                    <input type="text" name="name" required placeholder="e.g., ARV, Metformin">
                </div>
                <div class="form-group">
                    <label>💊 Dosage *</label>
                    <input type="text" name="dosage" required placeholder="e.g., 1 tablet, 10ml">
                </div>
                <div class="form-group">
                    <label>⏰ Time *</label>
                    <input type="time" name="time" required>
                </div>
                <div class="form-group">
                    <label>📝 Instructions</label>
                    <textarea name="instructions" rows="2" placeholder="e.g., Take with food, before bedtime"></textarea>
                </div>
                <div class="form-buttons">
                    <button type="button" onclick="closeModal('medicationModal')" class="btn btn-secondary">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Medication</button>
                </div>
            </form>
        </div>
    </div>
    
    <!-- Add Symptom Modal -->
    <div id="symptomModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Log Symptom</h3>
                <span class="close" onclick="closeModal('symptomModal')">&times;</span>
            </div>
            <form action="dashboard.jsp" method="post">
                <input type="hidden" name="action" value="addSymptom">
                <div class="form-group">
                    <label>🤒 Symptom *</label>
                    <input type="text" name="symptomName" required placeholder="e.g., Headache, Fever, Cough">
                </div>
                <div class="form-group">
                    <label>📊 Severity (1-5) *</label>
                    <select name="severity">
                        <option value="1">1 - Mild (Barely noticeable)</option>
                        <option value="2">2 - Moderate (Annoying but manageable)</option>
                        <option value="3" selected>3 - Uncomfortable (Hard to ignore)</option>
                        <option value="4">4 - Severe (Difficult to function)</option>
                        <option value="5">5 - Very Severe (Need immediate help)</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>📝 Notes</label>
                    <textarea name="notes" rows="2" placeholder="Additional details..."></textarea>
                </div>
                <div class="form-buttons">
                    <button type="button" onclick="closeModal('symptomModal')" class="btn btn-secondary">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Symptom</button>
                </div>
            </form>
        </div>
    </div>
    
    <!-- Help Request Modal -->
    <div id="helpModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Request Help</h3>
                <span class="close" onclick="closeModal('helpModal')">&times;</span>
            </div>
            <form action="dashboard.jsp" method="post">
                <input type="hidden" name="action" value="requestHelp">
                <div class="form-group">
                    <label>Type of Help *</label>
                    <select name="requestType" required>
                        <option value="medication">💊 Medication Pickup</option>
                        <option value="clinic">🚗 Clinic/Hospital Ride</option>
                        <option value="food">🍲 Food Parcel</option>
                        <option value="care">👵 Home Care Check-in</option>
                        <option value="other">📞 Other</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>📍 Your Location *</label>
                    <input type="text" name="location" required placeholder="e.g., Soweto, Khayelitsha">
                </div>
                <div class="form-group">
                    <label>📝 Details *</label>
                    <textarea name="details" rows="3" required placeholder="Please provide details that will help the volunteer..."></textarea>
                </div>
                <div class="form-group">
                    <label><input type="checkbox" name="urgent"> 🚨 This is urgent (needs help today)</label>
                </div>
                <div class="form-buttons">
                    <button type="button" onclick="closeModal('helpModal')" class="btn btn-secondary">Cancel</button>
                    <button type="submit" class="btn btn-primary">Submit Request</button>
                </div>
            </form>
        </div>
    </div>
    
    <script src="script.js"></script>
</body>
</html>