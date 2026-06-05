/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.dao.impl;

import za.ac.tut.model.dao.HealthLessonDAO;
import za.ac.tut.model.entity.HealthLesson;
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
 * Implementation of HealthLessonDAO interface using JDBC
 * 
 * @author USER
 */
public class HealthLessonDAOImpl implements HealthLessonDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnectionUtil.getConnection();
    }
    
    @Override
    public HealthLesson save(HealthLesson lesson) {
        String sql = "INSERT INTO HEALTHLESSONS (LESSONKEY, TITLE, CONTENT, LANGUAGE, IMAGEURL, CREATEDDATE, ACTIVE) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, lesson.getLessonKey());
            pstmt.setString(2, lesson.getTitle());
            pstmt.setString(3, lesson.getContent());
            pstmt.setString(4, lesson.getLanguage());
            pstmt.setString(5, lesson.getImageUrl());
            pstmt.setTimestamp(6, new Timestamp(lesson.getCreatedDate().getTime()));
            pstmt.setBoolean(7, lesson.isActive());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        lesson.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            System.out.println("Health lesson saved successfully: " + lesson.getTitle());
            return lesson;
            
        } catch (SQLException e) {
            System.err.println("Error saving health lesson: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public HealthLesson update(HealthLesson lesson) {
        String sql = "UPDATE HEALTHLESSONS SET LESSONKEY = ?, TITLE = ?, CONTENT = ?, "
                   + "LANGUAGE = ?, IMAGEURL = ?, ACTIVE = ? WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, lesson.getLessonKey());
            pstmt.setString(2, lesson.getTitle());
            pstmt.setString(3, lesson.getContent());
            pstmt.setString(4, lesson.getLanguage());
            pstmt.setString(5, lesson.getImageUrl());
            pstmt.setBoolean(6, lesson.isActive());
            pstmt.setLong(7, lesson.getId());
            
            pstmt.executeUpdate();
            
            System.out.println("Health lesson updated successfully: " + lesson.getTitle());
            return lesson;
            
        } catch (SQLException e) {
            System.err.println("Error updating health lesson: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public HealthLesson findById(Long id) {
        String sql = "SELECT * FROM HEALTHLESSONS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHealthLesson(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding health lesson by ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<HealthLesson> findAll() {
        List<HealthLesson> lessons = new ArrayList<>();
        String sql = "SELECT * FROM HEALTHLESSONS ORDER BY LESSONKEY, LANGUAGE";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lessons.add(mapResultSetToHealthLesson(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all health lessons: " + e.getMessage());
        }
        return lessons;
    }
    
    @Override
    public void delete(HealthLesson lesson) {
        deleteById(lesson.getId());
    }
    
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM HEALTHLESSONS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            
            System.out.println("Health lesson deleted with ID: " + id);
            
        } catch (SQLException e) {
            System.err.println("Error deleting health lesson by ID: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM HEALTHLESSONS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking health lesson existence: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM HEALTHLESSONS";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting health lessons: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public List<HealthLesson> findByLanguage(String language) {
        List<HealthLesson> lessons = new ArrayList<>();
        String sql = "SELECT * FROM HEALTHLESSONS WHERE LANGUAGE = ? AND ACTIVE = TRUE ORDER BY LESSONKEY";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, language);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lessons.add(mapResultSetToHealthLesson(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding lessons by language: " + e.getMessage());
        }
        return lessons;
    }
    
    @Override
    public List<HealthLesson> findByLessonKey(String lessonKey) {
        List<HealthLesson> lessons = new ArrayList<>();
        String sql = "SELECT * FROM HEALTHLESSONS WHERE LESSONKEY = ? AND ACTIVE = TRUE";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, lessonKey);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lessons.add(mapResultSetToHealthLesson(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding lessons by key: " + e.getMessage());
        }
        return lessons;
    }
    
    @Override
    public List<HealthLesson> findActiveLessons() {
        List<HealthLesson> lessons = new ArrayList<>();
        String sql = "SELECT * FROM HEALTHLESSONS WHERE ACTIVE = TRUE ORDER BY LESSONKEY";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lessons.add(mapResultSetToHealthLesson(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding active lessons: " + e.getMessage());
        }
        return lessons;
    }
    
    @Override
    public HealthLesson findByLanguageAndKey(String language, String lessonKey) {
        String sql = "SELECT * FROM HEALTHLESSONS WHERE LANGUAGE = ? AND LESSONKEY = ? AND ACTIVE = TRUE";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, language);
            pstmt.setString(2, lessonKey);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHealthLesson(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding lesson by language and key: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<String> getAvailableLanguages() {
        List<String> languages = new ArrayList<>();
        String sql = "SELECT DISTINCT LANGUAGE FROM HEALTHLESSONS WHERE ACTIVE = TRUE ORDER BY LANGUAGE";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                languages.add(rs.getString("LANGUAGE"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting available languages: " + e.getMessage());
        }
        return languages;
    }
    
    @Override
    public List<String> getLessonKeys() {
        List<String> keys = new ArrayList<>();
        String sql = "SELECT DISTINCT LESSONKEY FROM HEALTHLESSONS WHERE ACTIVE = TRUE ORDER BY LESSONKEY";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                keys.add(rs.getString("LESSONKEY"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting lesson keys: " + e.getMessage());
        }
        return keys;
    }
    
    @Override
    public long countByLanguage(String language) {
        String sql = "SELECT COUNT(*) FROM HEALTHLESSONS WHERE LANGUAGE = ? AND ACTIVE = TRUE";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, language);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting lessons by language: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public void deactivateLesson(Long lessonId) {
        String sql = "UPDATE HEALTHLESSONS SET ACTIVE = FALSE WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, lessonId);
            pstmt.executeUpdate();
            
            System.out.println("Lesson deactivated: " + lessonId);
            
        } catch (SQLException e) {
            System.err.println("Error deactivating lesson: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void activateLesson(Long lessonId) {
        String sql = "UPDATE HEALTHLESSONS SET ACTIVE = TRUE WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, lessonId);
            pstmt.executeUpdate();
            
            System.out.println("Lesson activated: " + lessonId);
            
        } catch (SQLException e) {
            System.err.println("Error activating lesson: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public List<HealthLesson> searchLessons(String searchTerm) {
        List<HealthLesson> lessons = new ArrayList<>();
        String sql = "SELECT * FROM HEALTHLESSONS WHERE (LOWER(TITLE) LIKE LOWER(?) OR LOWER(CONTENT) LIKE LOWER(?)) AND ACTIVE = TRUE";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lessons.add(mapResultSetToHealthLesson(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching lessons: " + e.getMessage());
        }
        return lessons;
    }
    
    // ============ HELPER METHOD ============
    
    private HealthLesson mapResultSetToHealthLesson(ResultSet rs) throws SQLException {
        HealthLesson lesson = new HealthLesson();
        lesson.setId(rs.getLong("ID"));
        lesson.setLessonKey(rs.getString("LESSONKEY"));
        lesson.setTitle(rs.getString("TITLE"));
        lesson.setContent(rs.getString("CONTENT"));
        lesson.setLanguage(rs.getString("LANGUAGE"));
        lesson.setImageUrl(rs.getString("IMAGEURL"));
        lesson.setCreatedDate(rs.getTimestamp("CREATEDDATE"));
        lesson.setActive(rs.getBoolean("ACTIVE"));
        return lesson;
    }
}