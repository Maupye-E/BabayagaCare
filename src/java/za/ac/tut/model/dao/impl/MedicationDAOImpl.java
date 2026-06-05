/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.dao.impl;

import za.ac.tut.model.dao.MedicationDAO;
import za.ac.tut.model.entity.Medication;
import za.ac.tut.model.entity.User;
import za.ac.tut.model.util.DBConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of MedicationDAO interface using JDBC
 * 
 * @author USER
 */
public class MedicationDAOImpl implements MedicationDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnectionUtil.getConnection();
    }
    
    @Override
    public Medication save(Medication medication) {
        String sql = "INSERT INTO MEDICATIONS (NAME, DOSAGE, TIME, INSTRUCTIONS, TAKEN, DATEADDED, USER_ID) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, medication.getName());
            pstmt.setString(2, medication.getDosage());
            pstmt.setString(3, medication.getTime());
            pstmt.setString(4, medication.getInstructions());
            pstmt.setBoolean(5, medication.isTaken());
            pstmt.setDate(6, new java.sql.Date(medication.getDateAdded().getTime()));
            pstmt.setLong(7, medication.getUser().getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        medication.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            System.out.println("Medication saved successfully: " + medication.getName());
            return medication;
            
        } catch (SQLException e) {
            System.err.println("Error saving medication: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Medication update(Medication medication) {
        String sql = "UPDATE MEDICATIONS SET NAME = ?, DOSAGE = ?, TIME = ?, "
                   + "INSTRUCTIONS = ?, TAKEN = ? WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, medication.getName());
            pstmt.setString(2, medication.getDosage());
            pstmt.setString(3, medication.getTime());
            pstmt.setString(4, medication.getInstructions());
            pstmt.setBoolean(5, medication.isTaken());
            pstmt.setLong(6, medication.getId());
            
            pstmt.executeUpdate();
            
            System.out.println("Medication updated successfully: " + medication.getName());
            return medication;
            
        } catch (SQLException e) {
            System.err.println("Error updating medication: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Medication findById(Long id) {
        String sql = "SELECT * FROM MEDICATIONS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMedication(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding medication by ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Medication> findAll() {
        List<Medication> medications = new ArrayList<>();
        String sql = "SELECT * FROM MEDICATIONS ORDER BY ID";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                medications.add(mapResultSetToMedication(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all medications: " + e.getMessage());
        }
        return medications;
    }
    
    @Override
    public void delete(Medication medication) {
        deleteById(medication.getId());
    }
    
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM MEDICATIONS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            
            System.out.println("Medication deleted with ID: " + id);
            
        } catch (SQLException e) {
            System.err.println("Error deleting medication by ID: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM MEDICATIONS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking medication existence: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM MEDICATIONS";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting medications: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public List<Medication> findByUser(User user) {
        return findByUserId(user.getId());
    }
    
    @Override
    public List<Medication> findByUserId(Long userId) {
        List<Medication> medications = new ArrayList<>();
        String sql = "SELECT * FROM MEDICATIONS WHERE USER_ID = ? ORDER BY TIME";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    medications.add(mapResultSetToMedication(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding medications by user ID: " + e.getMessage());
        }
        return medications;
    }
    
    @Override
    public List<Medication> findPendingMedications(Long userId) {
        List<Medication> medications = new ArrayList<>();
        String sql = "SELECT * FROM MEDICATIONS WHERE USER_ID = ? AND TAKEN = FALSE ORDER BY TIME";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    medications.add(mapResultSetToMedication(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding pending medications: " + e.getMessage());
        }
        return medications;
    }
    
    @Override
    public List<Medication> findMedicationsByTimeRange(String startTime, String endTime) {
        List<Medication> medications = new ArrayList<>();
        String sql = "SELECT * FROM MEDICATIONS WHERE TIME BETWEEN ? AND ? ORDER BY TIME";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, startTime);
            pstmt.setString(2, endTime);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    medications.add(mapResultSetToMedication(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding medications by time range: " + e.getMessage());
        }
        return medications;
    }
    
    @Override
    public void markAsTaken(Long medicationId) {
        String sql = "UPDATE MEDICATIONS SET TAKEN = TRUE WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, medicationId);
            pstmt.executeUpdate();
            
            System.out.println("Medication marked as taken: " + medicationId);
            
        } catch (SQLException e) {
            System.err.println("Error marking medication as taken: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Medication> getTodayMedications(Long userId) {
        // For now, return pending medications
        // You can enhance this to check dates if you add a DATE column
        return findPendingMedications(userId);
    }
    
    @Override
    public double getAdherenceRate(Long userId) {
        String sql = "SELECT COUNT(*), SUM(CASE WHEN TAKEN = TRUE THEN 1 ELSE 0 END) "
                   + "FROM MEDICATIONS WHERE USER_ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    long total = rs.getLong(1);
                    long taken = rs.getLong(2);
                    
                    if (total == 0) return 0.0;
                    return (taken * 100.0) / total;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error calculating adherence rate: " + e.getMessage());
        }
        return 0.0;
    }
    
    // ============ HELPER METHOD ============
    
    private Medication mapResultSetToMedication(ResultSet rs) throws SQLException {
        Medication medication = new Medication();
        medication.setId(rs.getLong("ID"));
        medication.setName(rs.getString("NAME"));
        medication.setDosage(rs.getString("DOSAGE"));
        medication.setTime(rs.getString("TIME"));
        medication.setInstructions(rs.getString("INSTRUCTIONS"));
        medication.setTaken(rs.getBoolean("TAKEN"));
        medication.setDateAdded(rs.getDate("DATEADDED"));
        
        // Set user (only ID, will need to fetch full user separately if needed)
        User user = new User();
        user.setId(rs.getLong("USER_ID"));
        medication.setUser(user);
        
        return medication;
    }
}