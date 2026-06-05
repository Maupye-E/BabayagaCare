/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.dao;

import za.ac.tut.model.entity.Medication;
import za.ac.tut.model.entity.User;
import java.util.List;

/**
 * Medication specific DAO operations
 * 
 * @author USER
 */
public interface MedicationDAO extends GenericDAO<Medication, Long> {
    
    // Find medications by user
    List<Medication> findByUser(User user);
    
    // Find medications by user ID
    List<Medication> findByUserId(Long userId);
    
    // Find medications that need to be taken today (not taken yet)
    List<Medication> findPendingMedications(Long userId);
    
    // Find medications by time range (for reminders)
    List<Medication> findMedicationsByTimeRange(String startTime, String endTime);
    
    // Mark medication as taken
    void markAsTaken(Long medicationId);
    
    // Get today's medications for a user
    List<Medication> getTodayMedications(Long userId);
    
    // Get medication adherence rate (percentage)
    double getAdherenceRate(Long userId);
}