/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.dao;

import za.ac.tut.model.entity.HealthLesson;
import java.util.List;

/**
 * HealthLesson specific DAO operations
 * 
 * @author USER
 */
public interface HealthLessonDAO extends GenericDAO<HealthLesson, Long> {
    
    // Find lessons by language
    List<HealthLesson> findByLanguage(String language);
    
    // Find lessons by lesson key (hiv, diabetes, hypertension, tb)
    List<HealthLesson> findByLessonKey(String lessonKey);
    
    // Find active lessons only
    List<HealthLesson> findActiveLessons();
    
    // Find lessons by language and lesson key
    HealthLesson findByLanguageAndKey(String language, String lessonKey);
    
    // Get all available languages
    List<String> getAvailableLanguages();
    
    // Get all lesson keys
    List<String> getLessonKeys();
    
    // Get lesson count by language
    long countByLanguage(String language);
    
    // Deactivate lesson (soft delete)
    void deactivateLesson(Long lessonId);
    
    // Activate lesson
    void activateLesson(Long lessonId);
    
    // Search lessons by title or content
    List<HealthLesson> searchLessons(String searchTerm);
}