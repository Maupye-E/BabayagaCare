package za.ac.tut.model.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import za.ac.tut.model.entity.User;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2026-06-05T15:53:05")
@StaticMetamodel(Medication.class)
public class Medication_ { 

    public static volatile SingularAttribute<Medication, String> dosage;
    public static volatile SingularAttribute<Medication, String> instructions;
    public static volatile SingularAttribute<Medication, Boolean> taken;
    public static volatile SingularAttribute<Medication, String> name;
    public static volatile SingularAttribute<Medication, Long> id;
    public static volatile SingularAttribute<Medication, String> time;
    public static volatile SingularAttribute<Medication, User> user;
    public static volatile SingularAttribute<Medication, Date> dateAdded;

}