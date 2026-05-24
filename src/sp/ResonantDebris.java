package sp;

import NativeLibraries.log.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoField;


import sp.Mods.ClassMap;

import sp.Panels.LoadPanel;
//I thought and thought... and decided to give up modding - why do I need it?
//now, i need to fix the bag
public class ResonantDebris {

    public static LoadPanel panel;
    public static JFrame frame;
    public static JLayeredPane laureat = null;
    public static final String BUILD = "4";
    public static final boolean ANDROID = isProbablyAPhone();
    public static int Season = GetSeason();
    private final Color textColor = new Color(0x66FCF1);
    public static int GetSeason() {
        LocalDate currentDate = LocalDate.now();
        //(new) yeah(r!), bad
        if(currentDate.get(ChronoField.MONTH_OF_YEAR) == 12 || currentDate.get(ChronoField.MONTH_OF_YEAR) == 1 || currentDate.get(ChronoField.MONTH_OF_YEAR) == 2 ){
            return 1;
        }else{
            return 0;
        }

    }

    public static void main(String[] args) {
        try {
            log.Info("{java} Version: " + System.getProperty("java.version"));
            log.Info("{Resonant Debris} loaded Resonant Debris build " + BUILD);
            log.Warn("{Resonant Debris} ATTENTION! If you use a Java version lower than 21, the game will not work!");
            log.Debug(String.valueOf(ANDROID));

            SwingUtilities.invokeLater(() -> {
                try {
                    initializeUI();
                } catch (Exception e) {
                    log.Fatal("Critical error during UI initialization: " + e.getMessage());
                    JOptionPane.showMessageDialog(null,
                            "Ошибка при запуске игры: " + e.getMessage(),
                            "Fatal Error",
                            JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            });
        }catch (Throwable t){
            log.Error(t.getMessage());
        }
    }


    private static void initializeUI() {



        frame = new JFrame("Resonant Debris");


        loadIcon();

        log.Info(String.valueOf(frame.getContentPane()));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        //frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.BLACK);
        laureat = new JLayeredPane();
        laureat.setPreferredSize(new Dimension(1000, 1000));
        //frame.setContentPane(laureat);

        frame.add(ClassMap.LoadPanel);
        ClassMap.LoadPanel.init();
        frame.setVisible(true);

        /*this breaks all gamr
        panel.gameThread.start();
        panel.SpawnThread.start();*/
    }

    private static void loadIcon() {
        try {

            URL iconUrl = ResonantDebris.class.getResource("/assets/sprites/UI/icon.png");
            if (iconUrl != null) {
                BufferedImage image = ImageIO.read(iconUrl);
                frame.setIconImage(image);
            } else {

                File iconFile = new File("assets/sprites/UI/icon.png");
                if (iconFile.exists()) {
                    BufferedImage image = ImageIO.read(iconFile);
                    frame.setIconImage(image);
                } else {
                    log.Warn("Icon file not found at assets/sprites/UI/icon.png");
                }
            }
        } catch (IOException e) {
            log.Error("Failed to load icon: " + e.getMessage());
        }
    }
    public static boolean isProbablyAPhone() {
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");


        // Очень приблизительная и крайне ненадежная эвристика
        // Это может ошибочно определить другие устройства как телефоны
        if (osName != null && osName.toLowerCase().contains("android")) {
            if (osArch != null && (osArch.contains("arm") || osArch.contains("aarch64"))) {
                // Предположительно, Android на архитектуре ARM, что часто является телефоном, но не всегда.
                return true;
            }
        }

        return false;
    }
}
