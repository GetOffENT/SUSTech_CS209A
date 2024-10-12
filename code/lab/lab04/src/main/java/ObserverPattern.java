// Main example class
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
class Subject {
    //TODO:
    private List<Consumer<String>> observers = new ArrayList<>();

    private String state;

    public void subscribe(Consumer<String> observer) {
        observers.add(observer);
    }

    public void unsubscribe(Consumer<String> observer) {
        observers.remove(observer);
    }

    public void changeState(String newState) {
        state = newState;
        notifyObservers();
    }

    private void notifyObservers() {
        observers.forEach(observer -> observer.accept(state));
    }

    public List<Consumer<String>> getObservers() {
        return observers;
    }

    public String getState() {
        return state;
    }
}

public class ObserverPattern {
    public static void main(String[] args) {
        // Create the subject
        Subject subject = new Subject();

        // Define observer 1: print the state change
        //TODO:
        Consumer<String> observer1 = string -> System.out.println("Observer 1: state changed to " + string);

        // Define observer 2: log the state change
        //TODO:
        Consumer<String> observer2 = string -> System.out.println("Observer 2: logging state change to " + string);

        // Define observer 3: perform specific actions
        //TODO:
        Consumer<String> observer3 = state -> {
            switch (state) {
                case "START" -> System.out.println("Observer 3: starting");
                case "STOP" -> System.out.println("Observer 3: stopping");
                case "RESTART" -> System.out.println("Observer 3: restarting");
            }
        };

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