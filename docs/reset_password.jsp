<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="za.ac.tut.model.service.UserService"%>
<%@page import="za.ac.tut.model.entity.User"%>
<%@page import="java.util.List"%>
<%
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null || !loggedInUser.isAdmin()) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    String message = "";
    String messageType = "error";
    String action = request.getParameter("action");
    Long selectedUserId = null;
    User selectedUser = null;
    
    if ("reset".equals(action)) {
        String userIdParam = request.getParameter("userId");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        if (userIdParam != null) {
            selectedUserId = Long.parseLong(userIdParam);
            UserService userService = new UserService();
            selectedUser = userService.getUserById(selectedUserId);
            
            if (newPassword != null && newPassword.equals(confirmPassword) && newPassword.length() >= 6) {
                boolean reset = userService.resetPassword(selectedUserId, newPassword);
                if (reset) {
                    message = "✅ Password reset successfully for " + selectedUser.getName() + "!";
                    messageType = "success";
                } else {
                    message = "❌ Failed to reset password.";
                }
            } else {
                message = "❌ Passwords do not match or are too short (min 6 characters)!";
            }
        }
    }
    
    UserService userService = new UserService();
    List<User> allUsers = userService.findAll();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password - Admin</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="admin-container">
        <div class="admin-header">
            <h2>👑 Admin: Reset User Password</h2>
            <p>Welcome, <%= loggedInUser.getName() %>!</p>
        </div>
        
        <% if (!message.isEmpty()) { %>
            <div class="message <%= messageType %>"><%= message %></div>
        <% } %>
        
        <div class="user-list">
            <h3>📋 All Users</h3>
            <% for (User u : allUsers) { %>
                <div class="user-item" data-id="<%= u.getId() %>" onclick="selectAdminUser(<%= u.getId() %>, '<%= u.getName() %>')">
                    <strong><%= u.getName() %></strong><br>
                    <small>📱 <%= u.getPhone() %> | 📍 <%= u.getLocation() != null ? u.getLocation() : "Not set" %> | 👥 <%= u.getRole() %></small>
                </div>
            <% } %>
        </div>
        
        <div id="selectedUserInfo" class="selected-user-info" style="display: none;">
            <span id="selectedUserName"></span>
        </div>
        
        <form action="reset_password.jsp" method="post" onsubmit="return validateAdminResetForm()">
            <input type="hidden" name="action" value="reset">
            <input type="hidden" id="selectedUserId" name="userId">
            <div class="form-group">
                <label>🔐 New Password</label>
                <input type="password" id="adminNewPassword" name="newPassword" required placeholder="Min 6 characters">
            </div>
            <div class="form-group">
                <label>🔐 Confirm Password</label>
                <input type="password" id="adminConfirmPassword" name="confirmPassword" required>
            </div>
            <button type="submit" class="btn btn-primary full-width">Reset Password</button>
        </form>
        
        <div class="btn-back"><a href="dashboard.jsp">← Back to Dashboard</a></div>
    </div>
    
    <script src="script.js"></script>
</body>
</html>