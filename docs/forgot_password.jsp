<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="za.ac.tut.model.service.UserService"%>
<%@page import="za.ac.tut.model.entity.User"%>
<%
    String message = "";
    String messageType = "error";
    String action = request.getParameter("action");
    
    if ("reset".equals(action)) {
        String phone = request.getParameter("phone");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        if (newPassword != null && newPassword.equals(confirmPassword) && newPassword.length() >= 6) {
            UserService userService = new UserService();
            User user = userService.getUserByPhone(phone);
            
            if (user != null) {
                boolean reset = userService.resetPassword(user.getId(), newPassword);
                if (reset) {
                    message = "✅ Password reset successful! Redirecting to login...";
                    messageType = "success";
                    response.setHeader("Refresh", "3;url=login.jsp");
                } else {
                    message = "❌ Failed to reset password.";
                }
            } else {
                message = "❌ Phone number not found!";
            }
        } else {
            message = "❌ Passwords do not match or are too short (min 6 characters)!";
        }
    }
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password - Babayaga Care</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="login-container">
        <h2 class="text-center">🔐 Reset Your Password</h2>
        
        <% if (!message.isEmpty()) { %>
            <div class="message <%= messageType %>"><%= message %></div>
        <% } %>
        
        <form action="forgot_password.jsp" method="post" onsubmit="return validateForgotPasswordForm()">
            <input type="hidden" name="action" value="reset">
            <div class="form-group">
                <label>📱 Phone Number</label>
                <input type="tel" id="forgotPhone" name="phone" required placeholder="Enter your phone number">
            </div>
            <div class="form-group">
                <label>🔐 New Password</label>
                <input type="password" id="forgotNewPassword" name="newPassword" required placeholder="Min 6 characters">
            </div>
            <div class="form-group">
                <label>🔐 Confirm Password</label>
                <input type="password" id="forgotConfirmPassword" name="confirmPassword" required>
            </div>
            <button type="submit" class="btn btn-primary full-width">Reset Password</button>
        </form>
        <p class="text-center mt-20"><a href="login.jsp">← Back to Login</a></p>
    </div>
    
    <script src="script.js"></script>
</body>
</html>