/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.service;

import za.ac.tut.model.dao.UserDAO;
import za.ac.tut.model.dao.impl.UserDAOImpl;
import za.ac.tut.model.entity.User;
import java.util.Date;
import java.util.List;

/**
 * Service class for User business logic
 * 
 * @author USER
 */
public class UserService {
    
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAOImpl();
    }
    
    // ============ REGISTRATION METHODS WITH PASSWORD ============
    
    // Register new user with password
    public User registerUser(String name, String phone, String password, String language, String location) {
        // Check if phone already exists
        if (userDAO.isPhoneExists(phone)) {
            System.err.println("Phone number already registered: " + phone);
            return null;
        }
        
        // Validate password
        if (password == null || password.length() < 6) {
            System.err.println("Password must be at least 6 characters");
            return null;
        }
        
        // Create new user
        User user = new User(name, phone, language);
        user.setPassword(password);
        user.setLocation(location);
        user.setRegistrationDate(new Date());
        user.setActive(true);
        user.setRole("patient");
        
        return userDAO.save(user);
    }
    
    // Register volunteer with password
    public User registerVolunteer(String name, String phone, String password, String language, String location) {
        // Check if phone already exists
        if (userDAO.isPhoneExists(phone)) {
            System.err.println("Phone number already registered: " + phone);
            return null;
        }
        
        // Validate password
        if (password == null || password.length() < 6) {
            System.err.println("Password must be at least 6 characters");
            return null;
        }
        
        // Create new volunteer
        User volunteer = new User(name, phone, language);
        volunteer.setPassword(password);
        volunteer.setLocation(location);
        volunteer.setRegistrationDate(new Date());
        volunteer.setActive(true);
        volunteer.setRole("volunteer");
        
        return userDAO.save(volunteer);
    }
    
    // ============ LOGIN METHODS WITH PASSWORD ============
    
    // Login with phone and password
    public User login(String phone, String password) {
        User user = userDAO.authenticate(phone, password);
        if (user != null && user.isActive()) {
            userDAO.updateLastLoginDate(user.getId());
            return user;
        }
        return null;
    }
    
    // Legacy login method (without password) - DEPRECATED, kept for compatibility
    @Deprecated
    public User loginWithoutPassword(String phone) {
        User user = userDAO.findByPhone(phone);
        if (user != null && user.isActive()) {
            userDAO.updateLastLoginDate(user.getId());
            return user;
        }
        return null;
    }
    
    // ============ PASSWORD MANAGEMENT ============
    
    // Change password
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            System.err.println("New password must be at least 6 characters");
            return false;
        }
        return userDAO.updatePassword(userId, oldPassword, newPassword);
    }
    
    // Reset password (admin only)
    public boolean resetPassword(Long userId, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            System.err.println("Password must be at least 6 characters");
            return false;
        }
        return userDAO.resetPassword(userId, newPassword);
    }
    
    // ============ USER MANAGEMENT ============
    
    // Get user by ID
    public User getUserById(Long userId) {
        return userDAO.findById(userId);
    }
    
    // Get user by phone
    public User getUserByPhone(String phone) {
        return userDAO.findByPhone(phone);
    }
    
    // Get all users (for admin)
    public List<User> findAll() {
        return userDAO.findAll();
    }
    
    // Get users by role
    public List<User> getUsersByRole(String role) {
        return userDAO.findByRole(role);
    }
    
    // Update user profile
    public User updateUserProfile(Long userId, String name, String language, String location) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setName(name);
            user.setLanguage(language);
            user.setLocation(location);
            return userDAO.update(user);
        }
        return null;
    }
    
    // Update user profile with password
    public User updateUserProfile(Long userId, String name, String language, String location, String password) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setName(name);
            user.setLanguage(language);
            user.setLocation(location);
            if (password != null && !password.isEmpty() && password.length() >= 6) {
                user.setPassword(password);
            }
            return userDAO.update(user);
        }
        return null;
    }
    
    // Update user role (admin only)
    public User updateUserRole(Long userId, String role) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setRole(role);
            return userDAO.update(user);
        }
        return null;
    }
    
    // ============ QUERY METHODS ============
    
    // Get all patients
    public List<User> getAllPatients() {
        return userDAO.findByRole("patient");
    }
    
    // Get all volunteers
    public List<User> getAllVolunteers() {
        return userDAO.findByRole("volunteer");
    }
    
    // Get all admins
    public List<User> getAllAdmins() {
        return userDAO.findByRole("admin");
    }
    
    // Get available volunteers
    public List<User> getAvailableVolunteers() {
        return userDAO.findAvailableVolunteers();
    }
    
    // Search users by name
    public List<User> searchUsersByName(String name) {
        return userDAO.searchByName(name);
    }
    
    // Get active users count
    public long getActiveUsersCount() {
        return userDAO.findActiveUsers().size();
    }
    
    // Get total users count
    public long getTotalUsersCount() {
        return userDAO.count();
    }
    
    // Get patients count
    public long getPatientsCount() {
        return getAllPatients().size();
    }
    
    // Get volunteers count
    public long getVolunteersCount() {
        return getAllVolunteers().size();
    }
    
    // Get admins count
    public long getAdminsCount() {
        return getAllAdmins().size();
    }
    
    // ============ ACCOUNT MANAGEMENT ============
    
    // Activate user account
    public void activateUser(Long userId) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setActive(true);
            userDAO.update(user);
        }
    }
    
    // Deactivate user account
    public void deactivateUser(Long userId) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setActive(false);
            userDAO.update(user);
        }
    }
    
    // Check if phone exists
    public boolean isPhoneExists(String phone) {
        return userDAO.isPhoneExists(phone);
    }
}