/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.dao;

import za.ac.tut.model.entity.User;
import java.util.List;

/**
 * User specific DAO operations
 * 
 * @author USER
 */
public interface UserDAO extends GenericDAO<User, Long> {
    
    // Find user by phone number (unique)
    User findByPhone(String phone);
    
    // Find users by role (patient, volunteer, admin)
    List<User> findByRole(String role);
    
    // Find active users
    List<User> findActiveUsers();
    
    // Find users by location (township/suburb)
    List<User> findByLocation(String location);
    
    // Find volunteers available for help
    List<User> findAvailableVolunteers();
    
    // Update last login date
    void updateLastLoginDate(Long userId);
    
    // Search users by name
    List<User> searchByName(String namePattern);
    
    // Authenticate user (phone + password)
    User authenticate(String phone, String password);
    
    // Check if phone number already exists
    boolean isPhoneExists(String phone);
    
    // ============ PASSWORD MANAGEMENT METHODS ============
    
    // Update user password (requires old password verification)
    boolean updatePassword(Long userId, String oldPassword, String newPassword);
    
    // Reset password (admin or forgot password use)
    boolean resetPassword(Long userId, String newPassword);
}