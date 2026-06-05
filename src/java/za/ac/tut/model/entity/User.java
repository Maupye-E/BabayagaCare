/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author USER
 */
@Entity
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generate ID
    private Long id;
    
    private String name;
    private String phone;
    private String language;
    private String password;      // if you want password protection
    private String role;          // 'patient', 'volunteer', 'admin'
    private String location;      // suburb/township/city
    
    @Temporal(TemporalType.DATE)
    private Date registrationDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate;
    
    private boolean active;
    
    @OneToMany(mappedBy = "user")
    private List<Medication> medications;
    
    @OneToMany(mappedBy = "user")
    private List<Symptom> symptoms;
    
    // ============ CONSTRUCTORS ============
    
    public User() {}
    
    public User(String name, String phone, String language) {
        this.name = name;
        this.phone = phone;
        this.language = language;
        this.role = "patient";
        this.active = true;
        this.registrationDate = new Date();
    }
    
    public User(String name, String phone, String language, String password, String location) {
        this.name = name;
        this.phone = phone;
        this.language = language;
        this.password = password;
        this.location = location;
        this.role = "patient";
        this.active = true;
        this.registrationDate = new Date();
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }

    public List<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<Symptom> symptoms) {
        this.symptoms = symptoms;
    }
    
    // ============ UTILITY METHODS ============
    
    public boolean isPatient() {
        return "patient".equalsIgnoreCase(role);
    }
    
    public boolean isVolunteer() {
        return "volunteer".equalsIgnoreCase(role);
    }
    
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', phone='" + phone + "', role='" + role + "'}";
    }
}