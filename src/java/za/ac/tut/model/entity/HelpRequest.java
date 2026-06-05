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
public class HelpRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String requestType;
    private String location;
    private String details;
    private boolean urgent;
    private String status;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedDate;
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;
    
    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private User volunteer;
    
    // ============ CONSTRUCTORS ============
    
    public HelpRequest() {}
    
    public HelpRequest(String requestType, String location, String details, User patient) {
        this.requestType = requestType;
        this.location = location;
        this.details = details;
        this.patient = patient;
        this.urgent = false;
        this.status = "open";
        this.requestDate = new Date();
    }
    
    public HelpRequest(String requestType, String location, String details, boolean urgent, User patient) {
        this.requestType = requestType;
        this.location = location;
        this.details = details;
        this.urgent = urgent;
        this.patient = patient;
        this.status = "open";
        this.requestDate = new Date();
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public User getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(User volunteer) {
        this.volunteer = volunteer;
    }
    
    // ============ UTILITY METHODS ============
    
    public String getRequestTypeIcon() {
        switch(requestType) {
            case "medication": return "💊";
            case "clinic": return "🚗";
            case "food": return "🍲";
            case "care": return "👵";
            default: return "📞";
        }
    }
    
    public String getRequestTypeText() {
        switch(requestType) {
            case "medication": return "Medication Pickup";
            case "clinic": return "Clinic/Hospital Ride";
            case "food": return "Food Parcel";
            case "care": return "Home Care Check-in";
            default: return "Other";
        }
    }
    
    public String getStatusBadge() {
        switch(status) {
            case "open": return "🟡 Open";
            case "assigned": return "🔵 Assigned";
            case "completed": return "✅ Completed";
            case "cancelled": return "❌ Cancelled";
            default: return "⚪ Unknown";
        }
    }
    
    public void assignToVolunteer(User volunteer) {
        this.volunteer = volunteer;
        this.status = "assigned";
        this.assignedDate = new Date();
    }
    
    public void complete() {
        this.status = "completed";
        this.completedDate = new Date();
    }
    
    public void cancel() {
        this.status = "cancelled";
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HelpRequest)) {
            return false;
        }
        HelpRequest other = (HelpRequest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HelpRequest{id=" + id + ", type='" + requestType + "', status='" + status + "'}";
    }
}