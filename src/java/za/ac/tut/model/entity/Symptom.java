/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author USER
 */
@Entity
public class Symptom implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String symptomName;
    private Integer severity;
    private String notes;
    
    @Temporal(TemporalType.DATE)
    private Date symptomDate;
    
    @Temporal(TemporalType.TIME)
    private Date symptomTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date loggedAt;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    // ============ CONSTRUCTORS ============
    
    public Symptom() {}
    
    public Symptom(String symptomName, Integer severity, User user) {
        this.symptomName = symptomName;
        this.severity = severity;
        this.user = user;
        this.symptomDate = new Date();
        this.symptomTime = new Date();
        this.loggedAt = new Date();
    }
    
    public Symptom(String symptomName, Integer severity, String notes, User user) {
        this.symptomName = symptomName;
        this.severity = severity;
        this.notes = notes;
        this.user = user;
        this.symptomDate = new Date();
        this.symptomTime = new Date();
        this.loggedAt = new Date();
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymptomName() {
        return symptomName;
    }

    public void setSymptomName(String symptomName) {
        this.symptomName = symptomName;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getSymptomDate() {
        return symptomDate;
    }

    public void setSymptomDate(Date symptomDate) {
        this.symptomDate = symptomDate;
    }

    public Date getSymptomTime() {
        return symptomTime;
    }

    public void setSymptomTime(Date symptomTime) {
        this.symptomTime = symptomTime;
    }

    public Date getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(Date loggedAt) {
        this.loggedAt = loggedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    // ============ UTILITY METHODS ============
    
    public String getSeverityText() {
        switch(severity) {
            case 1: return "Mild";
            case 2: return "Moderate";
            case 3: return "Uncomfortable";
            case 4: return "Severe";
            case 5: return "Very Severe";
            default: return "Unknown";
        }
    }
    
    public String getSeverityEmoji() {
        switch(severity) {
            case 1: return "🟢";
            case 2: return "🟡";
            case 3: return "🟠";
            case 4: return "🔴";
            case 5: return "‼️";
            default: return "⚪";
        }
    }
    
    // FIXED: Java 8 compatible version (no String.repeat())
    public String getSeverityStars() {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < severity; i++) {
            stars.append("⭐");
        }
        return stars.toString();
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Symptom)) {
            return false;
        }
        Symptom other = (Symptom) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Symptom{id=" + id + ", symptomName='" + symptomName + "', severity=" + severity + "}";
    }
}