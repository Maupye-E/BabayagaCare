<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="za.ac.tut.model.entity.User"%>
<%@page import="za.ac.tut.model.service.UserService"%>
<%@page import="java.util.List"%>
<%
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null || !loggedInUser.isAdmin()) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    UserService userService = new UserService();
    List<User> allUsers = userService.findAll();
    String action = request.getParameter("action");
    String message = "";
    String messageType = "success";
    
    if ("toggleActive".equals(action)) {
        Long userId = Long.parseLong(request.getParameter("userId"));
        User targetUser = userService.getUserById(userId);
        if (targetUser != null && !targetUser.isAdmin()) {
            if (targetUser.isActive()) {
                userService.deactivateUser(userId);
                message = "✅ User deactivated!";
            } else {
                userService.activateUser(userId);
                message = "✅ User activated!";
            }
        }
    }
    
    if ("makeAdmin".equals(action)) {
        Long userId = Long.parseLong(request.getParameter("userId"));
        User targetUser = userService.getUserById(userId);
        if (targetUser != null && !targetUser.isAdmin()) {
            targetUser.setRole("admin");
            userService.updateUserProfile(userId, targetUser.getName(), targetUser.getLanguage(), targetUser.getLocation());
            message = "✅ User promoted to Admin!";
        }
    }
    
    if ("removeAdmin".equals(action)) {
        Long userId = Long.parseLong(request.getParameter("userId"));
        User targetUser = userService.getUserById(userId);
        if (targetUser != null && targetUser.isAdmin() && targetUser.getId() != loggedInUser.getId()) {
            targetUser.setRole("patient");
            userService.updateUserProfile(userId, targetUser.getName(), targetUser.getLanguage(), targetUser.getLocation());
            message = "✅ Admin privileges removed!";
        } else if (targetUser != null && targetUser.getId() == loggedInUser.getId()) {
            message = "❌ You cannot remove your own admin privileges!";
            messageType = "error";
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Users - Admin</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="container">
        <div class="dashboard-header">
            <div><h2>👥 User Management</h2><p>Welcome, <%= loggedInUser.getName() %>! | 👑 Admin</p></div>
            <div class="header-buttons">
                <button onclick="window.location.href='admin_dashboard.jsp'" class="btn btn-secondary">← Back</button>
                <button onclick="logout()" class="btn btn-secondary">🚪 Logout</button>
            </div>
        </div>
        
        <% if (!message.isEmpty()) { %>
            <div class="message <%= messageType %>"><%= message %></div>
        <% } %>
        
        <table class="users-table">
            <thead><tr><th>ID</th><th>Name</th><th>Phone</th><th>Location</th><th>Role</th><th>Status</th><th>Registered</th><th>Actions</th></tr></thead>
            <tbody>
                <% for (User u : allUsers) { %>
                    <tr>
                        <td><%= u.getId() %></td>
                        <td><%= u.getName() %></td>
                        <td><%= u.getPhone() %></td>
                        <td><%= u.getLocation() != null ? u.getLocation() : "Not set" %></td>
                        <td><span class="role-badge role-<%= u.getRole() %>"><%= u.isAdmin() ? "👑 Admin" : u.isVolunteer() ? "🤝 Volunteer" : "👤 Patient" %></span></td>
                        <td><span class="<%= u.isActive() ? "status-active" : "status-inactive" %>"><%= u.isActive() ? "🟢 Active" : "🔴 Inactive" %></span></td>
                        <td><%= u.getRegistrationDate() %></td>
                        <td>
                            <% if (!u.isAdmin()) { %>
                                <a href="admin_users.jsp?action=makeAdmin&userId=<%= u.getId() %>" class="action-btn action-btn-success" onclick="return confirm('Make admin?')">👑 Make Admin</a>
                            <% } else if (u.getId() != loggedInUser.getId()) { %>
                                <a href="admin_users.jsp?action=removeAdmin&userId=<%= u.getId() %>" class="action-btn action-btn-warning" onclick="return confirm('Remove admin?')">⬇️ Remove</a>
                            <% } %>
                            <a href="reset_password.jsp?userId=<%= u.getId() %>" class="action-btn action-btn-primary">🔐 Reset</a>
                            <% if (!u.isAdmin()) { %>
                                <a href="admin_users.jsp?action=toggleActive&userId=<%= u.getId() %>" class="action-btn action-btn-danger" onclick="return confirm('<%= u.isActive() ? "Deactivate" : "Activate" %>?')"><%= u.isActive() ? "🔴 Deactivate" : "🟢 Activate" %></a>
                            <% } %>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
        
        <div class="stats-grid">
            <div class="stat-card"><h3><%= allUsers.size() %></h3><p>Total Users</p></div>
            <div class="stat-card"><h3><%= userService.getPatientsCount() %></h3><p>Patients</p></div>
            <div class="stat-card"><h3><%= userService.getVolunteersCount() %></h3><p>Volunteers</p></div>
            <div class="stat-card"><h3><%= userService.getAdminsCount() %></h3><p>Admins</p></div>
        </div>
    </div>
    
    <script src="script.js"></script>
</body>
</html>