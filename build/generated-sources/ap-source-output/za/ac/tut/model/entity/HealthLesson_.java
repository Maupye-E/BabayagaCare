package za.ac.tut.model.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2026-06-05T15:53:05")
@StaticMetamodel(HealthLesson.class)
public class HealthLesson_ { 

    public static volatile SingularAttribute<HealthLesson, Date> createdDate;
    public static volatile SingularAttribute<HealthLesson, String> imageUrl;
    public static volatile SingularAttribute<HealthLesson, String> lessonKey;
    public static volatile SingularAttribute<HealthLesson, Boolean> active;
    public static volatile SingularAttribute<HealthLesson, String> language;
    public static volatile SingularAttribute<HealthLesson, Long> id;
    public static volatile SingularAttribute<HealthLesson, String> title;
    public static volatile SingularAttribute<HealthLesson, String> content;

}