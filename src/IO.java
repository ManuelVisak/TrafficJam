
import yanwittmann.file.*;

import java.io.IOException;
import java.util.Objects;

public class IO {

    public int[][] readLevel(int levelNumber) {
        String textLevel = "";
        File levelFile = new File("levelFiles\\level" + levelNumber + ".dat");

        try {
            textLevel = levelFile.readToArray()[0];
        } catch (IOException e) {
            e.printStackTrace();
        }

        int numberOfCars = textLevel.length() / 5;
        int[][] level = new int[numberOfCars][4];

        for (int i = 0; i < numberOfCars; i++) {
            int j = 0;
            while(',' != textLevel.charAt(0)) {
                level[i][j] = (int) textLevel.charAt(0) - 48;
                j++;
                textLevel = textLevel.substring(1);
            }
            textLevel = textLevel.substring(1);
        }
        return level;
    }

    public void saveLevel(int[][] level) {
        int levelNumber = countSavedLevels();
        File levelFile = new File("levelFiles/level" + levelNumber + ".dat");
        String textLevel = "";
        for (int i = 0; i < level.length; i++) {
            if (!(level[i][2] == 0))
                textLevel = textLevel + "" + level[i][0] + "" + level[i][1] + "" + level[i][2] + "" + level[i][3] + ",";
        }
        System.out.println(textLevel);
        try {
            levelFile.write(textLevel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private int countSavedLevels() {         //list() source: https://javabeat.net/java-count-files-directory/
        return Objects.requireNonNull(new File("levelFiles").list()).length;
    }
}
