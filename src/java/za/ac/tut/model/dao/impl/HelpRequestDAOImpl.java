/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.dao.impl;

import za.ac.tut.model.dao.HelpRequestDAO;
import za.ac.tut.model.entity.HelpRequest;
import za.ac.tut.model.entity.User;
import za.ac.tut.model.util.DBConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of HelpRequestDAO interface using JDBC
 * 
 * @author USER
 */
public class HelpRequestDAOImpl implements HelpRequestDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnectionUtil.getConnection();
    }
    
    @Override
    public HelpRequest save(HelpRequest request) {
        String sql = "INSERT INTO HELPREQUESTS (REQUESTTYPE, LOCATION, DETAILS, URGENT, STATUS, "
                   + "REQUESTDATE, PATIENT_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, request.getRequestType());
            pstmt.setString(2, request.getLocation());
            pstmt.setString(3, request.getDetails());
            pstmt.setBoolean(4, request.isUrgent());
            pstmt.setString(5, request.getStatus());
            pstmt.setTimestamp(6, new Timestamp(request.getRequestDate().getTime()));
            pstmt.setLong(7, request.getPatient().getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        request.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            System.out.println("Help request saved successfully: " + request.getId());
            return request;
            
        } catch (SQLException e) {
            System.err.println("Error saving help request: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public HelpRequest update(HelpRequest request) {
        String sql = "UPDATE HELPREQUESTS SET REQUESTTYPE = ?, LOCATION = ?, DETAILS = ?, "
                   + "URGENT = ?, STATUS = ?, ASSIGNEDDATE = ?, COMPLETEDDATE = ?, "
                   + "VOLUNTEER_ID = ? WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, request.getRequestType());
            pstmt.setString(2, request.getLocation());
            pstmt.setString(3, request.getDetails());
            pstmt.setBoolean(4, request.isUrgent());
            pstmt.setString(5, request.getStatus());
            pstmt.setTimestamp(6, request.getAssignedDate() != null ? new Timestamp(request.getAssignedDate().getTime()) : null);
            pstmt.setTimestamp(7, request.getCompletedDate() != null ? new Timestamp(request.getCompletedDate().getTime()) : null);
            pstmt.setLong(8, request.getVolunteer() != null ? request.getVolunteer().getId() : null);
            pstmt.setLong(9, request.getId());
            
            pstmt.executeUpdate();
            
            System.out.println("Help request updated successfully: " + request.getId());
            return request;
            
        } catch (SQLException e) {
            System.err.println("Error updating help request: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public HelpRequest findById(Long id) {
        String sql = "SELECT * FROM HELPREQUESTS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHelpRequest(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding help request by ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<HelpRequest> findAll() {
        List<HelpRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM HELPREQUESTS ORDER BY REQUESTDATE DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                requests.add(mapResultSetToHelpRequest(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all help requests: " + e.getMessage());
        }
        return requests;
    }
    
    @Override
    public void delete(HelpRequest request) {
        deleteById(request.getId());
    }
    
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM HELPREQUESTS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            
            System.out.println("Help request deleted with ID: " + id);
            
        } catch (SQLException e) {
            System.err.println("Error deleting help request by ID: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM HELPREQUESTS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking help request existence: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM HELPREQUESTS";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting help requests: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public List<HelpRequest> findByPatient(User patient) {
        return findByPatientId(patient.getId());
    }
    
    @Override
    public List<HelpRequest> findByPatientId(Long patientId) {
        List<HelpRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM HELPREQUESTS WHERE PATIENT_ID = ? ORDER BY REQUESTDATE DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, patientId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToHelpRequest(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding requests by patient ID: " + e.getMessage());
        }
        return requests;
    }
    
    @Override
    public List<HelpRequest> findByVolunteer(User volunteer) {
        return findByVolunteerId(volunteer.getId());
    }
    
    @Override
    public List<HelpRequest> findByVolunteerId(Long volunteerId) {
        List<HelpRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM HELPREQUESTS WHERE VOLUNTEER_ID = ? ORDER BY REQUESTDATE DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, volunteerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToHelpRequest(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding requests by volunteer ID: " + e.getMessage());
        }
        return requests;
    }
    
    @Override
    public List<HelpRequest> findByStatus(String status) {
        List<HelpRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM HELPREQUESTS WHERE STATUS = ? ORDER BY REQUESTDATE DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToHelpRequest(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding requests by status: " + e.getMessage());
        }
        return requests;
    }
    
    @Override
    public List<HelpRequest> findOpenRequests() {
        return findByStatus("open");
    }
    
    @Override
    public List<HelpRequest> findUrgentRequests() {
        List<HelpRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM HELPREQUESTS WHERE URGENT = TRUE AND STATUS = 'open' ORDER BY REQUESTDATE DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                requests.add(mapResultSetToHelpRequest(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding urgent requests: " + e.getMessage());
        }
        return requests;
    }
    
    @Override
    public List<HelpRequest> findByLocation(String location) {
        List<HelpRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM HELPREQUESTS WHERE LOCATION LIKE ? ORDER BY REQUESTDATE DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + location + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToHelpRequest(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding requests by location: " + e.getMessage());
        }
        return requests;
    }
    
    @Override
    public List<HelpRequest> findByType(String requestType) {
        List<HelpRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM HELPREQUESTS WHERE REQUESTTYPE = ? ORDER BY REQUESTDATE DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, requestType);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToHelpRequest(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding requests by type: " + e.getMessage());
        }
        return requests;
    }
    
    @Override
    public void assignVolunteer(Long requestId, Long volunteerId) {
        String sql = "UPDATE HELPREQUESTS SET VOLUNTEER_ID = ?, STATUS = 'assigned', ASSIGNEDDATE = ? WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, volunteerId);
            pstmt.setTimestamp(2, new Timestamp(new Date().getTime()));
            pstmt.setLong(3, requestId);
            pstmt.executeUpdate();
            
            System.out.println("Volunteer assigned to request: " + requestId);
            
        } catch (SQLException e) {
            System.err.println("Error assigning volunteer: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void completeRequest(Long requestId) {
        String sql = "UPDATE HELPREQUESTS SET STATUS = 'completed', COMPLETEDDATE = ? WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            pstmt.setLong(2, requestId);
            pstmt.executeUpdate();
            
            System.out.println("Request completed: " + requestId);
            
        } catch (SQLException e) {
            System.err.println("Error completing request: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void cancelRequest(Long requestId) {
        String sql = "UPDATE HELPREQUESTS SET STATUS = 'cancelled' WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, requestId);
            pstmt.executeUpdate();
            
            System.out.println("Request cancelled: " + requestId);
            
        } catch (SQLException e) {
            System.err.println("Error cancelling request: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public List<HelpRequest> getVolunteerDashboardRequests(Long volunteerId) {
        List<HelpRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM HELPREQUESTS WHERE VOLUNTEER_ID = ? AND STATUS IN ('assigned', 'open') "
                   + "ORDER BY URGENT DESC, REQUESTDATE ASC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, volunteerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToHelpRequest(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting volunteer dashboard: " + e.getMessage());
        }
        return requests;
    }
    
    @Override
    public List<HelpRequest> getPatientDashboardRequests(Long patientId) {
        return findByPatientId(patientId);
    }
    
    @Override
    public long countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM HELPREQUESTS WHERE STATUS = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting by status: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public double getAverageResponseTime() {
        String sql = "SELECT AVG(TIMESTAMPDIFF(SQL_TSI_HOUR, REQUESTDATE, ASSIGNEDDATE)) "
                   + "FROM HELPREQUESTS WHERE ASSIGNEDDATE IS NOT NULL";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error calculating average response time: " + e.getMessage());
        }
        return 0.0;
    }
    
    // ============ HELPER METHOD ============
    
    private HelpRequest mapResultSetToHelpRequest(ResultSet rs) throws SQLException {
        HelpRequest request = new HelpRequest();
        request.setId(rs.getLong("ID"));
        request.setRequestType(rs.getString("REQUESTTYPE"));
        request.setLocation(rs.getString("LOCATION"));
        request.setDetails(rs.getString("DETAILS"));
        request.setUrgent(rs.getBoolean("URGENT"));
        request.setStatus(rs.getString("STATUS"));
        request.setRequestDate(rs.getTimestamp("REQUESTDATE"));
        request.setAssignedDate(rs.getTimestamp("ASSIGNEDDATE"));
        request.setCompletedDate(rs.getTimestamp("COMPLETEDDATE"));
        
        // Set patient (only ID)
        User patient = new User();
        patient.setId(rs.getLong("PATIENT_ID"));
        request.setPatient(patient);
        
        // Set volunteer (only ID, may be null)
        Long volunteerId = rs.getLong("VOLUNTEER_ID");
        if (!rs.wasNull()) {
            User volunteer = new User();
            volunteer.setId(volunteerId);
            request.setVolunteer(volunteer);
        }
        
        return request;
    }
}