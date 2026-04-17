package src.entities;
import src.interfaces.*;

public abstract class BaseEntity implements Identifiable, SerializableEntity {
    protected final String DELIMITER = ";";
}
