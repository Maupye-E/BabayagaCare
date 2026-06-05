/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.service;

import za.ac.tut.model.dao.SymptomDAO;
import za.ac.tut.model.dao.UserDAO;
import za.ac.tut.model.dao.impl.SymptomDAOImpl;
import za.ac.tut.model.dao.impl.UserDAOImpl;
import za.ac.tut.model.entity.Symptom;
import za.ac.tut.model.entity.User;
import java.util.Date;
import java.util.List;

/**
 * Service class for Symptom business logic
 * 
 * @author USER
 */
public class SymptomService {
    
    private SymptomDAO symptomDAO;
    private UserDAO userDAO;
    
    public SymptomService() {
        this.symptomDAO = new SymptomDAOImpl();
        this.userDAO = new UserDAOImpl();
    }
    
    // Log new symptom
    public Symptom logSymptom(Long userId, String symptomName, Integer severity, String notes) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return null;
        }
        
        Symptom symptom = new Symptom(symptomName, severity, notes, user);
        return symptomDAO.save(symptom);
    }
    
    // Delete symptom
    public boolean deleteSymptom(Long symptomId) {
        if (symptomDAO.existsById(symptomId)) {
            symptomDAO.deleteById(symptomId);
            return true;
        }
        return false;
    }
    
    // Get user's symptoms
    public List<Symptom> getUserSymptoms(Long userId) {
        return symptomDAO.findByUserId(userId);
    }
    
    // Get recent symptoms (last 7 days)
    public List<Symptom> getRecentSymptoms(Long userId) {
        return symptomDAO.findRecentSymptoms(userId, 7);
    }
    
    // Get today's symptoms
    public List<Symptom> getTodaySymptoms(Long userId) {
        return symptomDAO.getTodaySymptoms(userId);
    }
    
    // Get symptoms by date range
    public List<Symptom> getSymptomsByDateRange(Long userId, Date startDate, Date endDate) {
        return symptomDAO.findByDateRange(userId, startDate, endDate);
    }
    
    // Get average severity
    public double getAverageSeverity(Long userId) {
        return symptomDAO.getAverageSeverity(userId);
    }
    
    // Get most common symptoms
    public List<Object[]> getMostCommonSymptoms(Long userId, int limit) {
        return symptomDAO.getMostCommonSymptoms(userId, limit);
    }
    
    // Get symptom trends
    public List<Object[]> getSymptomTrends(Long userId, int days) {
        return symptomDAO.getSymptomTrends(userId, days);
    }
}