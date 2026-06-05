<%-- 
    Document   : lesson
    Created on : 05 Jun 2026, 11:14:12 AM
    Author     : USER
--%>

<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="za.ac.tut.model.service.HealthLessonService"%>
<%@page import="za.ac.tut.model.entity.HealthLesson"%>
<%@page import="za.ac.tut.model.entity.User"%>
<%
    // Check session
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    Long lessonId = null;
    try {
        lessonId = Long.parseLong(request.getParameter("id"));
    } catch (NumberFormatException e) {
        response.sendRedirect("dashboard.jsp");
        return;
    }
    
    HealthLessonService lessonService = new HealthLessonService();
    HealthLesson lesson = lessonService.getLessonById(lessonId);
    
    if (lesson == null) {
        response.sendRedirect("dashboard.jsp");
        return;
    }
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= lesson.getTitle() %> - Babayaga Care</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="lesson-container">
        <div class="lesson-header">
            <div class="lesson-icon"><%= lesson.getIcon() %></div>
            <h1><%= lesson.getTitle() %></h1>
            <p class="lesson-language">Language: <%= lesson.getLanguage() %></p>
        </div>
        <div class="lesson-content">
            <p><%= lesson.getContent() %></p>
        </div>
        <div class="back-btn">
            <button onclick="window.location.href='dashboard.jsp'" class="btn btn-primary">← Back to Dashboard</button>
        </div>
    </div>
    
    <script src="script.js"></script>
</body>
</html>