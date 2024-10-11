package Service;

public interface Service<T> {
    void display();

    void add(T entity); // sửa lại

    void save();

    void update();

}
