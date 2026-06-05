/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.service;

import za.ac.tut.model.dao.MedicationDAO;
import za.ac.tut.model.dao.UserDAO;
import za.ac.tut.model.dao.impl.MedicationDAOImpl;
import za.ac.tut.model.dao.impl.UserDAOImpl;
import za.ac.tut.model.entity.Medication;
import za.ac.tut.model.entity.User;
import java.util.List;

/**
 * Service class for Medication business logic
 * 
 * @author USER
 */
public class MedicationService {
    
    private MedicationDAO medicationDAO;
    private UserDAO userDAO;
    
    public MedicationService() {
        this.medicationDAO = new MedicationDAOImpl();
        this.userDAO = new UserDAOImpl();
    }
    
    // Add new medication
    public Medication addMedication(Long userId, String name, String dosage, String time, String instructions) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return null;
        }
        
        Medication medication = new Medication(name, dosage, time, instructions, user);
        return medicationDAO.save(medication);
    }
    
    // Update medication
    public Medication updateMedication(Long medicationId, String name, String dosage, String time, String instructions) {
        Medication medication = medicationDAO.findById(medicationId);
        if (medication != null) {
            medication.setName(name);
            medication.setDosage(dosage);
            medication.setTime(time);
            medication.setInstructions(instructions);
            return medicationDAO.update(medication);
        }
        return null;
    }
    
    // Delete medication
    public boolean deleteMedication(Long medicationId) {
        if (medicationDAO.existsById(medicationId)) {
            medicationDAO.deleteById(medicationId);
            return true;
        }
        return false;
    }
    
    // Get user's medications
    public List<Medication> getUserMedications(Long userId) {
        return medicationDAO.findByUserId(userId);
    }
    
    // Get pending medications (not taken)
    public List<Medication> getPendingMedications(Long userId) {
        return medicationDAO.findPendingMedications(userId);
    }
    
    // Mark medication as taken
    public void markMedicationAsTaken(Long medicationId) {
        medicationDAO.markAsTaken(medicationId);
    }
    
    // Get adherence rate
    public double getAdherenceRate(Long userId) {
        return medicationDAO.getAdherenceRate(userId);
    }
    
    // Get medications by time (for reminders)
    public List<Medication> getMedicationsByTime(String time) {
        return medicationDAO.findMedicationsByTimeRange(time, time);
    }
    
    // Get today's medications
    public List<Medication> getTodayMedications(Long userId) {
        return medicationDAO.getTodayMedications(userId);
    }
}