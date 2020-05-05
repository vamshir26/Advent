import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

public class Day10 {

    static class Bot {
        int name;
        int low;
        int high;
        int count;

        Bot lowBot;
        Bot highBot;

        Bot(int name) {
            this.name = name;
        }

        Bot(int name, int chip) {
            this.name = name;
            this.high = chip;
            count++;
        }


        void placeChip(int chip) {
            if (count >= 2) return;
            if (count == 0) {
                high = chip;
            } else {
                if (low == 0) {
                    if (chip < high) {
                        low = chip;
                    } else {
                        low = high;
                        high = chip;
                    }
                } else {
                    if (chip > low) {
                        high = chip;
                    } else {
                        high = low;
                        low = chip;
                    }
                }
            }
            count++;
        }

        void transfer() {
            checkWinner();
            lowBot.placeChip(this.low);
            highBot.placeChip(this.high);
            count = 0;
        }

         void checkWinner() {
            if (low == 17 && high == 61) {
                System.out.println("found ==>" + this);
           }
         }

        public String toString() {
            return "Bot " + name + " Low: " + low + " High: " + high;
        }
    }

    public static void main(String[] args) {
        List<String[]> input = getFileLinesSplit("/Users/vamshiramineni/Documents/workspace/Advent/src/practice/advent10.txt", " ");

        Map<Integer, Bot> botMap = new HashMap<>();

        for (String[] line : input) {
            if (line.length == 6) {
                Integer key = Integer.parseInt(line[5]);
                if (botMap.containsKey(key)) {
                    botMap.get(key).placeChip(Integer.parseInt(line[1]));
                } else {
                    Bot bot = new Bot(key, Integer.parseInt(line[1]));
                    botMap.put( key, bot );
                }
            } else {
                int botA = Integer.parseInt(line[1]);
                int low = Integer.parseInt(line[6]);
                int high = Integer.parseInt(line[11]);
                Bot parent = botMap.getOrDefault(botA, new Bot(botA));
                if (line[5].equals("output")) low += 1000; // horrible hack to separate output nodes
                if (line[10].equals("output")) high += 1000;
                Bot botLow = botMap.getOrDefault(low, new Bot(low));
                Bot botHigh = botMap.getOrDefault(high, new Bot(high));
                botMap.putIfAbsent(botA, parent);
                botMap.putIfAbsent(low, botLow);
                botMap.putIfAbsent(high, botHigh);
                parent.lowBot = botLow;
                parent.highBot = botHigh;

            }
        }

        while(true) {

            botMap.entrySet().stream()
                    .filter(x -> x.getValue().count == 2)
                    .map(Map.Entry::getValue)
                    .forEach(Bot::transfer);

            if ((botMap.entrySet().stream()
                    .filter(x -> x.getValue().count == 2)
                    .count()) == 0 ) {
                break;
            }
        }

        //System.out.println("Found bot # " + found);

        int output = botMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(x -> x.name >= 1000 && x.name < 1003)
                .map(x -> x.high)
                .reduce(1, Math::multiplyExact);

        System.out.println(output);


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
