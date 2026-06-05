/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.dao.impl;

import za.ac.tut.model.dao.UserDAO;
import za.ac.tut.model.entity.User;
import za.ac.tut.model.util.DBConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of UserDAO interface using JDBC
 * 
 * @author USER
 */
public class UserDAOImpl implements UserDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnectionUtil.getConnection();
    }
    
    @Override
    public User save(User user) {
        String sql = "INSERT INTO USERS (NAME, PHONE, LANGUAGE, PASSWORD, ROLE, LOCATION, REGISTRATIONDATE, LASTLOGINDATE, ACTIVE) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPhone());
            pstmt.setString(3, user.getLanguage());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getRole());
            pstmt.setString(6, user.getLocation());
            pstmt.setDate(7, new java.sql.Date(user.getRegistrationDate().getTime()));
            pstmt.setTimestamp(8, user.getLastLoginDate() != null ? new java.sql.Timestamp(user.getLastLoginDate().getTime()) : null);
            pstmt.setBoolean(9, user.isActive());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            System.out.println("User saved successfully: " + user.getName());
            return user;
            
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public User update(User user) {
        String sql = "UPDATE USERS SET NAME = ?, PHONE = ?, LANGUAGE = ?, PASSWORD = ?, "
                   + "ROLE = ?, LOCATION = ?, LASTLOGINDATE = ?, ACTIVE = ? WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPhone());
            pstmt.setString(3, user.getLanguage());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getRole());
            pstmt.setString(6, user.getLocation());
            pstmt.setTimestamp(7, user.getLastLoginDate() != null ? new java.sql.Timestamp(user.getLastLoginDate().getTime()) : null);
            pstmt.setBoolean(8, user.isActive());
            pstmt.setLong(9, user.getId());
            
            pstmt.executeUpdate();
            
            System.out.println("User updated successfully: " + user.getName());
            return user;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM USERS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USERS ORDER BY ID";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all users: " + e.getMessage());
        }
        return users;
    }
    
    @Override
    public void delete(User user) {
        deleteById(user.getId());
    }
    
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM USERS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            
            System.out.println("User deleted successfully with ID: " + id);
            
        } catch (SQLException e) {
            System.err.println("Error deleting user by ID: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM USERS";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting users: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public User findByPhone(String phone) {
        String sql = "SELECT * FROM USERS WHERE PHONE = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, phone);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding user by phone: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<User> findByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USERS WHERE ROLE = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, role);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding users by role: " + e.getMessage());
        }
        return users;
    }
    
    @Override
    public List<User> findActiveUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USERS WHERE ACTIVE = TRUE";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding active users: " + e.getMessage());
        }
        return users;
    }
    
    @Override
    public List<User> findByLocation(String location) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USERS WHERE LOCATION LIKE ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + location + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding users by location: " + e.getMessage());
        }
        return users;
    }
    
    @Override
    public List<User> findAvailableVolunteers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USERS WHERE ROLE = 'volunteer' AND ACTIVE = TRUE";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding available volunteers: " + e.getMessage());
        }
        return users;
    }
    
    @Override
    public void updateLastLoginDate(Long userId) {
        String sql = "UPDATE USERS SET LASTLOGINDATE = ? WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setLong(2, userId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error updating last login date: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public List<User> searchByName(String namePattern) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USERS WHERE NAME LIKE ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + namePattern + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching users by name: " + e.getMessage());
        }
        return users;
    }
    
    @Override
    public User authenticate(String phone, String password) {
        String sql = "SELECT * FROM USERS WHERE PHONE = ? AND PASSWORD = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, phone);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public boolean isPhoneExists(String phone) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE PHONE = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, phone);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking phone existence: " + e.getMessage());
        }
        return false;
    }
    
    // ============ HELPER METHOD ============
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("ID"));
        user.setName(rs.getString("NAME"));
        user.setPhone(rs.getString("PHONE"));
        user.setLanguage(rs.getString("LANGUAGE"));
        user.setPassword(rs.getString("PASSWORD"));
        user.setRole(rs.getString("ROLE"));
        user.setLocation(rs.getString("LOCATION"));
        user.setRegistrationDate(rs.getDate("REGISTRATIONDATE"));
        user.setLastLoginDate(rs.getTimestamp("LASTLOGINDATE"));
        user.setActive(rs.getBoolean("ACTIVE"));
        return user;
    }
    
    
    @Override
public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
    // First verify old password
    String checkSql = "SELECT COUNT(*) FROM USERS WHERE ID = ? AND PASSWORD = ?";
    
    try (Connection conn = getConnection();
         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
        
        checkStmt.setLong(1, userId);
        checkStmt.setString(2, oldPassword);
        
        try (ResultSet rs = checkStmt.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
                // Old password matches, update to new password
                String updateSql = "UPDATE USERS SET PASSWORD = ? WHERE ID = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, newPassword);
                    updateStmt.setLong(2, userId);
                    int rowsUpdated = updateStmt.executeUpdate();
                    return rowsUpdated > 0;
                }
            }
        }
        
    } catch (SQLException e) {
        System.err.println("Error updating password: " + e.getMessage());
    }
    return false;
}

@Override
public boolean resetPassword(Long userId, String newPassword) {
    String sql = "UPDATE USERS SET PASSWORD = ? WHERE ID = ?";
    
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, newPassword);
        pstmt.setLong(2, userId);
        int rowsUpdated = pstmt.executeUpdate();
        return rowsUpdated > 0;
        
    } catch (SQLException e) {
        System.err.println("Error resetting password: " + e.getMessage());
        return false;
    }
}
}