/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.dao;

import za.ac.tut.model.entity.HelpRequest;
import za.ac.tut.model.entity.User;
import java.util.List;

/**
 * HelpRequest specific DAO operations
 * 
 * @author USER
 */
public interface HelpRequestDAO extends GenericDAO<HelpRequest, Long> {
    
    // Find requests by patient
    List<HelpRequest> findByPatient(User patient);
    
    // Find requests by patient ID
    List<HelpRequest> findByPatientId(Long patientId);
    
    // Find requests by volunteer
    List<HelpRequest> findByVolunteer(User volunteer);
    
    // Find requests by volunteer ID
    List<HelpRequest> findByVolunteerId(Long volunteerId);
    
    // Find requests by status (open, assigned, completed, cancelled)
    List<HelpRequest> findByStatus(String status);
    
    // Find open requests (not yet assigned)
    List<HelpRequest> findOpenRequests();
    
    // Find urgent requests
    List<HelpRequest> findUrgentRequests();
    
    // Find requests by location
    List<HelpRequest> findByLocation(String location);
    
    // Find requests by type (medication, clinic, food, care)
    List<HelpRequest> findByType(String requestType);
    
    // Assign volunteer to request
    void assignVolunteer(Long requestId, Long volunteerId);
    
    // Complete request
    void completeRequest(Long requestId);
    
    // Cancel request
    void cancelRequest(Long requestId);
    
    // Get requests for volunteer dashboard
    List<HelpRequest> getVolunteerDashboardRequests(Long volunteerId);
    
    // Get requests for patient dashboard
    List<HelpRequest> getPatientDashboardRequests(Long patientId);
    
    // Count requests by status
    long countByStatus(String status);
    
    // Get average response time (in hours)
    double getAverageResponseTime();
}