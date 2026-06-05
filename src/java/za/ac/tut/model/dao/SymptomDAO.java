/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.dao;

import za.ac.tut.model.entity.Symptom;
import za.ac.tut.model.entity.User;
import java.util.Date;
import java.util.List;

/**
 * Symptom specific DAO operations
 * 
 * @author USER
 */
public interface SymptomDAO extends GenericDAO<Symptom, Long> {
    
    // Find symptoms by user
    List<Symptom> findByUser(User user);
    
    // Find symptoms by user ID
    List<Symptom> findByUserId(Long userId);
    
    // Find symptoms by date range
    List<Symptom> findByDateRange(Long userId, Date startDate, Date endDate);
    
    // Find symptoms by severity level
    List<Symptom> findBySeverity(Integer severity);
    
    // Find recent symptoms (last 7 days)
    List<Symptom> findRecentSymptoms(Long userId, int days);
    
    // Get symptoms for today
    List<Symptom> getTodaySymptoms(Long userId);
    
    // Get symptoms by symptom name
    List<Symptom> findBySymptomName(Long userId, String symptomName);
    
    // Get average severity for a user
    double getAverageSeverity(Long userId);
    
    // Get most common symptoms for a user
    List<Object[]> getMostCommonSymptoms(Long userId, int limit);
    
    // Get symptom trends over time
    List<Object[]> getSymptomTrends(Long userId, int days);
    
    // Delete old symptoms (older than days)
    int deleteOldSymptoms(int days);
}