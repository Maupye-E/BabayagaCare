/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.service;

import za.ac.tut.model.dao.HealthLessonDAO;
import za.ac.tut.model.dao.impl.HealthLessonDAOImpl;
import za.ac.tut.model.entity.HealthLesson;
import java.util.List;

/**
 * Service class for HealthLesson business logic
 * 
 * @author USER
 */
public class HealthLessonService {
    
    private HealthLessonDAO lessonDAO;
    
    public HealthLessonService() {
        this.lessonDAO = new HealthLessonDAOImpl();
    }
    
    // ============ BASIC CRUD ============
    
    // Get lesson by ID
    public HealthLesson getLessonById(Long lessonId) {
        return lessonDAO.findById(lessonId);
    }
    
    // Get all lessons (for admin)
    public List<HealthLesson> findAll() {
        return lessonDAO.findAll();
    }
    
    // Create new lesson
    public HealthLesson createLesson(String lessonKey, String title, String content, String language) {
        HealthLesson lesson = new HealthLesson(lessonKey, title, content, language);
        return lessonDAO.save(lesson);
    }
    
    // Update lesson
    public HealthLesson updateLesson(Long lessonId, String title, String content) {
        HealthLesson lesson = lessonDAO.findById(lessonId);
        if (lesson != null) {
            lesson.setTitle(title);
            lesson.setContent(content);
            return lessonDAO.update(lesson);
        }
        return null;
    }
    
    // Update full lesson
    public HealthLesson updateFullLesson(Long lessonId, String lessonKey, String title, String content, String language, boolean active) {
        HealthLesson lesson = lessonDAO.findById(lessonId);
        if (lesson != null) {
            lesson.setLessonKey(lessonKey);
            lesson.setTitle(title);
            lesson.setContent(content);
            lesson.setLanguage(language);
            lesson.setActive(active);
            return lessonDAO.update(lesson);
        }
        return null;
    }
    
    // Delete lesson
    public boolean deleteLesson(Long lessonId) {
        if (lessonDAO.existsById(lessonId)) {
            lessonDAO.deleteById(lessonId);
            return true;
        }
        return false;
    }
    
    // ============ QUERY METHODS ============
    
    // Get lessons by language
    public List<HealthLesson> getLessonsByLanguage(String language) {
        return lessonDAO.findByLanguage(language);
    }
    
    // Get lesson by language and key
    public HealthLesson getLessonByLanguageAndKey(String language, String lessonKey) {
        return lessonDAO.findByLanguageAndKey(language, lessonKey);
    }
    
    // Get all active lessons
    public List<HealthLesson> getAllActiveLessons() {
        return lessonDAO.findActiveLessons();
    }
    
    // Get lessons by lesson key
    public List<HealthLesson> getLessonsByLessonKey(String lessonKey) {
        return lessonDAO.findByLessonKey(lessonKey);
    }
    
    // ============ UTILITY METHODS ============
    
    // Get available languages
    public List<String> getAvailableLanguages() {
        return lessonDAO.getAvailableLanguages();
    }
    
    // Get lesson keys
    public List<String> getLessonKeys() {
        return lessonDAO.getLessonKeys();
    }
    
    // Get lesson count by language
    public long getLessonCountByLanguage(String language) {
        return lessonDAO.countByLanguage(language);
    }
    
    // Search lessons
    public List<HealthLesson> searchLessons(String searchTerm) {
        return lessonDAO.searchLessons(searchTerm);
    }
    
    // ============ ADMIN METHODS ============
    
    // Activate lesson
    public void activateLesson(Long lessonId) {
        lessonDAO.activateLesson(lessonId);
    }
    
    // Deactivate lesson
    public void deactivateLesson(Long lessonId) {
        lessonDAO.deactivateLesson(lessonId);
    }
    
    // Toggle lesson active status
    public void toggleLessonStatus(Long lessonId) {
        HealthLesson lesson = lessonDAO.findById(lessonId);
        if (lesson != null) {
            if (lesson.isActive()) {
                lessonDAO.deactivateLesson(lessonId);
            } else {
                lessonDAO.activateLesson(lessonId);
            }
        }
    }
}