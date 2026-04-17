package src.entities;
import java.util.*;
import java.util.function.Supplier;


public class DataListManager<T extends BaseEntity> {
    private List<T> list;
    private String filePath;
    private Supplier<T> factory;

    public DataListManager(String filePath, Supplier<T> factory) {}
}

