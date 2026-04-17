package interfaces;
import entities.BaseEntity;
import java.util.Scanner;


public interface DataFormHandler<T extends BaseEntity> {
    T createInput(Scanner scanner);
    T updateInput(Scanner scanner, T existingData);
    void displayItem(T item);
}