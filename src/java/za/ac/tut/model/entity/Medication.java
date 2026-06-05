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
public class Medication implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String dosage;
    private String time;
    private String instructions;
    private boolean taken;
    
    @Temporal(TemporalType.DATE)
    private Date dateAdded;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    // ============ CONSTRUCTORS ============
    
    public Medication() {}
    
    public Medication(String name, String dosage, String time, User user) {
        this.name = name;
        this.dosage = dosage;
        this.time = time;
        this.user = user;
        this.taken = false;
        this.dateAdded = new Date();
    }
    
    public Medication(String name, String dosage, String time, String instructions, User user) {
        this.name = name;
        this.dosage = dosage;
        this.time = time;
        this.instructions = instructions;
        this.user = user;
        this.taken = false;
        this.dateAdded = new Date();
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

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    // ============ UTILITY METHODS ============
    
    public String getStatusIcon() {
        return taken ? "✅" : "⭕";
    }
    
    public String getStatusText() {
        return taken ? "Taken today" : "Not taken yet";
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Medication)) {
            return false;
        }
        Medication other = (Medication) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Medication{id=" + id + ", name='" + name + "', time='" + time + "'}";
    }
}