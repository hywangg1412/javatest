package Service;

public interface Service<T> {
    void display();

    void add(T entity);

    void save();

    void edit();
}
