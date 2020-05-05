import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

public class Day04 {

    static int getID(String s) {
        return Integer.parseInt(s.substring(0,s.indexOf("[")));
    }

    static String getCheckSum(String s) {
        return s.substring(s.indexOf("[") + 1, s.length()-1);
    }

    static char rot(int rotation, char c) {
        return (char) ((((c - 'a') + rotation ) % 26) + 'a');
    }

    public static void main(String[] args) {

        List<String[]> input = getFileLinesSplit("/Users/vamshiramineni/Documents/workspace/Advent2016/src/day04.txt", "-");
        List<String[]> part2 = new ArrayList<>();
        int sum = 0;

        for (String[] each : input) {
            Map<Character, Integer> freq = new HashMap<>();
            
            // make frequency table
            for (int i = 0; i < each.length - 1; i++) {
                each[i].chars()
                        .filter(x -> x != '-')
                        .forEach(x -> freq.put((char) x, freq.getOrDefault((char) x, 0) + 1));
            }
            
            // get top 5 frequencies, sorted by character order and then frequency
            // and turn into string
            String top5 = freq.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .limit(5)
                    .map(Map.Entry::getKey)
                    .map(x -> Character.toString(x))
                    .collect(Collectors.joining(""));

            String checkSum = getCheckSum(each[each.length - 1]);
            
            if (top5.equals(checkSum)) {
                sum += getID(each[each.length - 1]);
                
                // add to list (not dummy data)
                part2.add(each);
            }
        }
        System.out.println(sum);
        
        // decrypt data
        for (String[] each : part2) {
            String temp = "";
            for (int i = 0; i < each.length - 1; i++) {
                for (int j = 0; j < each[i].length(); j++) {
                    temp += rot(getID(each[each.length - 1]), each[i].charAt(j));
                }
                temp += "-";
            }
            temp += each[each.length - 1];
            if (temp.startsWith("northpole-object-storage")) System.out.println(temp);
        }
    }
    
    /**
     * Return an ArrayList of String Arrays, split using the given delimiter
     * @param filename file in current working directory or full pathname
     * @param delimiter REGEX string delimiter. Catches PatternSyntaxException.
     * @return List of String Arrays
     */
    public static List<String[]> getFileLinesSplit(String filename, String delimiter) {
        List<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String input;
            while ((input = br.readLine()) != null) {
                try {
                    String[] s = input.split(delimiter);
                    list.add(s);
                } catch (PatternSyntaxException pse) {
                    System.out.println("Bad regex syntax. Delimiter \"" + delimiter + " \"");
                    return null;
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return list;

    }
}