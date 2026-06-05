<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="za.ac.tut.model.service.UserService"%>
<%@page import="za.ac.tut.model.entity.User"%>
<%
    if (session.getAttribute("user") != null) {
        response.sendRedirect("dashboard.jsp");
        return;
    }
    
    String action = request.getParameter("action");
    String message = "";
    String messageType = "error";
    
    if ("login".equals(action)) {
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        
        UserService userService = new UserService();
        User user = userService.login(phone, password);
        
        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("userRole", user.getRole());
            response.sendRedirect("dashboard.jsp");
            return;
        } else {
            message = "❌ Invalid phone number or password. Please try again.";
        }
    }
    
    if ("register".equals(action)) {
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String language = request.getParameter("language");
        String location = request.getParameter("location");
        String role = request.getParameter("role");
        
        if (!password.equals(confirmPassword)) {
            message = "❌ Passwords do not match!";
        } else if (password.length() < 6) {
            message = "❌ Password must be at least 6 characters long!";
        } else {
            UserService userService = new UserService();
            User user = null;
            
            if ("volunteer".equals(role)) {
                user = userService.registerVolunteer(name, phone, password, language, location);
            } else {
                user = userService.registerUser(name, phone, password, language, location);
            }
            
            if (user != null) {
                message = "✅ Registration successful! Please login with your password.";
                messageType = "success";
            } else {
                message = "❌ Phone number already exists. Please use a different number or login.";
            }
        }
    }
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Babayaga Care</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="login-container">
        <h2 class="text-center">🧙‍♀️ Babayaga Care</h2>
        
        <% if (!message.isEmpty()) { %>
            <div class="message <%= messageType %>"><%= message %></div>
        <% } %>
        
        <div class="tab-container">
            <button class="tab active" onclick="showPanel('login')">Login</button>
            <button class="tab" onclick="showPanel('register')">Register</button>
        </div>
        
        <div id="loginPanel" class="form-panel active">
            <form action="login.jsp" method="post" onsubmit="return handleLoginForm(event)">
                <input type="hidden" name="action" value="login">
                <div class="form-group">
                    <label>📱 Phone Number</label>
                    <input type="tel" id="loginPhone" name="phone" placeholder="e.g., 0712345678" required>
                </div>
                <div class="form-group">
                    <label>🔐 Password</label>
                    <input type="password" id="loginPassword" name="password" placeholder="Enter your password" required>
                </div>
                <button type="submit" class="btn btn-primary full-width">Login</button>
            </form>
            <div class="forgot-link"><a href="forgot_password.jsp">Forgot Password?</a></div>
        </div>
        
        <div id="registerPanel" class="form-panel">
            <form action="login.jsp" method="post" onsubmit="return validateRegistrationForm()">
                <input type="hidden" name="action" value="register">
                <div class="form-group">
                    <label>👤 Full Name</label>
                    <input type="text" id="regName" name="name" required placeholder="Thabo Nkosi">
                </div>
                <div class="form-group">
                    <label>📱 Phone Number</label>
                    <input type="tel" id="regPhone" name="phone" required placeholder="0712345678">
                </div>
                <div class="form-group">
                    <label>🔐 Password</label>
                    <input type="password" id="regPassword" name="password" required placeholder="Min 6 characters">
                </div>
                <div class="form-group">
                    <label>🔐 Confirm Password</label>
                    <input type="password" id="regConfirmPassword" name="confirmPassword" required placeholder="Re-enter password">
                </div>
                <div class="form-group">
                    <label>🗣️ Preferred Language</label>
                    <select name="language">
                        <option value="English">English</option>
                        <option value="isiZulu">isiZulu</option>
                        <option value="Sesotho">Sesotho</option>
                        <option value="Afrikaans">Afrikaans</option>
                        <option value="isiXhosa">isiXhosa</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>📍 Location</label>
                    <input type="text" name="location" placeholder="e.g., Soweto, Khayelitsha">
                </div>
                <div class="form-group">
                    <label>👥 Register as</label>
                    <select name="role">
                        <option value="patient">Patient (Need help)</option>
                        <option value="volunteer">Volunteer (Want to help)</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary full-width">Register</button>
            </form>
        </div>
        
        <p class="footer-text">Your data is stored securely in our database.</p>
    </div>
    
    <script src="script.js"></script>
</body>
</html>