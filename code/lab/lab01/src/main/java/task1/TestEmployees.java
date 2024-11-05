package task1;

import org.junit.Test;

/**
 * @author Will
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-09-10 11:01
 */
public class TestEmployees {

    @Test
    public void testEmployees() {
        Employees employees = new Employees();
        Employee employee1 = new Employee("Will", 20);
        Employee employee2 = new Employee("Tom", 21);
        Employee employee3 = new Employee("Jerry", 22);
        employees.addEmployee(employee1);
        employees.addEmployee(employee2);
        employees.addEmployee(employee3);
        employees.printEmployees();
        employees.deleteEmployee(employee2);
        employees.printEmployees();
    }
}
