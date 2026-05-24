package sp.Panels;

import NativeLibraries.DataLibrary;
import NativeLibraries.SoundLibrary;


import NativeLibraries.log.log;
import sp.Mods.ClassMap;
import sp.Units.Star;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.FlowLayout;
import java.util.ArrayList;

import java.util.Random;


import static sp.ResonantDebris.frame;
import static sp.ResonantDebris.laureat;

public final class MenuPanel extends JPanel {


    public static boolean stop;
    private Clip clip;
    private JButton playButton;
    private JButton exitButton;
    private JButton settingsButton;
    private ArrayList<Star> stars = new ArrayList<>();
    private final String gameTitle = "Resonant Debris";
    private final Font titleFont = new Font("Arial", Font.BOLD, 48);
    private final Font textFont = new Font("Arial", Font.BOLD, 16);
    private final Font buildFont = new Font("Arial", Font.BOLD, 8);
    private final Color textColor = new Color(0x66FCF1);

    public static boolean Arr = true;
    public static final int TICK = 200;
    public static boolean flowWork = false;

    public static int Gpoints = DataLibrary.loadScore();

    public static GamePanel Gpanel = null;
    private boolean registered;
    public static boolean registeredSettings = false;

    public MenuPanel() {
    }
    public void init(){
        setSize(frame.getWidth(), frame.getHeight());
        setLayout(new BorderLayout());
        setBackground(new Color(0x0F131D));
        setBounds(0,-30,frame.getWidth(),frame.getHeight());
        Random random = new Random();
        if (stars.isEmpty()) {
            for (int i = 0; i < 100; i++) {
                int x = random.nextInt(frame.getWidth());
                int y = random.nextInt(frame.getHeight());
                int size = random.nextInt(3) + 1;
                Color color = new Color(200, 200, 255);
                stars.add(new Star(x, y, size));
            }
        }

        if (!registered) {
            playButton = new JButton("Играть");
            playButton.setPreferredSize(new Dimension(150, 40));
            playButton.setForeground(textColor);
            playButton.setBackground(new Color(0x000000));
            playButton.setFocusPainted(false);
            playButton.setBorderPainted(false);
            playButton.setFont(new Font("Arial", Font.BOLD, 16));

            exitButton = new JButton("Выход");
            exitButton.setPreferredSize(new Dimension(150, 40));
            exitButton.setForeground(textColor);
            exitButton.setBackground(new Color(0x45A29E));
            exitButton.setFocusPainted(false);
            exitButton.setBorderPainted(false);
            exitButton.setFont(new Font("Arial", Font.BOLD, 16));

            settingsButton = new JButton("Настройки");
            settingsButton.setPreferredSize(new Dimension(150, 40));
            settingsButton.setForeground(textColor);
            settingsButton.setBackground(new Color(0x45A29E));
            settingsButton.setFocusPainted(false);
            settingsButton.setBorderPainted(false);
            settingsButton.setFont(new Font("Arial", Font.BOLD, 16));

            playButton.addActionListener(_ -> startGame());

            exitButton.addActionListener(_ -> {
                DataLibrary.saveScore(Gpoints);
                if (stop) {
                    return;
                }

                System.exit(0);
            });

            settingsButton.addActionListener(_ -> openSettings());

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setOpaque(false);
            buttonPanel.add(playButton);
            buttonPanel.add(settingsButton);
            buttonPanel.add(exitButton);
            //buttonPanel.setBounds(0,0,frame.getWidth(),frame.getHeight());
            add(buttonPanel, BorderLayout.SOUTH);
            registered = true;
        }
        SoundLibrary.playAudio("assets/music/Space.wav", true);
        repaint();
    }

      public void paintComponent(Graphics g){
              super.paintComponent(g);
              setBackground(new Color(0x0C0C17));

              for (Star star : stars) {
                  g.setColor(Color.WHITE);
                  g.fillRect(star.x, star.y, star.size, star.size);
              }

              g.setFont(titleFont);
              g.setColor(textColor);
              FontMetrics fm = g.getFontMetrics();
              if (fm != null) {
                  int titleWidth = fm.stringWidth(gameTitle);
                  int x = (getWidth() - titleWidth) / 2;
                  int y = getHeight() / 4;
                  g.drawString(gameTitle, x, y);
              } else {

                  System.err.println("Ошибка: FontMetrics равен null для titleFont.");
                  g.drawString(gameTitle, getWidth() / 2, getHeight() / 4);
              }

          }

    public  void startGame() {
        try {
            SwingUtilities.invokeLater(() -> {

                frame.setContentPane(laureat);
                ClassMap.GamePanel.setBounds(0, 0, frame.getWidth() - 16, frame.getHeight() - 16);
                laureat.add(ClassMap.GamePanel, JLayeredPane.DEFAULT_LAYER);

                frame.getContentPane().removeAll();
                frame.add(ClassMap.GamePanel);
                ClassMap.GamePanel.init();

                frame.revalidate();
                frame.repaint();
                ClassMap.GamePanel.requestFocusInWindow();
                ClassMap.GamePanel.setIsGameRunning(true);


            });
        }catch (Throwable e) {

            JOptionPane.showMessageDialog(this,
                    "Ошибка при запуске настроек: " + e.getMessage(),
                    "Fatal Error",
                    JOptionPane.ERROR_MESSAGE);
            //throw new RuntimeException(e);


        }
    }

    private void openSettings() {

        try {
            if (!registeredSettings) {
                ClassMap.SettingsPanel.init();
              registeredSettings = true;
            }
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка при запуске настроек: " + e.getMessage(),
                    "Fatal Error",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
        ClassMap.SettingsPanel.setSize(frame.getWidth(), frame.getHeight());
        ClassMap.SettingsPanel.difficultyComboBox.setSelectedIndex(SettingsPanel.Difficulty - 1);
        frame.getContentPane().removeAll();
        frame.add(ClassMap.SettingsPanel);

        frame.revalidate();
        frame.repaint();
        ClassMap.SettingsPanel.requestFocusInWindow();
    }}