package sp.Panels;

import javax.swing.*;
import java.awt.*;

public class PausePanel extends JPanel {
    private JButton statsButton = null;
    private JButton continueButton = null;  // Новая кнопка
    private JButton exitButton = null;
    private JLabel pointsLabel = null;
    private JLabel totalPointsLabel = null;



    // Интерфейс для обработки действий
    public interface PauseActionListener {
        void onContinue();
        void onExit();
    }

    private PauseActionListener listener = null;



    public PausePanel(GamePanel gpane, int currentPoints, int totalPoints, PauseActionListener listener) {
        setOpaque(false);
        this.listener = listener;
        setLayout(new GridBagLayout());


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Заголовок
        JLabel title = new JLabel("ПАУЗА");
        title.setForeground(new Color(0x66FCF1));
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy = 0;
        add(title, gbc);

        // Текущие очки
        pointsLabel = new JLabel("Текущие очки: " + currentPoints);
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        add(pointsLabel, gbc);

        // Общие очки
        totalPointsLabel = new JLabel("Всего очков: " + totalPoints);
        totalPointsLabel.setForeground(Color.WHITE);
        totalPointsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 2;
        add(totalPointsLabel, gbc);

        // Кнопка статистики
        statsButton = new JButton("Статистика");
        statsButton.setForeground(new Color(0x66FCF1));
        statsButton.setBackground(new Color(0x45A29E));
        statsButton.setFocusPainted(false);
        statsButton.setBorderPainted(false);
        statsButton.setFont(new Font("Arial", Font.BOLD, 16));
        statsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Текущая игра: " + currentPoints + " очков\n" +
                            "Всего заработано: " + totalPoints + " очков",
                    "Статистика", JOptionPane.INFORMATION_MESSAGE);
        });
        gbc.gridy = 3;
        add(statsButton, gbc);

        // Кнопка продолжения игры (новая)
        continueButton = new JButton("Продолжить игру");
        continueButton.setForeground(new Color(0x66FCF1));
        continueButton.setBackground(new Color(0x45A29E));
        continueButton.setFocusPainted(false);
        continueButton.setBorderPainted(false);
        continueButton.setFont(new Font("Arial", Font.BOLD, 16));
        continueButton.addActionListener(e -> this.listener.onContinue());
        gbc.gridy = 4;
        add(continueButton, gbc);

        // Кнопка выхода в меню
        exitButton = new JButton("Вернуться в меню");
        exitButton.setForeground(new Color(0x66FCF1));
        exitButton.setBackground(new Color(0x45A29E));
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.addActionListener(e -> this.listener.onExit());
        gbc.gridy = 5;
        add(exitButton, gbc);
    }
    public PausePanel() {}

}