package task2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Will
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-09-24 11:05
 */
public class Main {
    public static <T> List<T> filter(List<T> list, MyPredicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (p.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please input the function no:");
            System.out.println("1 - Get even numbers");
            System.out.println("2 - Get odd numbers");
            System.out.println("3 - Get prime numbers");
            System.out.println("0 - Quit");

            int choice = scanner.nextInt();

            if (choice == 0) {
                break;
            }

            System.out.println("Input the integer list:");
            scanner.nextLine();
            String[] numbers = scanner.nextLine().split(" ");
            List<Integer> intList = new ArrayList<>();
            for (String num : numbers) {
                intList.add(Integer.parseInt(num));
            }

            List<Integer> filteredList = new ArrayList<>();
            switch (choice) {
                case 1:
                    filteredList = filter(intList, Main::isEven);
                    break;
                case 2:
                    filteredList = filter(intList, Main::isOdd);
                    break;
                case 3:
                    filteredList = filter(intList, Main::isPrime);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

            System.out.println("Filter results:");
            System.out.println(filteredList);
        }
    }

    public static boolean isEven(Integer num) {
        return num % 2 == 0;
    }

    public static boolean isOdd(Integer num) {
        return num % 2 != 0;
    }

    public static boolean isPrime(Integer num) {
        if (num < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}
