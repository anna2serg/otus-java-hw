package homework;

import java.util.*;

public class CustomerService {

    private TreeMap<Customer, String> customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> firstCustomer = customers.firstEntry();
        return Objects.isNull(firstCustomer) ? null :
                new AbstractMap.SimpleEntry<>(new Customer(firstCustomer.getKey()), firstCustomer.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextCustomer = customers.higherEntry(customer);
        return Objects.isNull(nextCustomer) ? null :
                new AbstractMap.SimpleEntry<>(new Customer(nextCustomer.getKey()), nextCustomer.getValue());
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}
