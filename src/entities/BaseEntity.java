package entities;
import interfaces.*;

public abstract class BaseEntity implements Identifiable, SerializableEntity {
    protected final String DELIMITER = ";";
}
