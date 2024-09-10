package task1;

import java.util.Comparator;
import java.util.HashSet;

/**
 * @author Will
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-09-10 10:55
 */
public class Employees {
    HashSet<Employee> employees = new HashSet<>();

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void deleteEmployee(Employee employee) {
        employees.remove(employee);
    }

    /**
     * Print the info of all employees by the alphabetical order of their names.
     */
    public void printEmployees() {
        employees.stream().sorted(Comparator.comparing(Employee::getName)).forEach(System.out::println);
        System.out.println();
    }
}
