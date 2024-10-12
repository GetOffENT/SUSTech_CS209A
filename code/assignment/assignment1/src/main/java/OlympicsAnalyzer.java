import constant.PathConstant;
import entity.*;
import utils.CSVReader;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class OlympicsAnalyzer implements OlympicsAnalyzerInterface {
    public static void main(String[] args) {
        String path = "assignment1/src/main/resources/local_test_data/Olympic_Results.csv";
        System.out.println(path);

        File file = new File(path);
        System.out.println(file.exists());
    }

    private final List<AthleteBioFiltered> athletes;
    private final List<AthleteEventResult> athleteEventResults;
    private final List<Country> countries;
    private final List<Game> games;
    private final List<GamesMedalTally> gamesMedalTallyList;
//    private final List<Result> resultList;


    public OlympicsAnalyzer(String datasetPath) {
        // Load the data from the datasetPath
        athletes = CSVReader.mapCsvToClass(datasetPath + PathConstant.OLYMPIC_ATHLETE_BIO_FILTERED, AthleteBioFiltered.class);
        athleteEventResults = CSVReader.mapCsvToClass(datasetPath + PathConstant.OLYMPIC_ATHLETE_EVENT_RESULTS, AthleteEventResult.class);
        countries = CSVReader.mapCsvToClass(datasetPath + PathConstant.OLYMPICS_COUNTRY, Country.class);
        games = CSVReader.mapCsvToClass(datasetPath + PathConstant.OLYMPICS_GAMES, Game.class);
        gamesMedalTallyList = CSVReader.mapCsvToClass(datasetPath + PathConstant.OLYMPIC_GAMES_MEDAL_TALLY, GamesMedalTally.class);
//        resultList = CSVReader.mapCsvToClass(datasetPath + PathConstant.OLYMPIC_RESULTS, Result.class);
    }

    @Override
    public Map<String, Integer> topPerformantFemale() {
//        Set<Integer> femaleAthleteIds = athletes.stream()
//                .filter(athlete -> "Female".equals(athlete.getSex()))
//                .map(AthleteBioFiltered::getAthlete_id)
//                .collect(Collectors.toSet());
//
//        Map<String, Integer> goldMedalCount = new HashMap<>();
//
//        athleteEventResults.stream()
//                .filter(result -> "Gold".equals(result.getMedal()) && !result.getTeamSport())
//                .filter(result -> femaleAthleteIds.contains(result.getAthlete_id()))
//                .forEach(result -> {
//                    String athleteName = athletes.stream()
//                            .filter(athlete -> athlete.getAthlete_id().equals(result.getAthlete_id()))
//                            .map(AthleteBioFiltered::getName)
//                            .findFirst()
//                            .orElse("Unknown");
//
//                    goldMedalCount.put(athleteName, goldMedalCount.getOrDefault(athleteName, 0) + 1);
//                });
//
//        return goldMedalCount.entrySet().stream()
//                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
//                        .thenComparing(Map.Entry.comparingByKey()))
//                .limit(10)
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (e1, e2) -> e1,
//                        LinkedHashMap::new
//                ));


        Set<Integer> femaleAthleteIds = athletes.stream()
                .filter(athlete -> "Female".equals(athlete.getSex()))
                .map(AthleteBioFiltered::getAthlete_id)
                .collect(Collectors.toSet());

        Map<Integer, String> athleteNameMap = athletes.stream()
                .collect(Collectors.toMap(AthleteBioFiltered::getAthlete_id, AthleteBioFiltered::getName));

        Map<String, Integer> goldMedalCount = new HashMap<>();

        athleteEventResults.stream()
                .filter(result -> "Gold".equals(result.getMedal()) && !result.getTeamSport())
                .filter(result -> femaleAthleteIds.contains(result.getAthlete_id()))
                .forEach(result -> {
                    String athleteName = athleteNameMap.getOrDefault(result.getAthlete_id(), "Unknown");
                    goldMedalCount.put(athleteName, goldMedalCount.getOrDefault(athleteName, 0) + 1);
                });

        return goldMedalCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Map<String, Float> bmiBySports() {
        Map<Integer, AthleteBioFiltered> athleteMap = athletes.stream()
                .filter(a -> a.getHeight() != null && a.getWeight() != null)
                .collect(Collectors.toMap(AthleteBioFiltered::getAthlete_id, a -> a));

        return athleteEventResults.stream()
                .filter(result -> athleteMap.containsKey(result.getAthlete_id()))
                .collect(Collectors.groupingBy(AthleteEventResult::getSport))
                .entrySet()
                .stream()
                .map(e -> Map.entry(e.getKey(), new ArrayList<>(e.getValue().stream()
                        .map(result -> athleteMap.get(result.getAthlete_id()))
                        .collect(Collectors.toSet()))))
                .map(e -> Map.entry(e.getKey(), e.getValue().stream()
                                .map(athlete -> athlete.getWeight() / Math.pow(athlete.getHeight() / 100, 2))
                                .mapToDouble(Double::doubleValue).average().orElse(0.0)
                        )
                )
                .map(e -> Map.entry(e.getKey(), Math.round(e.getValue().floatValue() * 10) / 10.0f))
                .sorted(Map.Entry.<String, Float>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Map<String, Set<Integer>> leastAppearedSport() {
        Map<Integer, Game> gameMap = games.stream()
                .collect(Collectors.toMap(Game::getEdition_id, g -> g));

        return athleteEventResults.stream()
                .filter(result -> result.getEdition().endsWith("Summer Olympics"))
                .collect(Collectors.groupingBy(AthleteEventResult::getSport))
                .entrySet()
                .stream()
                .map(e -> Map.entry(e.getKey(), e.getValue().stream()
                                .map(result -> gameMap.get(result.getEdition_id()))
                                .map(Game::getYear)
                                .map(Integer::parseInt)
                                .collect(Collectors.toSet())
                        )
                )
                .sorted((e1, e2) -> {
                    long count1 = e1.getValue().size();
                    long count2 = e2.getValue().size();
                    return count1 == count2 ? e1.getKey().compareTo(e2.getKey()) : Long.compare(count1, count2);
                })
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Map<String, Integer> winterMedalsByCountry() {
        return gamesMedalTallyList.stream()
                .filter(e -> Integer.parseInt(e.getYear()) >= 2000 && e.getEdition().endsWith("Winter Olympics"))
                .collect(Collectors.groupingBy(GamesMedalTally::getCountry_noc))
                .entrySet()
                .stream()
                .map(e -> Map.entry(e.getKey(), e.getValue().stream()
                        .map(GamesMedalTally::getTotal).mapToInt(Integer::intValue).sum())
                )
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Map<String, Integer> topCountryWithYoungAthletes() {
        Map<Integer, AthleteBioFiltered> athleteMap = athletes.stream()
                .filter(a -> a.getBorn() != null)
                .collect(Collectors.toMap(AthleteBioFiltered::getAthlete_id, a -> a));
        Map<String, String> countryMap = countries.stream()
                .collect(Collectors.toMap(Country::getNoc, Country::getCountry, (v1, v2) -> v1));

        return athleteEventResults.stream()
                .filter(a -> "2020 Summer Olympics".equals(a.getEdition()) && athleteMap.containsKey(a.getAthlete_id()))
                .collect(Collectors.groupingBy(AthleteEventResult::getCountry_noc))
                .entrySet()
                .stream()
                .map(e -> Map.entry(e.getKey(), e.getValue().stream()
                                .map(result -> athleteMap.get(result.getAthlete_id()))
                                .collect(Collectors.toSet())
                                .stream()
                                .map(athlete -> {
                                    String born = athlete.getBorn();
                                    int year = Integer.parseInt(born.substring(born.length() - 4));
                                    return 2020 - year;
                                })
                                .mapToDouble(Integer::doubleValue)
                                .average().orElse(0.0)
                        )
                )
                .map(e -> Map.entry(countryMap.get(e.getKey()), Long.valueOf(Math.round(e.getValue())).intValue()))
                .sorted(Map.Entry.<String, Integer>comparingByValue()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
