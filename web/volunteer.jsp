<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="za.ac.tut.model.entity.User"%>
<%@page import="za.ac.tut.model.service.HelpRequestService"%>
<%@page import="za.ac.tut.model.entity.HelpRequest"%>
<%@page import="java.util.List"%>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"volunteer".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    HelpRequestService helpRequestService = new HelpRequestService();
    List<HelpRequest> openRequests = helpRequestService.getOpenRequests();
    List<HelpRequest> myRequests = helpRequestService.getVolunteerRequests(user.getId());
    
    String action = request.getParameter("action");
    String message = "";
    
    if ("assign".equals(action)) {
        Long requestId = Long.parseLong(request.getParameter("id"));
        if (helpRequestService.assignVolunteer(requestId, user.getId())) {
            message = "Request assigned successfully!";
            response.sendRedirect("volunteer.jsp?msg=" + java.net.URLEncoder.encode(message, "UTF-8"));
            return;
        }
    }
    
    if ("complete".equals(action)) {
        Long requestId = Long.parseLong(request.getParameter("id"));
        if (helpRequestService.completeRequest(requestId)) {
            message = "Request marked as completed!";
            response.sendRedirect("volunteer.jsp?msg=" + java.net.URLEncoder.encode(message, "UTF-8"));
            return;
        }
    }
    
    String msgParam = request.getParameter("msg");
    if (msgParam != null) {
        message = msgParam;
    }
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volunteer Dashboard - Babayaga Care</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="container">
        <div class="dashboard-header">
            <div>
                <h2>🙋‍♂️ Welcome, Volunteer <%= user.getName() %>!</h2>
                <p>📍 <%= user.getLocation() != null ? user.getLocation() : "Location not set" %></p>
            </div>
            <div class="header-buttons">
                <button onclick="window.location.href='dashboard.jsp'" class="btn btn-secondary">← Dashboard</button>
                <button onclick="window.location.href='change_password.jsp'" class="btn btn-secondary">🔐 Change Password</button>
                <button onclick="logout()" class="btn btn-secondary">🚪 Logout</button>
            </div>
        </div>
        
        <% if (!message.isEmpty()) { %>
            <div class="message success"><%= message %></div>
        <% } %>
        
        <div class="section">
            <h2>🆕 Open Help Requests</h2>
            <% if (openRequests.isEmpty()) { %>
                <p>No open requests at the moment.</p>
            <% } else { %>
                <% for (HelpRequest req : openRequests) { %>
                    <div class="request-card <%= req.isUrgent() ? "urgent" : "" %>">
                        <div class="request-header">
                            <div class="request-info">
                                <strong><%= req.getRequestTypeIcon() %> <%= req.getRequestTypeText() %></strong>
                                <% if (req.isUrgent()) { %><span class="badge badge-urgent">🚨 URGENT</span><% } %>
                                <span class="badge badge-open"><%= req.getStatus() %></span>
                            </div>
                            <a href="volunteer.jsp?action=assign&id=<%= req.getId() %>" class="btn btn-primary btn-small">Accept</a>
                        </div>
                        <div class="request-details">
                            <div>📍 <%= req.getLocation() %></div>
                            <div>📝 <%= req.getDetails() %></div>
                            <div>📅 <%= req.getRequestDate() %></div>
                        </div>
                    </div>
                <% } %>
            <% } %>
        </div>
        
        <div class="section">
            <h2>📋 My Assigned Requests</h2>
            <% if (myRequests.isEmpty()) { %>
                <p>You haven't accepted any requests yet.</p>
            <% } else { %>
                <% for (HelpRequest req : myRequests) { %>
                    <div class="request-card">
                        <div class="request-header">
                            <div class="request-info">
                                <strong><%= req.getRequestTypeIcon() %> <%= req.getRequestTypeText() %></strong>
                                <span class="badge badge-assigned"><%= req.getStatus() %></span>
                            </div>
                            <% if ("assigned".equals(req.getStatus())) { %>
                                <a href="volunteer.jsp?action=complete&id=<%= req.getId() %>" class="btn btn-success btn-small">Mark Complete</a>
                            <% } %>
                        </div>
                        <div class="request-details">
                            <div>📍 <%= req.getLocation() %></div>
                            <div>📝 <%= req.getDetails() %></div>
                            <div>📅 <%= req.getRequestDate() %></div>
                        </div>
                    </div>
                <% } %>
            <% } %>
        </div>
    </div>
    
    <script src="script.js"></script>
</body>
</html>