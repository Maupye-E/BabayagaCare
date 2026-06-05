<%-- 
    Document   : logout
    Created on : 05 Jun 2026, 11:13:46 AM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Invalidate the session (destroy all session data)
    session.invalidate();
    
    // Redirect to login page
    response.sendRedirect("login.jsp");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="refresh" content="2;url=login.jsp">
        <title>Logging out...</title>
        <link rel="stylesheet" href="style.css">
    </head>
    <body class="logout-page">
        <div class="logout-container">
            <h2>🚪 Logging out...</h2>
            <p>Please wait while you are redirected to the login page.</p>
            <p>If you are not redirected automatically, <a href="login.jsp">click here</a>.</p>
        </div>
    </body>
</html>