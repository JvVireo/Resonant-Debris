package sp.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import NativeLibraries.SoundLibrary;
import NativeLibraries.log.*;
import sp.Mods.ClassMap;
import sp.ResonantDebris;

public class LoadPanel extends JPanel {

    private JPanel mainPanel;
    private JProgressBar progressBar;
    private JLabel progressLabel;
    private JTextArea statusTextArea;
    private Timer timer;
    private int progressValue = 0;
    private final Random random = new Random();
    private JPanel signalPanel; // Панель для графика сигнала
    private JPanel memoryGraphPanel; // Панель для графика потребления памяти



    public void init(){
        initComponents();
        startLoading();
        setVisible(true);
    }

    long pressStart = 0;
    private void initComponents() {
        SoundLibrary.playAudio("assets/SFX/load.wav", false);
        pressStart = System.currentTimeMillis();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(0x10, 0x10, 0x40));

        // 1. График сигнала
        signalPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawSignalGraph(g);
            }
        };
        signalPanel.setPreferredSize(new Dimension(1000, 200)); // Занимает верхнюю часть экрана
        signalPanel.setBackground(new Color(19, 19, 43)); // Тот же тёмно-синий фон
        mainPanel.add(signalPanel, BorderLayout.NORTH);

        // 2. Индикатор прогресса и текст
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(0x10, 0x10, 0x40)); // Тот же тёмно-синий фон
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressLabel = new JLabel("0%");
        progressLabel.setForeground(new Color(0x80, 0xB0, 0xFF));   // Светло-синий цвет
        progressLabel.setFont(new Font("Arial", Font.BOLD, 20));
        progressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(400, 40));
        progressBar.setForeground(new Color(0x50, 0x80, 0xFF));  // Более яркий синий
        progressBar.setBackground(new Color(0x30, 0x30, 0x70));  // Ещё более темный синий
        progressBar.setBorderPainted(false);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loadingText = new JLabel("Разшифровка электромагнитного сигнала");
        loadingText.setForeground(Color.WHITE);
        loadingText.setFont(new Font("Arial", Font.BOLD, 24));
        loadingText.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel warningText = new JLabel("Внимание! не выключайте устройство до полной разшифровки!");
        warningText.setForeground(Color.RED);
        warningText.setFont(new Font("Arial", Font.BOLD, 12));
        warningText.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel versionPanel = new JPanel();
        versionPanel.setBackground(new Color(0x30, 0x30, 0x70));
        JLabel versionLabel = new JLabel("Версия ПО: " + ResonantDebris.BUILD);
        versionLabel.setForeground(Color.WHITE);
        versionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        versionPanel.add(versionLabel);
        versionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);



        memoryGraphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMemoryGraph(g);
            }
        };
        memoryGraphPanel.setPreferredSize(new Dimension(400, 100));
        memoryGraphPanel.setBackground(new Color(0x20, 0x20, 0x60));
        memoryGraphPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue()); // Растяжка
        centerPanel.add(loadingText);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(progressLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        centerPanel.add(warningText);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(versionPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(memoryGraphPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 300)));
        centerPanel.add(progressBar);
        centerPanel.add(Box.createVerticalGlue()); // Растяжка

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // 3. Текстовое поле для этапов
        statusTextArea = new JTextArea();
        statusTextArea.setBackground(new Color(0x20, 0x20, 0x60));
        statusTextArea.setForeground(Color.WHITE);
        statusTextArea.setEditable(false);
        statusTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(statusTextArea);
        scrollPane.setPreferredSize(new Dimension(600, 700));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);
        // Добавляем слушатель изменения размера окна
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scaleComponents();
            }
        });
    }

    private void drawSignalGraph(Graphics g) {
        g.setColor(new Color(0x80, 0xB0, 0xFF)); // Светло-синий
        int[] signalData = generateSignalData();
        int width = signalPanel.getWidth();
        int height = signalPanel.getHeight();
        int dataPoints = signalData.length;

        for (int i = 0; i < dataPoints - 1; i++) {
            int x1 = i * width / dataPoints;
            int y1 = height - signalData[i];
            int x2 = (i + 1) * width / dataPoints;
            int y2 = height - signalData[i + 1];
            g.drawLine(x1, y1, x2, y2);
        }
    }

    private void drawMemoryGraph(Graphics g) {
        g.setColor(new Color(0x90, 0xEE, 0x90)); // Светло-зеленый
        int[] memoryData = generateMemoryData();
        int width = memoryGraphPanel.getWidth();
        int height = memoryGraphPanel.getHeight();
        int dataPoints = memoryData.length;

        for (int i = 0; i < dataPoints - 1; i++) {
            int x1 = i * width / dataPoints;
            int y1 = height - memoryData[i];
            int x2 = (i + 1) * width / dataPoints;
            int y2 = height - memoryData[i + 1];
            g.drawLine(x1, y1, x2, y2);
        }
    }

    private int[] generateSignalData() {
        int[] data = new int[100];
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt(80) + 20;  // Значения от 20 до 100 для высоты графика
        }
        return data;
    }

    private int[] generateMemoryData() {
        int[] data = new int[50];
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt(70) + 10; // Значения от 10 до 80 для высоты графика памяти
        }
        return data;
    }

    private void startLoading() {
        String[] loadingSteps = {
                "Инициализация JDK...",
                "Инициализация библиотек расшифровки...",
                "Фильтрация помех...",
                "Определение метода Decode();...",
                "Декомпиляция сигнала..."
        };

        timer = new Timer(80, new ActionListener() {
            int stepIndex = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                progressValue++;
                progressBar.setValue(progressValue);
                progressLabel.setText(progressValue + "%");
                signalPanel.repaint();

                if (progressValue % (100 / loadingSteps.length) == 0 && stepIndex < loadingSteps.length) {
                    statusTextArea.append(loadingSteps[stepIndex] + "\n");
                    stepIndex++;
                    memoryGraphPanel.repaint();
                    Thread thread = new Thread(() -> {
                        SoundLibrary.playAudio("assets/SFX/typo.wav",false);
                    });
                    thread.start();
                }

                if (progressValue >= 100) {
                    timer.stop();
                    statusTextArea.append("Сигнал декомпилирован успешно!\n");
                    startGame();

                }
            }
        });
        timer.start();
    }
    long finalTime = 0;
    long endTimer = 0;
    private void startGame() {
        long endTimer = System.currentTimeMillis();
         finalTime =  endTimer - pressStart;
         pressStart = endTimer = 0;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        SoundLibrary.stopAudio("assets/SFX/load.wav");

        ResonantDebris.frame.getContentPane().removeAll();
        ResonantDebris.frame.add(ClassMap.MenuPanel);
        ClassMap.MenuPanel.init();
        ResonantDebris.frame.revalidate();
        ResonantDebris.frame.repaint();
        ClassMap.MenuPanel.requestFocusInWindow();
        log.Debug("{Resonant Debris} time to load: " + finalTime);
    }
    private void scaleComponents() {
        // Получаем текущие размеры панели
        int width = this.getWidth();
        int height = this.getHeight();

        // Задаем новые размеры для компонентов
        signalPanel.setPreferredSize(new Dimension(width, (int)(height * 0.2)));
        memoryGraphPanel.setPreferredSize(new Dimension((int)(width * 0.4), (int)(height * 0.1)));
        progressBar.setPreferredSize(new Dimension((int)(width * 0.4), (int)(height * 0.04)));
        statusTextArea.setPreferredSize(new Dimension((int)(width * 0.6), (int)(height * 0.7)));

        // Обновляем интерфейс
        signalPanel.repaint();
        memoryGraphPanel.repaint();
        revalidate();
        repaint();
    }
}
