package sp.Panels;

import sp.Mods.ClassMap;
import sp.Units.Star;
import NativeLibraries.log.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

import static sp.ResonantDebris.frame;
//TODO i want to make "panels" package BUT_IM_LAZY(and classLoader problems)
public class SettingsPanel extends JPanel implements KeyListener {

    public JComboBox<String> difficultyComboBox;
    private JButton backButton = null;
    private JButton modsButton = null; // Кнопка "Моды"
    private  JLabel titleLabel = null;
    private final Font titleFont = new Font("Arial", Font.BOLD, 48);
    private final Font buttonFont = new Font("Arial", Font.BOLD, 16);
    private final Color textColor = new Color(0x66FCF1);
    private final ArrayList<Star> stars = new ArrayList<>();
    private final SDraw drawComponent = new SDraw();
    private final JTextArea logArea = new JTextArea();
    private final JScrollPane logScrollPane = new JScrollPane(logArea);
    private boolean logVisible = false;
    private Timer logUpdateTimer = null;
    public static int Difficulty = 1;

    public void init(){
        setLayout(new GridBagLayout());
        setBackground(new Color(0x0F131D));
        setSize(frame.getWidth(), frame.getHeight());
        setFocusable(true);
        addKeyListener(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.NONE;

        titleLabel = new JLabel("Настройки");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(textColor);
        add(titleLabel, gbc);

        JLabel difficultyLabel = new JLabel("Сложность:");
        difficultyLabel.setForeground(textColor);
        add(difficultyLabel, gbc);

        String[] difficulties = {"Легкая", "Средняя", "Сложная", "АДСКАЯ"};
        difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setPreferredSize(new Dimension(150, 30));
        difficultyComboBox.addActionListener(e -> {
            String selectedOption = (String) difficultyComboBox.getSelectedItem();
            switch (selectedOption){
                case "Лёгкая":
                    Difficulty = 1;
                    break;
                case "Средняя":
                    Difficulty = 2;
                    break;
                case "Сложная":
                    Difficulty = 3;
                    break;
                case "АДСКАЯ":
                    Difficulty = 4;
                    break;
            }
        });
        add(difficultyComboBox, gbc);

        // Кнопка "Моды"
        modsButton = new JButton("Моды");
        modsButton.setFont(buttonFont);
        modsButton.setForeground(textColor);
        modsButton.setBackground(new Color(0x45A29E));
        modsButton.setFocusPainted(false);
        modsButton.setBorderPainted(false);
        modsButton.addActionListener(e -> {
            // Открываем панель модов (замените ModPanel на вашу реализацию)
           frame.getContentPane().removeAll();
            frame.add(ClassMap.ModPanel);

            ClassMap.ModPanel.init();
            frame.revalidate();
            frame.repaint();
            ClassMap.ModPanel.requestFocusInWindow();

        });
        add(modsButton, gbc);

        backButton = new JButton("Назад");
        backButton.setFont(buttonFont);
        backButton.setForeground(textColor);
        backButton.setBackground(new Color(0x45A29E));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {

            frame.getContentPane().removeAll();
            frame.add(ClassMap.MenuPanel);
            frame.revalidate();
            frame.repaint();
            ClassMap.MenuPanel.requestFocusInWindow();
        });
        add(backButton, gbc);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(drawComponent, gbc);

        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        logArea.setBackground(new Color(0x222831));
        logArea.setForeground(textColor);
        logArea.setEditable(false);


        logArea.setLineWrap(false);
        logArea.setWrapStyleWord(false);
        logScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        logScrollPane.setPreferredSize(new Dimension(500, 600));
        logScrollPane.setVisible(false);
        add(logScrollPane, gbc);

        for (int i = 0; i < 100; i++) {
            stars.add(new Star((int) (Math.random() * frame.getWidth()), (int) (Math.random() * frame.getHeight()), (int) (Math.random() * 5) + 1));
        }

        drawComponent.repaint();

        logUpdateTimer = new Timer(1000, e -> {
            if (logVisible) {
                updateLogArea();
            }
        });
    }



    class SDraw extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(new Color(0x0C0C17));

            for (Star star : stars) {
                g.setColor(Color.WHITE);
                g.fillRect(star.x, star.y, star.size, star.size);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            backButton.doClick();
        } else if (e.isAltDown() && e.getKeyCode() == KeyEvent.VK_L) {
            toggleLogs();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void toggleLogs() {
        logVisible = !logVisible;
        logScrollPane.setVisible(logVisible);

        if (logVisible) {
            updateLogArea();
            logUpdateTimer.start();
        } else {
            logUpdateTimer.stop();
        }

        revalidate();
        repaint();
    }

    private void updateLogArea() {


        String logText = "Логи игры:\n" +
                log.ToString();

        logArea.setText(logText);
        logArea.setCaretPosition(0);
    }
}