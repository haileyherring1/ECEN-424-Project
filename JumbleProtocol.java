import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class JumbleProtocol {

    private static final String[] WORDS_DATABASE = new String[] {
        "aggies","engineering","program","reveille","football","howdy"
    };

    int numberOfGuesses = 0;
    String original = selectRandomWord();
    String shuffled = getShuffledWord(original);

    private static final int WAITING = 0;
    private static final int SENDJUMBLE = 1;
    private static final int SENTJUMBLE = 2;
    private static final int ANOTHER = 3;

    private int state = WAITING;
    private int jumbleNumber = 0;
    private int numGuesses = 0;
    private long start = 0;
    private long end = 0;
    private long time = 0;

    public String selectRandomWord() {
        int rPos = ThreadLocalRandom.current().nextInt(0, WORDS_DATABASE.length);
        return WORDS_DATABASE[rPos];
    }

    public String getShuffledWord(String original) {
        String shuffledWord = original; // start with original
        int wordSize = original.length();
        int shuffleCount = 10; // let us randomly shuffle letters 10 times
        for(int i=0;i<shuffleCount;i++) {
            //swap letters in two indexes
            int position1 = ThreadLocalRandom.current().nextInt(0, wordSize);
            int position2 = ThreadLocalRandom.current().nextInt(0, wordSize);
            shuffledWord = swapCharacters(shuffledWord,position1,position2);
        }
        return shuffledWord;
    }

    private String swapCharacters(String shuffledWord, int position1, int position2) {
        char[] charArray = shuffledWord.toCharArray();
        char temp = charArray[position1];
        charArray[position1] = charArray[position2];
        charArray[position2] = temp;
        return new String(charArray);
    }

    public String processInput(String theInput) {
        String theOutput = null;

        if (state == WAITING) {
            theOutput = "Unjumble the following word in as few guesses as possible!" + 
            " Type your guess and hit enter; you have unlimited guesses! Hit Enter to start.";
            state = SENDJUMBLE;
        } else if (state == SENDJUMBLE) {
            //Test time to compute
            start = System.nanoTime();
            theOutput = shuffled;
            state = SENTJUMBLE;
        } else if (state == SENTJUMBLE) {
            if (theInput.equalsIgnoreCase(original)) {
                theOutput = "Congratulations! Hit enter to check your score!";
                end = System.nanoTime();
                time = end - start;
                state = ANOTHER;
            } else {
                theOutput = "Incorrect. Please try again.";
                numGuesses++;
                //Test time to compute
            }
        }  else if (state == ANOTHER) {
                    theOutput = "You found the word in " +numGuesses+ " guesses and " +time+ " nanoseconds.";
                    state = WAITING;

        } 
        return theOutput;
    }
}