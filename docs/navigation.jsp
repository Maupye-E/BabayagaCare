<%-- 
    Document   : navigation
    Created on : 05 Jun 2026, 2:17:22 PM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="za.ac.tut.model.entity.User"%>
<%
    User navUser = (User) session.getAttribute("user");
    if (navUser == null) {
        return;
    }
%>
<div class="top-nav">
    <div class="nav-brand">
        <a href="dashboard.jsp">🧙‍♀️ Babayaga Care</a>
    </div>
    <div class="nav-links">
        <a href="dashboard.jsp">📊 Dashboard</a>
        
        <% if (navUser.isVolunteer()) { %>
            <a href="volunteer.jsp">🤝 Volunteer Dashboard</a>
        <% } %>
        
        <% if (navUser.isAdmin()) { %>
            <a href="admin_dashboard.jsp">👑 Admin Dashboard</a>
            <a href="admin_users.jsp">👥 Manage Users</a>
            <a href="admin_help_requests.jsp">📋 Help Requests</a>
            <a href="reset_password.jsp">🔐 Reset Passwords</a>
        <% } %>
        
        <a href="change_password.jsp">🔑 Change Password</a>
        <a href="logout.jsp" onclick="return confirm('Are you sure you want to logout?')">🚪 Logout</a>
    </div>
    <div class="nav-user">
        👤 <%= navUser.getName() %> | <%= navUser.getRole() %>
    </div>
</div>