package NativeLibraries;

import javax.swing.*;
import java.io.*;
import java.util.Arrays;

import NativeLibraries.log.*;
import sp.Panels.MenuPanel;
import sp.ResonantDebris;

/**rules to saves in ResonantDebris
 * <ul>
 * <il>{@code SaveScore()} to save
 * <p>
 * <il>{@code LoadScore()} to read the save
 * </ul>
 * these methods create the save in {@code /AppData/Roaming/ResonantDebris/save.bin}.
 * you can also change save route for yourself.*/
public interface DataLibrary {
//why is it an interface? for fun.

    static void saveScore(int score) {
        String path = System.getProperty("user.home") + "/AppData/Roaming/ResonantDebris";
        String filePath = path + "/save.bin";

        File directory = new File(path);
        File file = new File(filePath);

        try {

            if (!directory.exists()) {
                directory.mkdirs();
            }


            if (!file.exists()) {
                file.createNewFile();
            }


            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filePath))) {
                dos.writeInt(score);
                byte b = 1;

                //System.out.println("Score " + score + " saved to " + filePath);
            }

        } catch (IOException e) {
            log.Error("Error saving score: " + e.getMessage());
            MenuPanel.stop = true;
            e.printStackTrace();
        }
    }
    static int loadScore() {
        String path = System.getProperty("user.home") + "/AppData/Roaming/ResonantDebris";
        String filePath = path + "/save.bin";
        int score = 0;

        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
            score = dis.readInt();

            //System.out.println("Score loaded from " + filePath + ": " + score);
        } catch (FileNotFoundException e) {
            log.Error( "An Error was occured during Reading save:" + e.getMessage() + "Caused by FileNotFoundExpertion");
        } catch (IOException e) {
            log.Error( "An Error was occured during Reading save:" + e.getMessage() + "Caused by IOExpertion");
            return 0;

        }

        return score;
    }

}