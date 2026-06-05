package za.ac.tut.model.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import za.ac.tut.model.entity.Medication;
import za.ac.tut.model.entity.Symptom;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2026-06-05T15:53:05")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile ListAttribute<User, Symptom> symptoms;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, String> role;
    public static volatile SingularAttribute<User, String> phone;
    public static volatile ListAttribute<User, Medication> medications;
    public static volatile SingularAttribute<User, String> name;
    public static volatile SingularAttribute<User, Date> registrationDate;
    public static volatile SingularAttribute<User, Boolean> active;
    public static volatile SingularAttribute<User, String> language;
    public static volatile SingularAttribute<User, String> location;
    public static volatile SingularAttribute<User, Date> lastLoginDate;
    public static volatile SingularAttribute<User, Long> id;

}