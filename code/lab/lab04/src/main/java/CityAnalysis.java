import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CityAnalysis {
    public static class City
    {
        private String name;
        private String state;
        private int population;

        public City(String name, String state, int population)
        {
            this.name = name;
            this.state = state;
            this.population = population;
        }

        public String getName()
        {
            return name;
        }

        public String getState()
        {
            return state;
        }

        public int getPopulation()
        {
            return population;
        }

        @Override
        public String toString() {
            return "City{" +
                    "name='" + name + '\'' +
                    ", state='" + state + '\'' +
                    ", population=" + population +
                    '}';
        }
    }

    public static Stream<City> readCities(String filename) throws IOException
    {
        return Files.lines(Paths.get(filename))
                .map(l -> l.split(", "))
                .map(a -> new City(a[0], a[1], Integer.parseInt(a[2])));
    }
    private static final String FILENAME = "lab04/src/main/resources/cities.txt";

    public static void main(String[] args) throws IOException {

        Stream<City> cities = readCities(FILENAME);
        // Q1: count how many cities there are for each state
        // TODO: Map<String, Long> cityCountPerState = ...
        cities.collect(Collectors.groupingBy(City::getState, Collectors.counting()))
                .forEach((state, count) -> System.out.println(state + ": " + count));

        cities = readCities(FILENAME);
        // Q2: count the total population for each state
        // TODO: Map<String, Integer> statePopulation = ...
        cities.collect(Collectors.groupingBy(City::getState, Collectors.summingInt(City::getPopulation)))
                .forEach((state, population) -> System.out.println(state + ": " + population));


        cities = readCities(FILENAME);
        // Q3: for each state, get the city with the longest name
        // TODO: Map<String, String> longestCityNameByState = ...
        cities.collect(Collectors.groupingBy(City::getState,
                Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(c -> c.getName().length())),
                        c -> c.get().getName())))
                .forEach((state, city) -> System.out.println(state + ": " + city));

        cities = readCities(FILENAME);
        // Q4: for each state, get the set of cities with >500,000 population
        // TODO: Map<String, Set<City>> largeCitiesByState = ...
        cities.collect(Collectors.groupingBy(City::getState,
                Collectors.filtering(c -> c.getPopulation() > 500000, Collectors.toSet())))
                .forEach((state, citiesSet) -> System.out.println(state + ": " + citiesSet));

    }
}
