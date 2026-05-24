package sp.Panels;

import NativeLibraries.SoundLibrary;
import sp.Mods.ClassMap;
import sp.ResonantDebris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static sp.ResonantDebris.frame;

public class GameOverPanel extends JPanel {

    private float alpha = 0.7f;
    private  JLabel titleLabel = null;
    private  JTextArea gameOverText = null;
    private  JButton restartButton = null ;
    private  JButton menuButton = null;
    private  Timer textTimer = null;
    boolean fadeIn = true;
    private MenuPanel Mpanel = null;
    private Color textColor = new Color(0, 1.0f, 1.0f, alpha);
    private Color shadowColor = new Color(0, 0.7f, 0.7f, 0.8f);

    public GameOverPanel() {
        setSize(new Dimension(frame.getWidth(),frame.getHeight()));
        setBounds(0,-30,frame.getWidth(),frame.getHeight());
        setLayout(new BorderLayout());
        setBackground(new Color(0x0F131D));
        SoundLibrary.playAudio("assets/SFX/gameOver.wav",false);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        titleLabel = new JLabel("<<<</ВЫ ПРОИГРАЛИ\\>>>>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 1.0f, 1.0f, 1.0f));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 100)));

        gameOverText = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                FontMetrics fm = g.getFontMetrics();
                String text = getText();
                int lineHeight = fm.getHeight();
                int width = getWidth();

                List<String> lines = new ArrayList<>();

                String[] paragraphs = text.split("\\n");
                for (String paragraph : paragraphs) {
                    StringBuilder currentLine = new StringBuilder();
                    // Разделяем параграф на слова и формируем строки
                    for (String word : paragraph.split("\\s+")) {
                        if (fm.stringWidth(currentLine + word) < width) {
                            currentLine.append(word).append(" ");
                        } else {
                            lines.add(currentLine.toString().trim());
                            currentLine = new StringBuilder(word).append(" ");
                        }
                    }
                    lines.add(currentLine.toString().trim());
                }

                int y = fm.getAscent();

                g2d.setColor(shadowColor);
                for (String line : lines) {
                    g2d.drawString(line, 2, 2 + y);
                    g2d.drawString(line, 1, 1 + y);
                    y += lineHeight;
                }

                y = fm.getAscent();
                g2d.setColor(textColor);
                for (String line : lines) {
                    g2d.drawString(line, 0, y);
                    y += lineHeight;
                }
            }
        };

        gameOverText.setText("Ваш корабль разлетелся на части, став частью космического мусора. Но вы хотя бы насобирали немного металлолома перед тем, как разлететься в щепки об один из тысячи астероидов..." +
                "\n                            конечно досадно, когда от вас остаётся одни обломки, спрятавшиеся в бесконечных просторах космоса\n \nДанные которые были получены с корабля перед столкновением: \n очков собрано за данный полёт: " + GamePanel.points + "\n очков собрано всего: " + MenuPanel.Gpoints);
        gameOverText.setPreferredSize(new Dimension(400, 400));
        gameOverText.setLineWrap(true);
        gameOverText.setWrapStyleWord(true);
        gameOverText.setOpaque(false);
        gameOverText.setEditable(false);
        gameOverText.setFont(new Font("Arial", Font.ITALIC, 16));
        gameOverText.setForeground(textColor);
        gameOverText.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(gameOverText);

        add(mainPanel, BorderLayout.CENTER);

        Thread musicThread = new Thread(() -> {

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            SoundLibrary.stopAudio("assets/SFX/gameOver.wav");
            SoundLibrary.playAudio("assets/music/memories.wav", 0.3F,true);
        });
        restartButton = new JButton("Начать заново");
        restartButton.setPreferredSize(new Dimension(170, 40));
        restartButton.setForeground(textColor);
        restartButton.setBackground(new Color(0x45A29E));
        restartButton.setFocusPainted(false);
        restartButton.setBorderPainted(false);
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        menuButton = new JButton("Вернуться в меню");
        menuButton.setPreferredSize(new Dimension(200, 40));
        menuButton.setForeground(textColor);
        menuButton.setBackground(new Color(0x45A29E));
        menuButton.setFocusPainted(false);
        menuButton.setBorderPainted(false);
        menuButton.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(restartButton);
        buttonPanel.add(menuButton);
        buttonPanel.setOpaque(false);
        add(buttonPanel, BorderLayout.SOUTH);

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundLibrary.stopAudio("assets/music/memories.wav");
                musicThread.interrupt();
                ClassMap.MenuPanel.startGame();
            }
        });

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             SoundLibrary.stopAudio("assets/music/memories.wav");
                musicThread.interrupt();

                frame.getContentPane().removeAll();
                frame.add(ClassMap.MenuPanel);
                frame.revalidate();
                frame.repaint();
             SoundLibrary.playAudio("assets/music/Space.wav");
                ClassMap.MenuPanel.requestFocusInWindow();
            }
        });

        textTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fadeIn) {
                    alpha += 0.02f;
                    if (alpha >= 0.9f) {
                        alpha = 0.9f;
                        fadeIn = false;
                    }
                } else {
                    alpha -= 0.02f;
                    if (alpha <= 0.5f) {
                        alpha = 0.5f;
                        fadeIn = true;
                    }
                }
                textColor = new Color(0, 1.0f, 1.0f, alpha);
                shadowColor = new Color(0, 0f, 0.7f, alpha * 0.8f);

                gameOverText.repaint();
            }
        });
        textTimer.start();
        repaint();
        musicThread.start();
    }

    public GameOverPanel(int i){}

    public JButton getRestartButton() {
        return restartButton;
    }

    public JButton getMenuButton() {
        return menuButton;
    }

}