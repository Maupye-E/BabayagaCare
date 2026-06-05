<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="za.ac.tut.model.entity.User"%>
<%@page import="za.ac.tut.model.service.HelpRequestService"%>
<%@page import="za.ac.tut.model.service.UserService"%>
<%@page import="za.ac.tut.model.entity.HelpRequest"%>
<%@page import="java.util.List"%>
<%
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null || !loggedInUser.isAdmin()) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    HelpRequestService helpRequestService = new HelpRequestService();
    UserService userService = new UserService();
    
    List<HelpRequest> allRequests = helpRequestService.findAll();
    String filter = request.getParameter("filter");
    
    if ("open".equals(filter)) allRequests = helpRequestService.getOpenRequests();
    else if ("completed".equals(filter)) allRequests = helpRequestService.getCompletedRequests();
    else if ("assigned".equals(filter)) allRequests = helpRequestService.getAssignedRequests();
    else if ("urgent".equals(filter)) allRequests = helpRequestService.getUrgentRequests();
    
    int openCount = (int) helpRequestService.getOpenRequestsCount();
    int assignedCount = (int) helpRequestService.getAssignedRequestsCount();
    int completedCount = (int) helpRequestService.getCompletedRequestsCount();
    int urgentCount = (int) helpRequestService.getUrgentRequestsCount();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Help Requests - Admin</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="container">
        <div class="dashboard-header">
            <div><h2>📋 Help Requests Management</h2><p>Welcome, <%= loggedInUser.getName() %>! | 👑 Admin</p></div>
            <div class="header-buttons">
                <button onclick="window.location.href='admin_dashboard.jsp'" class="btn btn-secondary">← Back</button>
                <button onclick="logout()" class="btn btn-secondary">🚪 Logout</button>
            </div>
        </div>
        
        <div class="filter-buttons">
            <a href="admin_help_requests.jsp" class="filter-btn <%= filter == null ? "active" : "" %>">📋 All</a>
            <a href="admin_help_requests.jsp?filter=open" class="filter-btn <%= "open".equals(filter) ? "active" : "" %>">🟡 Open</a>
            <a href="admin_help_requests.jsp?filter=assigned" class="filter-btn <%= "assigned".equals(filter) ? "active" : "" %>">🔵 Assigned</a>
            <a href="admin_help_requests.jsp?filter=completed" class="filter-btn <%= "completed".equals(filter) ? "active" : "" %>">✅ Completed</a>
            <a href="admin_help_requests.jsp?filter=urgent" class="filter-btn <%= "urgent".equals(filter) ? "active" : "" %>">🚨 Urgent</a>
        </div>
        
        <table class="requests-table">
            <thead><tr><th>ID</th><th>Patient</th><th>Type</th><th>Location</th><th>Status</th><th>Urgent</th><th>Volunteer</th><th>Requested</th></tr></thead>
            <tbody>
                <% for (HelpRequest req : allRequests) { 
                    User patient = userService.getUserById(req.getPatient().getId());
                    User volunteer = req.getVolunteer() != null ? userService.getUserById(req.getVolunteer().getId()) : null;
                %>
                    <tr class="<%= req.isUrgent() && "open".equals(req.getStatus()) ? "urgent-row" : "" %>">
                        <td><%= req.getId() %></td>
                        <td><%= patient != null ? patient.getName() : "Unknown" %><br><small><%= patient != null ? patient.getPhone() : "" %></small></td>
                        <td><%= req.getRequestTypeIcon() %> <%= req.getRequestTypeText() %></td>
                        <td><%= req.getLocation() %></td>
                        <td><span class="status-badge status-<%= req.getStatus() %>"><%= req.getStatusBadge() %></span></td>
                        <td><%= req.isUrgent() ? "🚨 Yes" : "No" %></td>
                        <td><%= volunteer != null ? volunteer.getName() : "Not assigned" %></td>
                        <td><%= req.getRequestDate() %></td>
                    </tr>
                <% } %>
                <% if (allRequests.isEmpty()) { %>
                    <tr><td colspan="8" style="text-align: center;">No help requests found</td></tr>
                <% } %>
            </tbody>
        </table>
        
        <div class="stats-grid">
            <div class="stat-card"><h3><%= openCount %></h3><p>🟡 Open</p></div>
            <div class="stat-card"><h3><%= assignedCount %></h3><p>🔵 Assigned</p></div>
            <div class="stat-card"><h3><%= completedCount %></h3><p>✅ Completed</p></div>
            <div class="stat-card"><h3><%= urgentCount %></h3><p>🚨 Urgent</p></div>
        </div>
    </div>
    
    <script src="script.js"></script>
</body>
</html>