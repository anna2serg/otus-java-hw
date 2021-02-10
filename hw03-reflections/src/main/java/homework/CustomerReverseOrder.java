package homework;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    private Deque<Customer> customers = new ArrayDeque<>();

    public void add(Customer customer) {
        customers.push(customer);
    }

    public Customer take() {
        return customers.pop();
    }
}
