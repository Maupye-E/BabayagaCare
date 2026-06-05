<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="za.ac.tut.model.entity.User"%>
<%@page import="za.ac.tut.model.service.HealthLessonService"%>
<%@page import="za.ac.tut.model.entity.HealthLesson"%>
<%@page import="java.util.List"%>
<%
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null || !loggedInUser.isAdmin()) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    HealthLessonService lessonService = new HealthLessonService();
    String action = request.getParameter("action");
    String message = "";
    String messageType = "success";
    
    // Handle Add Lesson
    if ("add".equals(action)) {
        String lessonKey = request.getParameter("lessonKey");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String language = request.getParameter("language");
        
        HealthLesson newLesson = lessonService.createLesson(lessonKey, title, content, language);
        if (newLesson != null) {
            message = "✅ Lesson added successfully!";
        } else {
            message = "❌ Failed to add lesson";
            messageType = "error";
        }
    }
    
    // Handle Update Lesson
    if ("update".equals(action)) {
        Long lessonId = Long.parseLong(request.getParameter("lessonId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        
        HealthLesson updated = lessonService.updateLesson(lessonId, title, content);
        if (updated != null) {
            message = "✅ Lesson updated successfully!";
        } else {
            message = "❌ Failed to update lesson";
            messageType = "error";
        }
    }
    
    // Handle Delete Lesson
    if ("delete".equals(action)) {
        Long lessonId = Long.parseLong(request.getParameter("lessonId"));
        boolean deleted = lessonService.deleteLesson(lessonId);
        if (deleted) {
            message = "✅ Lesson deleted successfully!";
        } else {
            message = "❌ Failed to delete lesson";
            messageType = "error";
        }
    }
    
    // Handle Toggle Active Status
    if ("toggleActive".equals(action)) {
        Long lessonId = Long.parseLong(request.getParameter("lessonId"));
        HealthLesson lesson = lessonService.getLessonById(lessonId);
        if (lesson != null) {
            if (lesson.isActive()) {
                lessonService.deactivateLesson(lessonId);
                message = "✅ Lesson deactivated!";
            } else {
                lessonService.activateLesson(lessonId);
                message = "✅ Lesson activated!";
            }
        }
    }
    
    List<HealthLesson> allLessons = lessonService.findAll();
    String editId = request.getParameter("editId");
    HealthLesson editLesson = null;
    if (editId != null) {
        editLesson = lessonService.getLessonById(Long.parseLong(editId));
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Health Lessons - Admin</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="container">
        <div class="dashboard-header">
            <div>
                <h2>📚 Manage Health Lessons</h2>
                <p>Welcome, <%= loggedInUser.getName() %>! | 👑 Admin</p>
            </div>
            <div class="header-buttons">
                <button onclick="window.location.href='admin_dashboard.jsp'" class="btn btn-secondary">← Back to Admin</button>
                <button onclick="logout()" class="btn btn-secondary">🚪 Logout</button>
            </div>
        </div>
        
        <% if (!message.isEmpty()) { %>
            <div class="message <%= messageType %>"><%= message %></div>
        <% } %>
        
        <!-- Add/Edit Lesson Form -->
        <div class="edit-form">
            <h3><%= editLesson != null ? "✏️ Edit Lesson" : "➕ Add New Lesson" %></h3>
            <form action="admin_lessons.jsp" method="post">
                <input type="hidden" name="action" value="<%= editLesson != null ? "update" : "add" %>">
                <% if (editLesson != null) { %>
                    <input type="hidden" name="lessonId" value="<%= editLesson.getId() %>">
                <% } %>
                <div class="form-group">
                    <label>Lesson Key *</label>
                    <select name="lessonKey" required <%= editLesson != null ? "disabled" : "" %>>
                        <option value="hiv" <%= (editLesson != null && "hiv".equals(editLesson.getLessonKey())) ? "selected" : "" %>>🩸 HIV/AIDS</option>
                        <option value="diabetes" <%= (editLesson != null && "diabetes".equals(editLesson.getLessonKey())) ? "selected" : "" %>>🍬 Diabetes</option>
                        <option value="hypertension" <%= (editLesson != null && "hypertension".equals(editLesson.getLessonKey())) ? "selected" : "" %>>❤️ Hypertension</option>
                        <option value="tb" <%= (editLesson != null && "tb".equals(editLesson.getLessonKey())) ? "selected" : "" %>>🫁 Tuberculosis (TB)</option>
                        <option value="other" <%= (editLesson != null && "other".equals(editLesson.getLessonKey())) ? "selected" : "" %>>📖 Other</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Title *</label>
                    <input type="text" name="title" required value="<%= editLesson != null ? editLesson.getTitle() : "" %>" placeholder="e.g., Understanding HIV">
                </div>
                <div class="form-group">
                    <label>Language *</label>
                    <select name="language" required>
                        <option value="English" <%= (editLesson != null && "English".equals(editLesson.getLanguage())) ? "selected" : "" %>>English</option>
                        <option value="isiZulu" <%= (editLesson != null && "isiZulu".equals(editLesson.getLanguage())) ? "selected" : "" %>>isiZulu</option>
                        <option value="Sesotho" <%= (editLesson != null && "Sesotho".equals(editLesson.getLanguage())) ? "selected" : "" %>>Sesotho</option>
                        <option value="Afrikaans" <%= (editLesson != null && "Afrikaans".equals(editLesson.getLanguage())) ? "selected" : "" %>>Afrikaans</option>
                        <option value="isiXhosa" <%= (editLesson != null && "isiXhosa".equals(editLesson.getLanguage())) ? "selected" : "" %>>isiXhosa</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Content *</label>
                    <textarea name="content" rows="8" required placeholder="Write the full lesson content here..."><%= editLesson != null ? editLesson.getContent() : "" %></textarea>
                </div>
                <div class="form-buttons">
                    <button type="submit" class="btn btn-primary"><%= editLesson != null ? "Update Lesson" : "Add Lesson" %></button>
                    <% if (editLesson != null) { %>
                        <a href="admin_lessons.jsp" class="btn btn-secondary">Cancel Edit</a>
                    <% } %>
                </div>
            </form>
        </div>
        
        <!-- Lessons List -->
        <h3>📋 Existing Lessons</h3>
        <table class="lessons-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Icon</th>
                    <th>Title</th>
                    <th>Language</th>
                    <th>Content Preview</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% for (HealthLesson lesson : allLessons) { %>
                    <tr class="lesson-row">
                        <td><%= lesson.getId() %></td>
                        <td class="lesson-icon-cell"><%= lesson.getIcon() %></td>
                        <td><%= lesson.getTitle() %></td>
                        <td><%= lesson.getLanguage() %></td>
                        <td class="lesson-content-preview"><%= lesson.getShortContent(50) %></td>
                        <td class="<%= lesson.isActive() ? "status-active" : "status-inactive" %>">
                            <%= lesson.isActive() ? "🟢 Active" : "🔴 Inactive" %>
                        </td>
                        <td class="action-buttons">
                            <a href="admin_lessons.jsp?editId=<%= lesson.getId() %>" class="action-btn action-btn-primary">✏️ Edit</a>
                            <a href="admin_lessons.jsp?action=toggleActive&lessonId=<%= lesson.getId() %>" 
                               class="action-btn action-btn-warning" 
                               onclick="return confirm('<%= lesson.isActive() ? "Deactivate" : "Activate" %> this lesson?')">
                                <%= lesson.isActive() ? "🔴 Deactivate" : "🟢 Activate" %>
                            </a>
                            <a href="admin_lessons.jsp?action=delete&lessonId=<%= lesson.getId() %>" 
                               class="action-btn action-btn-danger" 
                               onclick="return confirm('Delete this lesson permanently?')">🗑️ Delete</a>
                        </td>
                    </tr>
                <% } %>
                <% if (allLessons.isEmpty()) { %>
                    <tr>
                        <td colspan="7" style="text-align: center;">No lessons found. Click "Add New Lesson" to create one.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>
        
        <div class="stats-grid mt-20">
            <div class="stat-card">
                <h3><%= allLessons.size() %></h3>
                <p>📚 Total Lessons</p>
            </div>
            <div class="stat-card">
                <h3><%= lessonService.getAvailableLanguages().size() %></h3>
                <p>🗣️ Languages</p>
            </div>
        </div>
    </div>
    
    <script src="script.js"></script>
</body>
</html>