<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="za.ac.tut.model.service.UserService"%>
<%@page import="za.ac.tut.model.entity.User"%>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    String message = "";
    String messageType = "error";
    String action = request.getParameter("action");
    
    if ("change".equals(action)) {
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        if (newPassword.equals(confirmPassword)) {
            if (newPassword.length() >= 6) {
                UserService userService = new UserService();
                boolean changed = userService.changePassword(user.getId(), oldPassword, newPassword);
                
                if (changed) {
                    message = "✅ Password changed successfully! Please login again.";
                    messageType = "success";
                    session.invalidate();
                    response.setHeader("Refresh", "3;url=login.jsp");
                } else {
                    message = "❌ Old password is incorrect!";
                }
            } else {
                message = "❌ New password must be at least 6 characters!";
            }
        } else {
            message = "❌ New passwords do not match!";
        }
    }
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Change Password - Babayaga Care</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="password-container">
        <div class="password-header">
            <h2>🔐 Change Password</h2>
            <p>Welcome, <%= user.getName() %>!</p>
        </div>
        
        <% if (!message.isEmpty()) { %>
            <div class="message <%= messageType %>"><%= message %></div>
        <% } %>
        
        <form action="change_password.jsp" method="post" onsubmit="return validateChangePasswordForm()">
            <input type="hidden" name="action" value="change">
            <div class="form-group">
                <label>🔐 Current Password</label>
                <input type="password" id="oldPassword" name="oldPassword" required>
            </div>
            <div class="form-group">
                <label>🔐 New Password</label>
                <input type="password" id="newPassword" name="newPassword" required placeholder="Min 6 characters">
            </div>
            <div class="form-group">
                <label>🔐 Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>
            <button type="submit" class="btn btn-primary full-width">Update Password</button>
        </form>
        
        <div class="btn-back"><a href="dashboard.jsp">← Back to Dashboard</a></div>
    </div>
    
    <script src="script.js"></script>
</body>
</html>