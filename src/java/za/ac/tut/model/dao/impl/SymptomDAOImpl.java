/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.dao.impl;

import za.ac.tut.model.dao.SymptomDAO;
import za.ac.tut.model.entity.Symptom;
import za.ac.tut.model.entity.User;
import za.ac.tut.model.util.DBConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Implementation of SymptomDAO interface using JDBC
 * 
 * @author USER
 */
public class SymptomDAOImpl implements SymptomDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnectionUtil.getConnection();
    }
    
    @Override
    public Symptom save(Symptom symptom) {
        String sql = "INSERT INTO SYMPTOMS (SYMPTOMNAME, SEVERITY, NOTES, SYMPTOMDATE, SYMPTOMTIME, LOGGEDAT, USER_ID) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, symptom.getSymptomName());
            pstmt.setInt(2, symptom.getSeverity());
            pstmt.setString(3, symptom.getNotes());
            pstmt.setDate(4, new java.sql.Date(symptom.getSymptomDate().getTime()));
            pstmt.setTime(5, new java.sql.Time(symptom.getSymptomTime().getTime()));
            pstmt.setTimestamp(6, new java.sql.Timestamp(symptom.getLoggedAt().getTime()));
            pstmt.setLong(7, symptom.getUser().getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        symptom.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            System.out.println("Symptom saved successfully: " + symptom.getSymptomName());
            return symptom;
            
        } catch (SQLException e) {
            System.err.println("Error saving symptom: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Symptom update(Symptom symptom) {
        String sql = "UPDATE SYMPTOMS SET SYMPTOMNAME = ?, SEVERITY = ?, NOTES = ?, "
                   + "SYMPTOMDATE = ?, SYMPTOMTIME = ? WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, symptom.getSymptomName());
            pstmt.setInt(2, symptom.getSeverity());
            pstmt.setString(3, symptom.getNotes());
            pstmt.setDate(4, new java.sql.Date(symptom.getSymptomDate().getTime()));
            pstmt.setTime(5, new java.sql.Time(symptom.getSymptomTime().getTime()));
            pstmt.setLong(6, symptom.getId());
            
            pstmt.executeUpdate();
            
            System.out.println("Symptom updated successfully: " + symptom.getSymptomName());
            return symptom;
            
        } catch (SQLException e) {
            System.err.println("Error updating symptom: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Symptom findById(Long id) {
        String sql = "SELECT * FROM SYMPTOMS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSymptom(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding symptom by ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Symptom> findAll() {
        List<Symptom> symptoms = new ArrayList<>();
        String sql = "SELECT * FROM SYMPTOMS ORDER BY LOGGEDAT DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                symptoms.add(mapResultSetToSymptom(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all symptoms: " + e.getMessage());
        }
        return symptoms;
    }
    
    @Override
    public void delete(Symptom symptom) {
        deleteById(symptom.getId());
    }
    
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM SYMPTOMS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            
            System.out.println("Symptom deleted with ID: " + id);
            
        } catch (SQLException e) {
            System.err.println("Error deleting symptom by ID: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM SYMPTOMS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking symptom existence: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM SYMPTOMS";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting symptoms: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public List<Symptom> findByUser(User user) {
        return findByUserId(user.getId());
    }
    
    @Override
    public List<Symptom> findByUserId(Long userId) {
        List<Symptom> symptoms = new ArrayList<>();
        String sql = "SELECT * FROM SYMPTOMS WHERE USER_ID = ? ORDER BY LOGGEDAT DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    symptoms.add(mapResultSetToSymptom(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding symptoms by user ID: " + e.getMessage());
        }
        return symptoms;
    }
    
    @Override
    public List<Symptom> findByDateRange(Long userId, Date startDate, Date endDate) {
        List<Symptom> symptoms = new ArrayList<>();
        String sql = "SELECT * FROM SYMPTOMS WHERE USER_ID = ? AND SYMPTOMDATE BETWEEN ? AND ? ORDER BY SYMPTOMDATE DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(3, new java.sql.Date(endDate.getTime()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    symptoms.add(mapResultSetToSymptom(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding symptoms by date range: " + e.getMessage());
        }
        return symptoms;
    }
    
    @Override
    public List<Symptom> findBySeverity(Integer severity) {
        List<Symptom> symptoms = new ArrayList<>();
        String sql = "SELECT * FROM SYMPTOMS WHERE SEVERITY = ? ORDER BY LOGGEDAT DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, severity);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    symptoms.add(mapResultSetToSymptom(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding symptoms by severity: " + e.getMessage());
        }
        return symptoms;
    }
    
    @Override
    public List<Symptom> findRecentSymptoms(Long userId, int days) {
        List<Symptom> symptoms = new ArrayList<>();
        String sql = "SELECT * FROM SYMPTOMS WHERE USER_ID = ? AND LOGGEDAT >= CURRENT_DATE - ? DAYS ORDER BY LOGGEDAT DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            pstmt.setInt(2, days);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    symptoms.add(mapResultSetToSymptom(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding recent symptoms: " + e.getMessage());
        }
        return symptoms;
    }
    
    @Override
    public List<Symptom> getTodaySymptoms(Long userId) {
        List<Symptom> symptoms = new ArrayList<>();
        String sql = "SELECT * FROM SYMPTOMS WHERE USER_ID = ? AND DATE(LOGGEDAT) = CURRENT_DATE ORDER BY LOGGEDAT DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    symptoms.add(mapResultSetToSymptom(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting today's symptoms: " + e.getMessage());
        }
        return symptoms;
    }
    
    @Override
    public List<Symptom> findBySymptomName(Long userId, String symptomName) {
        List<Symptom> symptoms = new ArrayList<>();
        String sql = "SELECT * FROM SYMPTOMS WHERE USER_ID = ? AND LOWER(SYMPTOMNAME) LIKE LOWER(?) ORDER BY LOGGEDAT DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            pstmt.setString(2, "%" + symptomName + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    symptoms.add(mapResultSetToSymptom(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding symptoms by name: " + e.getMessage());
        }
        return symptoms;
    }
    
    @Override
    public double getAverageSeverity(Long userId) {
        String sql = "SELECT AVG(SEVERITY) FROM SYMPTOMS WHERE USER_ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error calculating average severity: " + e.getMessage());
        }
        return 0.0;
    }
    
    @Override
    public List<Object[]> getMostCommonSymptoms(Long userId, int limit) {
        List<Object[]> results = new ArrayList<>();
        String sql = "SELECT SYMPTOMNAME, COUNT(*) as COUNT FROM SYMPTOMS "
                   + "WHERE USER_ID = ? GROUP BY SYMPTOMNAME ORDER BY COUNT DESC FETCH FIRST ? ROWS ONLY";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            pstmt.setInt(2, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[2];
                    row[0] = rs.getString("SYMPTOMNAME");
                    row[1] = rs.getLong("COUNT");
                    results.add(row);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting most common symptoms: " + e.getMessage());
        }
        return results;
    }
    
    @Override
    public List<Object[]> getSymptomTrends(Long userId, int days) {
        List<Object[]> results = new ArrayList<>();
        String sql = "SELECT SYMPTOMDATE, AVG(SEVERITY) FROM SYMPTOMS "
                   + "WHERE USER_ID = ? AND LOGGEDAT >= CURRENT_DATE - ? DAYS "
                   + "GROUP BY SYMPTOMDATE ORDER BY SYMPTOMDATE ASC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            pstmt.setInt(2, days);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[2];
                    row[0] = rs.getDate("SYMPTOMDATE");
                    row[1] = rs.getDouble(2);
                    results.add(row);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting symptom trends: " + e.getMessage());
        }
        return results;
    }
    
    @Override
    public int deleteOldSymptoms(int days) {
        String sql = "DELETE FROM SYMPTOMS WHERE LOGGEDAT < CURRENT_DATE - ? DAYS";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, days);
            int deletedCount = pstmt.executeUpdate();
            
            System.out.println("Deleted " + deletedCount + " old symptoms");
            return deletedCount;
            
        } catch (SQLException e) {
            System.err.println("Error deleting old symptoms: " + e.getMessage());
            return 0;
        }
    }
    
    // ============ HELPER METHOD ============
    
    private Symptom mapResultSetToSymptom(ResultSet rs) throws SQLException {
        Symptom symptom = new Symptom();
        symptom.setId(rs.getLong("ID"));
        symptom.setSymptomName(rs.getString("SYMPTOMNAME"));
        symptom.setSeverity(rs.getInt("SEVERITY"));
        symptom.setNotes(rs.getString("NOTES"));
        symptom.setSymptomDate(rs.getDate("SYMPTOMDATE"));
        symptom.setSymptomTime(rs.getTime("SYMPTOMTIME"));
        symptom.setLoggedAt(rs.getTimestamp("LOGGEDAT"));
        
        // Set user (only ID, will need to fetch full user separately if needed)
        User user = new User();
        user.setId(rs.getLong("USER_ID"));
        symptom.setUser(user);
        
        return symptom;
    }
}