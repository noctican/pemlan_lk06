package interfaces;

public interface SerializableEntity {
    String toDataString();
    void fromDataString(String data);
}