/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.service;

import za.ac.tut.model.dao.HelpRequestDAO;
import za.ac.tut.model.dao.UserDAO;
import za.ac.tut.model.dao.impl.HelpRequestDAOImpl;
import za.ac.tut.model.dao.impl.UserDAOImpl;
import za.ac.tut.model.entity.HelpRequest;
import za.ac.tut.model.entity.User;
import java.util.List;

/**
 * Service class for HelpRequest business logic
 * 
 * @author USER
 */
public class HelpRequestService {
    
    private HelpRequestDAO helpRequestDAO;
    private UserDAO userDAO;
    
    public HelpRequestService() {
        this.helpRequestDAO = new HelpRequestDAOImpl();
        this.userDAO = new UserDAOImpl();
    }
    
    // ============ CREATE ============
    
    // Create new help request
    public HelpRequest createHelpRequest(Long patientId, String requestType, String location, 
                                         String details, boolean urgent) {
        User patient = userDAO.findById(patientId);
        if (patient == null) {
            return null;
        }
        
        HelpRequest request = new HelpRequest(requestType, location, details, urgent, patient);
        return helpRequestDAO.save(request);
    }
    
    // ============ UPDATE / ACTIONS ============
    
    // Assign volunteer to request
    public boolean assignVolunteer(Long requestId, Long volunteerId) {
        HelpRequest request = helpRequestDAO.findById(requestId);
        User volunteer = userDAO.findById(volunteerId);
        
        if (request != null && volunteer != null && "open".equals(request.getStatus())) {
            helpRequestDAO.assignVolunteer(requestId, volunteerId);
            return true;
        }
        return false;
    }
    
    // Complete request
    public boolean completeRequest(Long requestId) {
        HelpRequest request = helpRequestDAO.findById(requestId);
        if (request != null && "assigned".equals(request.getStatus())) {
            helpRequestDAO.completeRequest(requestId);
            return true;
        }
        return false;
    }
    
    // Cancel request
    public boolean cancelRequest(Long requestId) {
        HelpRequest request = helpRequestDAO.findById(requestId);
        if (request != null) {
            helpRequestDAO.cancelRequest(requestId);
            return true;
        }
        return false;
    }
    
    // ============ QUERY METHODS ============
    
    // Get all help requests (for admin)
    public List<HelpRequest> findAll() {
        return helpRequestDAO.findAll();
    }
    
    // Get request by ID
    public HelpRequest getHelpRequestById(Long requestId) {
        return helpRequestDAO.findById(requestId);
    }
    
    // Get patient's requests
    public List<HelpRequest> getPatientRequests(Long patientId) {
        return helpRequestDAO.findByPatientId(patientId);
    }
    
    // Get volunteer's assigned requests
    public List<HelpRequest> getVolunteerRequests(Long volunteerId) {
        return helpRequestDAO.findByVolunteerId(volunteerId);
    }
    
    // Get open requests (available for volunteers)
    public List<HelpRequest> getOpenRequests() {
        return helpRequestDAO.findOpenRequests();
    }
    
    // Get assigned requests
    public List<HelpRequest> getAssignedRequests() {
        return helpRequestDAO.findByStatus("assigned");
    }
    
    // Get completed requests
    public List<HelpRequest> getCompletedRequests() {
        return helpRequestDAO.findByStatus("completed");
    }
    
    // Get urgent requests
    public List<HelpRequest> getUrgentRequests() {
        return helpRequestDAO.findUrgentRequests();
    }
    
    // Get requests by status
    public List<HelpRequest> getRequestsByStatus(String status) {
        return helpRequestDAO.findByStatus(status);
    }
    
    // Get requests by location
    public List<HelpRequest> getRequestsByLocation(String location) {
        return helpRequestDAO.findByLocation(location);
    }
    
    // Get requests by type
    public List<HelpRequest> getRequestsByType(String requestType) {
        return helpRequestDAO.findByType(requestType);
    }
    
    // ============ STATISTICS ============
    
    // Get request statistics
    public long getOpenRequestsCount() {
        return helpRequestDAO.countByStatus("open");
    }
    
    public long getAssignedRequestsCount() {
        return helpRequestDAO.countByStatus("assigned");
    }
    
    public long getCompletedRequestsCount() {
        return helpRequestDAO.countByStatus("completed");
    }
    
    public long getCancelledRequestsCount() {
        return helpRequestDAO.countByStatus("cancelled");
    }
    
    public long getUrgentRequestsCount() {
        return getUrgentRequests().size();
    }
    
    public long getTotalRequestsCount() {
        return helpRequestDAO.count();
    }
    
    public double getAverageResponseTime() {
        return helpRequestDAO.getAverageResponseTime();
    }
    
    // Get completion rate percentage
    public int getCompletionRate() {
        long total = getTotalRequestsCount();
        long completed = getCompletedRequestsCount();
        if (total == 0) return 0;
        return (int) (completed * 100 / total);
    }
}