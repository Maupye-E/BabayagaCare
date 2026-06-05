package za.ac.tut.model.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import za.ac.tut.model.entity.User;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2026-06-05T15:53:05")
@StaticMetamodel(Symptom.class)
public class Symptom_ { 

    public static volatile SingularAttribute<Symptom, Integer> severity;
    public static volatile SingularAttribute<Symptom, Date> symptomDate;
    public static volatile SingularAttribute<Symptom, String> notes;
    public static volatile SingularAttribute<Symptom, Date> loggedAt;
    public static volatile SingularAttribute<Symptom, Date> symptomTime;
    public static volatile SingularAttribute<Symptom, Long> id;
    public static volatile SingularAttribute<Symptom, User> user;
    public static volatile SingularAttribute<Symptom, String> symptomName;

}