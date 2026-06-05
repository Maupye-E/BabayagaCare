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
public class HealthLesson implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String lessonKey;
    private String title;
    
    @Column(length = 2000)
    private String content;
    
    private String language;
    private String imageUrl;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    private boolean active;
    
    // ============ CONSTRUCTORS ============
    
    public HealthLesson() {}
    
    public HealthLesson(String lessonKey, String title, String content, String language) {
        this.lessonKey = lessonKey;
        this.title = title;
        this.content = content;
        this.language = language;
        this.active = true;
        this.createdDate = new Date();
    }
    
    public HealthLesson(String lessonKey, String title, String content, String language, String imageUrl) {
        this.lessonKey = lessonKey;
        this.title = title;
        this.content = content;
        this.language = language;
        this.imageUrl = imageUrl;
        this.active = true;
        this.createdDate = new Date();
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLessonKey() {
        return lessonKey;
    }

    public void setLessonKey(String lessonKey) {
        this.lessonKey = lessonKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    // ============ UTILITY METHODS ============
    
    public String getShortContent(int length) {
        if (content == null) return "";
        if (content.length() <= length) {
            return content;
        }
        return content.substring(0, length) + "...";
    }
    
    public String getIcon() {
        switch(lessonKey) {
            case "hiv": return "🩸";
            case "diabetes": return "🍬";
            case "hypertension": return "❤️";
            case "tb": return "🫁";
            default: return "📖";
        }
    }
    
    public String getColorClass() {
        switch(lessonKey) {
            case "hiv": return "lesson-hiv";
            case "diabetes": return "lesson-diabetes";
            case "hypertension": return "lesson-hypertension";
            case "tb": return "lesson-tb";
            default: return "lesson-default";
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HealthLesson)) {
            return false;
        }
        HealthLesson other = (HealthLesson) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HealthLesson{id=" + id + ", title='" + title + "', language='" + language + "'}";
    }
}