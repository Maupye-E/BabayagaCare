package za.ac.tut.model.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import za.ac.tut.model.entity.User;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2026-06-05T15:53:05")
@StaticMetamodel(HelpRequest.class)
public class HelpRequest_ { 

    public static volatile SingularAttribute<HelpRequest, Date> assignedDate;
    public static volatile SingularAttribute<HelpRequest, User> volunteer;
    public static volatile SingularAttribute<HelpRequest, String> requestType;
    public static volatile SingularAttribute<HelpRequest, User> patient;
    public static volatile SingularAttribute<HelpRequest, Date> requestDate;
    public static volatile SingularAttribute<HelpRequest, String> location;
    public static volatile SingularAttribute<HelpRequest, String> details;
    public static volatile SingularAttribute<HelpRequest, Long> id;
    public static volatile SingularAttribute<HelpRequest, Boolean> urgent;
    public static volatile SingularAttribute<HelpRequest, Date> completedDate;
    public static volatile SingularAttribute<HelpRequest, String> status;

}