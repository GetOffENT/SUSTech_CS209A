// Main example class
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
class Subject {
    //TODO:
}

public class ObserverPattern {
    public static void main(String[] args) {
        // Create the subject
        Subject subject = new Subject();

        // Define observer 1: print the state change
        //TODO:
        Consumer<String> observer1 = 

        // Define observer 2: log the state change
        //TODO:
        Consumer<String> observer2 = 

        // Define observer 3: perform specific actions
        //TODO:
        Consumer<String> observer3 = 

        // Subscribe the observers
        subject.subscribe(observer1);
        subject.subscribe(observer2);
        subject.subscribe(observer3);

        // Change state
        System.out.println("Changing state to 'START'.");
        subject.changeState("START");

        System.out.println("\nChanging state to 'STOP'.");
        subject.changeState("STOP");

        // Unsubscribe observer 2
        subject.unsubscribe(observer2);

        System.out.println("\nChanging state to 'RESTART', observer 2 is unsubscribed.");
        subject.changeState("RESTART");
    }
}