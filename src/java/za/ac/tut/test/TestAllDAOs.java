/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.test;

import za.ac.tut.model.dao.*;
import za.ac.tut.model.dao.impl.*;
import za.ac.tut.model.entity.*;
import java.util.List;

/**
 * Test class for all DAO operations
 * 
 * @author USER
 */
public class TestAllDAOs {
    
    public static void main(String[] args) {
        
        System.out.println("=== BABAYAGA CARE DAO TEST ===\n");
        
        // Test UserDAO
        testUserDAO();
        
        // Test MedicationDAO
        testMedicationDAO();
        
        // Test SymptomDAO
        testSymptomDAO();
        
        // Test HelpRequestDAO
        testHelpRequestDAO();
        
        // Test HealthLessonDAO
        testHealthLessonDAO();
        
        System.out.println("\n=== ALL TESTS COMPLETED ===");
    }
    
    private static void testUserDAO() {
        System.out.println("--- Testing UserDAO ---");
        UserDAO userDAO = new UserDAOImpl();
        
        // Check if user already exists
        User existingUser = userDAO.findByPhone("0712345678");
        
        if (existingUser == null) {
            // Create test user only if doesn't exist
            User testUser = new User("Test User", "0712345678", "English");
            testUser.setLocation("Soweto");
            User saved = userDAO.save(testUser);
            
            if (saved != null) {
                System.out.println("✓ User created with ID: " + saved.getId());
            } else {
                System.out.println("✗ Failed to create user");
            }
        } else {
            System.out.println("✓ Test user already exists with ID: " + existingUser.getId());
            System.out.println("✓ User name: " + existingUser.getName());
            System.out.println("✓ User phone: " + existingUser.getPhone());
        }
        
        // Find by phone
        User found = userDAO.findByPhone("0712345678");
        if (found != null) {
            System.out.println("✓ User found by phone: " + found.getName());
        }
        
        // Count users
        long count = userDAO.count();
        System.out.println("✓ Total users in database: " + count);
        System.out.println();
    }
    
    private static void testMedicationDAO() {
        System.out.println("--- Testing MedicationDAO ---");
        MedicationDAO medDAO = new MedicationDAOImpl();
        
        UserDAO userDAO = new UserDAOImpl();
        User user = userDAO.findByPhone("0712345678");
        
        if (user != null) {
            // Check if medication already exists
            List<Medication> existingMeds = medDAO.findByUser(user);
            boolean hasTestMed = false;
            
            for (Medication m : existingMeds) {
                if ("Test Med".equals(m.getName())) {
                    hasTestMed = true;
                    System.out.println("✓ Test medication already exists with ID: " + m.getId());
                    break;
                }
            }
            
            if (!hasTestMed) {
                Medication testMed = new Medication("Test Med", "1 tablet", "08:00", user);
                testMed.setInstructions("Take with food");
                Medication saved = medDAO.save(testMed);
                
                if (saved != null) {
                    System.out.println("✓ Medication created with ID: " + saved.getId());
                    
                    // Mark as taken
                    medDAO.markAsTaken(saved.getId());
                    System.out.println("✓ Medication marked as taken");
                } else {
                    System.out.println("✗ Failed to create medication");
                }
            }
            
            // Find by user
            List<Medication> meds = medDAO.findByUser(user);
            System.out.println("✓ Found " + meds.size() + " medications for user");
        } else {
            System.out.println("✗ User not found - skipping medication test");
        }
        System.out.println();
    }
    
    private static void testSymptomDAO() {
        System.out.println("--- Testing SymptomDAO ---");
        SymptomDAO symptomDAO = new SymptomDAOImpl();
        
        UserDAO userDAO = new UserDAOImpl();
        User user = userDAO.findByPhone("0712345678");
        
        if (user != null) {
            // Check if symptom already exists
            List<Symptom> existingSymptoms = symptomDAO.findByUser(user);
            boolean hasTestSymptom = false;
            
            for (Symptom s : existingSymptoms) {
                if ("Headache".equals(s.getSymptomName())) {
                    hasTestSymptom = true;
                    System.out.println("✓ Test symptom already exists with ID: " + s.getId());
                    break;
                }
            }
            
            if (!hasTestSymptom) {
                Symptom testSymptom = new Symptom("Headache", 3, "Mild headache in morning", user);
                Symptom saved = symptomDAO.save(testSymptom);
                
                if (saved != null) {
                    System.out.println("✓ Symptom created with ID: " + saved.getId());
                } else {
                    System.out.println("✗ Failed to create symptom");
                }
            }
            
            // Find by user
            List<Symptom> symptoms = symptomDAO.findByUser(user);
            System.out.println("✓ Found " + symptoms.size() + " symptoms for user");
            
            // Get average severity
            double avgSeverity = symptomDAO.getAverageSeverity(user.getId());
            System.out.println("✓ Average symptom severity: " + String.format("%.2f", avgSeverity));
        } else {
            System.out.println("✗ User not found - skipping symptom test");
        }
        System.out.println();
    }
    
    private static void testHelpRequestDAO() {
        System.out.println("--- Testing HelpRequestDAO ---");
        HelpRequestDAO requestDAO = new HelpRequestDAOImpl();
        
        UserDAO userDAO = new UserDAOImpl();
        User user = userDAO.findByPhone("0712345678");
        
        if (user != null) {
            // Check if help request already exists
            List<HelpRequest> existingRequests = requestDAO.findByPatient(user);
            boolean hasTestRequest = false;
            
            for (HelpRequest r : existingRequests) {
                if ("medication".equals(r.getRequestType()) && "Soweto".equals(r.getLocation())) {
                    hasTestRequest = true;
                    System.out.println("✓ Test help request already exists with ID: " + r.getId());
                    break;
                }
            }
            
            if (!hasTestRequest) {
                HelpRequest testRequest = new HelpRequest("medication", "Soweto", "Need ARV pickup", user);
                testRequest.setUrgent(true);
                HelpRequest saved = requestDAO.save(testRequest);
                
                if (saved != null) {
                    System.out.println("✓ Help request created with ID: " + saved.getId());
                } else {
                    System.out.println("✗ Failed to create help request");
                }
            }
            
            // Find open requests
            List<HelpRequest> openRequests = requestDAO.findOpenRequests();
            System.out.println("✓ Open requests count: " + openRequests.size());
            
            // Find urgent requests
            List<HelpRequest> urgent = requestDAO.findUrgentRequests();
            System.out.println("✓ Urgent requests count: " + urgent.size());
        } else {
            System.out.println("✗ User not found - skipping help request test");
        }
        System.out.println();
    }
    
    private static void testHealthLessonDAO() {
        System.out.println("--- Testing HealthLessonDAO ---");
        HealthLessonDAO lessonDAO = new HealthLessonDAOImpl();
        
        // Check if lesson already exists
        HealthLesson existingLesson = lessonDAO.findByLanguageAndKey("English", "hiv");
        
        if (existingLesson == null) {
            // Create test lesson only if doesn't exist
            HealthLesson testLesson = new HealthLesson(
                "hiv", 
                "Understanding HIV", 
                "HIV is manageable with daily medication (ARVs). Take your pills at the same time every day to stay healthy.", 
                "English"
            );
            testLesson.setActive(true);
            HealthLesson saved = lessonDAO.save(testLesson);
            
            if (saved != null) {
                System.out.println("✓ Health lesson created with ID: " + saved.getId());
            } else {
                System.out.println("✗ Failed to create health lesson");
            }
        } else {
            System.out.println("✓ Health lesson already exists (ID: " + existingLesson.getId() + ")");
            System.out.println("✓ Lesson title: " + existingLesson.getTitle());
        }
        
        // Find by language
        List<HealthLesson> englishLessons = lessonDAO.findByLanguage("English");
        System.out.println("✓ Found " + englishLessons.size() + " English lessons");
        
        // Display lesson titles
        for (HealthLesson lesson : englishLessons) {
            System.out.println("   - " + lesson.getIcon() + " " + lesson.getTitle());
        }
        
        // Get available languages
        List<String> languages = lessonDAO.getAvailableLanguages();
        System.out.println("✓ Available languages: " + languages);
        
        // Get lesson keys
        List<String> keys = lessonDAO.getLessonKeys();
        System.out.println("✓ Lesson keys: " + keys);
        
        System.out.println();
    }
}