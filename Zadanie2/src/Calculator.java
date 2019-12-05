//https://stackoverflow.com/questions/1623480/generic-interface

public interface Calculator<T> {

    T add(T first, T second);
    T subtract(T first, T second);
    T multiply(T first, T second);
    T divide(T first, T second);
    int compareTo(T first, T second);
    T returnZero();
    T abs(T x);
    T convertInt(int x);
    T parse(String s);
}
