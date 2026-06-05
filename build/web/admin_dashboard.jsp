<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="za.ac.tut.model.entity.User"%>
<%@page import="za.ac.tut.model.service.UserService"%>
<%@page import="za.ac.tut.model.service.MedicationService"%>
<%@page import="za.ac.tut.model.service.SymptomService"%>
<%@page import="za.ac.tut.model.service.HelpRequestService"%>
<%@page import="za.ac.tut.model.entity.HelpRequest"%>
<%@page import="java.util.List"%>
<%
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null || !loggedInUser.isAdmin()) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    UserService userService = new UserService();
    MedicationService medicationService = new MedicationService();
    SymptomService symptomService = new SymptomService();
    HelpRequestService helpRequestService = new HelpRequestService();
    
    List<User> allPatients = userService.getAllPatients();
    List<User> allVolunteers = userService.getAllVolunteers();
    List<User> allAdmins = userService.getAllAdmins();
    List<HelpRequest> allHelpRequests = helpRequestService.findAll();
    List<HelpRequest> openRequests = helpRequestService.getOpenRequests();
    List<HelpRequest> assignedRequests = helpRequestService.getAssignedRequests();
    List<HelpRequest> completedRequests = helpRequestService.getCompletedRequests();
    
    long totalUsers = userService.getTotalUsersCount();
    long activeUsers = userService.getActiveUsersCount();
    long totalMedications = 0;
    long totalSymptoms = 0;
    
    for (User u : allPatients) {
        totalMedications += medicationService.getUserMedications(u.getId()).size();
        totalSymptoms += symptomService.getUserSymptoms(u.getId()).size();
    }
    for (User v : allVolunteers) {
        totalMedications += medicationService.getUserMedications(v.getId()).size();
        totalSymptoms += symptomService.getUserSymptoms(v.getId()).size();
    }
    
    double avgResponseTime = helpRequestService.getAverageResponseTime();
    int completionRate = helpRequestService.getCompletionRate();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Babayaga Care</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="container">
        <div class="dashboard-header">
            <div>
                <h2>👑 Admin Dashboard</h2>
                <p>Welcome, <%= loggedInUser.getName() %>! | Role: <%= loggedInUser.getRole() %></p>
            </div>
            <div class="header-buttons">
                <button onclick="window.location.href='dashboard.jsp'" class="btn btn-secondary">📊 User Dashboard</button>
                <button onclick="window.location.href='admin_lessons.jsp'" class="btn btn-secondary">📚 Manage Lessons</button>
                <button onclick="window.location.href='change_password.jsp'" class="btn btn-secondary">🔑 Change Password</button>
                <button onclick="logout()" class="btn btn-secondary">🚪 Logout</button>
            </div>
        </div>
        
        <div class="stats-grid">
            <div class="stat-card"><h3><%= totalUsers %></h3><p>👥 Total Users</p></div>
            <div class="stat-card"><h3><%= activeUsers %></h3><p>🟢 Active</p></div>
            <div class="stat-card"><h3><%= allPatients.size() %></h3><p>👤 Patients</p></div>
            <div class="stat-card"><h3><%= allVolunteers.size() %></h3><p>🤝 Volunteers</p></div>
            <div class="stat-card"><h3><%= allAdmins.size() %></h3><p>👑 Admins</p></div>
            <div class="stat-card"><h3><%= totalMedications %></h3><p>💊 Medications</p></div>
            <div class="stat-card"><h3><%= totalSymptoms %></h3><p>📝 Symptoms</p></div>
            <div class="stat-card"><h3><%= allHelpRequests.size() %></h3><p>📋 Requests</p></div>
        </div>
        
        <div class="admin-actions">
            <div class="admin-action-card" onclick="window.location.href='admin_users.jsp'">
                <div class="admin-action-icon">👥</div><h3>User Management</h3><p>View and manage all users</p>
            </div>
            <div class="admin-action-card" onclick="window.location.href='reset_password.jsp'">
                <div class="admin-action-icon">🔐</div><h3>Reset Passwords</h3><p>Reset any user's password</p>
            </div>
            <div class="admin-action-card" onclick="window.location.href='admin_help_requests.jsp'">
                <div class="admin-action-icon">📋</div><h3>Help Requests</h3><p>View all help requests</p>
            </div>
        </div>
        
        <div class="dashboard-card">
            <h3>📊 Help Request Statistics</h3>
            <div class="stats-row">
                <div class="stat-item"><span class="stat-label">🟡 Open</span><span class="stat-value"><%= openRequests.size() %></span></div>
                <div class="stat-item"><span class="stat-label">🔵 Assigned</span><span class="stat-value"><%= assignedRequests.size() %></span></div>
                <div class="stat-item"><span class="stat-label">✅ Completed</span><span class="stat-value"><%= completedRequests.size() %></span></div>
                <div class="stat-item"><span class="stat-label">📊 Completion Rate</span><span class="stat-value"><%= completionRate %>%</span></div>
                <div class="stat-item"><span class="stat-label">⏱️ Avg Response</span><span class="stat-value"><%= String.format("%.1f", avgResponseTime) %> hrs</span></div>
            </div>
        </div>
    </div>
    
    <script src="script.js"></script>
</body>
</html>