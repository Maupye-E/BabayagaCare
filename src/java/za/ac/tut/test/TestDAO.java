/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.test;

import za.ac.tut.model.dao.UserDAO;
import za.ac.tut.model.dao.impl.UserDAOImpl;
import za.ac.tut.model.entity.User;
import java.util.List;

/**
 * Test class for DAO operations
 * 
 * @author USER
 */
public class TestDAO {
    
    public static void main(String[] args) {
        
        System.out.println("=== BABAYAGA CARE DAO TEST ===\n");
        
        // Initialize DAO
        UserDAO userDAO = new UserDAOImpl();
        
        // Test 1: Check if user exists first
        System.out.println("=== TEST 1: Create User ===");
        User existingUser = userDAO.findByPhone("0712345678");
        
        if (existingUser == null) {
            User newUser = new User("Thabo Nkosi", "0712345678", "English");
            newUser.setLocation("Soweto");
            User savedUser = userDAO.save(newUser);
            
            if (savedUser != null) {
                System.out.println("✅ User created with ID: " + savedUser.getId());
            } else {
                System.out.println("❌ Failed to create user");
            }
        } else {
            System.out.println("ℹ️ User already exists with ID: " + existingUser.getId());
            System.out.println("   Name: " + existingUser.getName());
            System.out.println("   Phone: " + existingUser.getPhone());
        }
        
        // Test 2: Find user by phone
        System.out.println("\n=== TEST 2: Find by Phone ===");
        User foundUser = userDAO.findByPhone("0712345678");
        if (foundUser != null) {
            System.out.println("✅ Found user: " + foundUser.getName() + " (Phone: " + foundUser.getPhone() + ")");
        } else {
            System.out.println("❌ User not found");
        }
        
        // Test 3: Update user
        System.out.println("\n=== TEST 3: Update User ===");
        if (foundUser != null) {
            foundUser.setLocation("Johannesburg");
            foundUser.setLanguage("isiZulu");
            User updatedUser = userDAO.update(foundUser);
            if (updatedUser != null) {
                System.out.println("✅ Updated user - Location: " + updatedUser.getLocation() + ", Language: " + updatedUser.getLanguage());
            } else {
                System.out.println("❌ Failed to update user");
            }
        }
        
        // Test 4: Count all users
        System.out.println("\n=== TEST 4: Count Users ===");
        long userCount = userDAO.count();
        System.out.println("✅ Total users in database: " + userCount);
        
        // Test 5: Find all users
        System.out.println("\n=== TEST 5: Find All Users ===");
        List<User> allUsers = userDAO.findAll();
        if (allUsers != null && !allUsers.isEmpty()) {
            for (User u : allUsers) {
                System.out.println("   - ID: " + u.getId() + ", Name: " + u.getName() + 
                                 ", Phone: " + u.getPhone() + ", Role: " + u.getRole());
            }
        } else {
            System.out.println("   No users found");
        }
        
        // Test 6: Check if phone exists
        System.out.println("\n=== TEST 6: Check Phone Exists ===");
        boolean phoneExists = userDAO.isPhoneExists("0712345678");
        System.out.println("✅ Phone 0712345678 exists: " + phoneExists);
        
        boolean phoneNotExists = userDAO.isPhoneExists("0999999999");
        System.out.println("✅ Phone 0999999999 exists: " + phoneNotExists);
        
        // Test 7: Find active users
        System.out.println("\n=== TEST 7: Find Active Users ===");
        List<User> activeUsers = userDAO.findActiveUsers();
        System.out.println("✅ Active users count: " + (activeUsers != null ? activeUsers.size() : 0));
        
        // Test 8: Find by role
        System.out.println("\n=== TEST 8: Find by Role (patient) ===");
        List<User> patients = userDAO.findByRole("patient");
        System.out.println("✅ Patients count: " + (patients != null ? patients.size() : 0));
        
        System.out.println("\n=== All tests completed! ===");
    }
}