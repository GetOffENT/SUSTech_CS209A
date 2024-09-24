package task2;

import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author Will
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-09-24 11:56
 */
public class testWordFrequency {


    @Test
    public void test(){
        WordFrequency wordFrequency = new WordFrequency();
        wordFrequency.wordFrequency(new File("src/main/resources/alice.txt"));
    }


}
