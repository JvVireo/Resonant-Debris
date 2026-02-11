package sp.Panels;

import sp.Annotations.mod;
import sp.Mods.ClassMap;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import sp.Panels.MenuPanel;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static sp.ResonantDebris.frame;

public class ModPanel extends JPanel implements KeyListener {

    private static final String MODS_DIRECTORY = System.getenv("APPDATA") + "/ResonantDebris/mods";
    private Map<String, ModInfo> modMap = new HashMap<>();
    private JPanel modListPanel;
    private JButton exitButton;
    private final Color textColor = new Color(0x66FCF1);


    public void init() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        addKeyListener(this);
        JLabel titleLabel = new JLabel("Mod List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
       exitButton = new JButton("выйти");
        exitButton.setPreferredSize(new Dimension(150, 40));
        exitButton.setForeground(textColor);
        exitButton.setBackground(new Color(0x45A29E));
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.addActionListener(e -> {
                    close();
                });
                
        add(exitButton, BorderLayout.NORTH);
        modListPanel = new JPanel();
        modListPanel.setLayout(new BoxLayout(modListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(modListPanel);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        add(scrollPane, BorderLayout.CENTER);

        loadMods();
    }
    private void loadMods() {
        File modsDir = new File(MODS_DIRECTORY);
        if (!modsDir.exists()) {
            JOptionPane.showMessageDialog(this, "Папка модов не существует: " + MODS_DIRECTORY, "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        File[] modFiles = modsDir.listFiles(file -> file.isFile() && file.getName().endsWith(".jar"));
        if (modFiles == null || modFiles.length == 0) {
            JOptionPane.showMessageDialog(this, "Моды не найдены в каталоге: " + MODS_DIRECTORY, "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        modListPanel.removeAll();

        for (File modFile : modFiles) {
            try {
                ModInfo modInfo = getModInfo(modFile);
                if (modInfo == null) {
                    System.err.println("Не получилось получить информацию Mod для: " + modFile.getName());
                    JLabel errorLabel = new JLabel("Ошибка при загрузке: " + modFile.getName());
                    modListPanel.add(errorLabel);
                    modListPanel.add(Box.createVerticalStrut(5));
                    continue;
                }
                modMap.put(modFile.getName(), modInfo);

                JPanel modEntryPanel = createModEntryPanel(modInfo, modFile.getName());
                modListPanel.add(modEntryPanel);
                modListPanel.add(Box.createVerticalStrut(5)); // Небольшой пробел между модами
            } catch (Exception e) {
                System.err.println("Ошибка при получении информации Mod или создании записи о моде для " + modFile.getName() + ": " + e.getMessage());
                e.printStackTrace();
                JLabel errorLabel = new JLabel("Ошибка при загрузке: " + modFile.getName());
                modListPanel.add(errorLabel);
                modListPanel.add(Box.createVerticalStrut(5));
            }
        }

        modListPanel.revalidate();
        modListPanel.repaint();
    }

    private ModInfo getModInfo(File modFile) {
        try {
            JarFile jarFile = new JarFile(modFile);
            URLClassLoader classLoader = new URLClassLoader(new URL[]{modFile.toURI().toURL()}, ModPanel.class.getClassLoader());
            Class<?> modClass = null;

            //Ищу класс с аннотацией @Mod
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                    continue;
                }

                String className = entry.getName().substring(0, entry.getName().length() - 6).replace('/', '.'); // убираем .class и заменяем / на .
                try {
                    Class<?> loadedClass = classLoader.loadClass(className);
                    if (loadedClass.isAnnotationPresent(mod.class)) {
                        modClass = loadedClass;
                        break;  // Нашли класс мода
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("Не удалось загрузить класс: " + className + " в " + modFile.getName() + ": " + e.getMessage());
                }
            }

            if (modClass == null) {
                System.err.println("Не найден класс с аннотацией @Mod в: " + modFile.getName());
                return null;
            }

            // Получаем метаданные, вызвав метод getMetaData
            Method getMetaDataMethod = modClass.getMethod("getMetaData", File.class);
            Object metaData = getMetaDataMethod.invoke(null, modFile.getParentFile()); // Вызываем статический метод.  NULL значит static.

            //Получаем имя и описание из объекта metaData с помощью геттеров
            String modName = null;
            String description = null;
            try {
                Method getNameMethod = metaData.getClass().getMethod("getName");
                modName = (String) getNameMethod.invoke(metaData);
                Method getDescriptionMethod = metaData.getClass().getMethod("getDescription");
                description = (String) getDescriptionMethod.invoke(metaData);
            } catch (NoSuchMethodException | SecurityException e) {
                System.err.println("Метод getName или getDescription не найден в ModMetaData: " + e.getMessage());
                // Обязательно используйте какое-то значение по умолчанию, чтобы предотвратить ошибки при загрузке
                modName = modFile.getName();
                description = "Описание не найдено.";
            }

            classLoader.close(); // Закрывает classloader.  критически важно предотвратить утечку памяти

            return new ModInfo(modName, description, true);

        } catch (Exception e) {
            System.err.println("Ошибка при попытке получить информацию о моде из " + modFile.getName() + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private JPanel createModEntryPanel(ModInfo modInfo, String fileName) {
        JPanel modEntryPanel = new JPanel();
        modEntryPanel.setLayout(new BorderLayout());
        modEntryPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel nameLabel = new JLabel(modInfo.name);
        JTextArea descriptionArea = new JTextArea(modInfo.description);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);

        JCheckBox enabledCheckBox = new JCheckBox("Включён", modInfo.enabled);
        enabledCheckBox.addActionListener(e -> {
            modInfo.enabled = enabledCheckBox.isSelected();
            modMap.get(fileName).enabled = enabledCheckBox.isSelected(); // Обновление modInfo в карте
            System.out.println("Mod " + modInfo.name + " включено: " + modInfo.enabled);
            // @TODO: Добавьте логику, чтобы предотвратить загрузку мода при следующей загрузке игры.
        });

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.add(nameLabel, BorderLayout.NORTH);
        textPanel.add(descriptionArea, BorderLayout.CENTER);

        modEntryPanel.add(textPanel, BorderLayout.CENTER);
        modEntryPanel.add(enabledCheckBox, BorderLayout.EAST);

        return modEntryPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyUni = e.getKeyCode();
        if (keyUni == KeyEvent.VK_ESCAPE){
            close();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    public void close(){
        try {
            if (!MenuPanel.registeredSettings) {
                ClassMap.SettingsPanel.init();
                MenuPanel.registeredSettings = true;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка при запуске настроек: " + ex.getMessage(),
                    "Fatal Error",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(ex);
        }
        ClassMap.SettingsPanel.difficultyComboBox.setSelectedIndex(SettingsPanel.Difficulty - 1);
        frame.getContentPane().removeAll();
        frame.add(ClassMap.SettingsPanel);

        frame.revalidate();
        frame.repaint();
        ClassMap.SettingsPanel.requestFocusInWindow();
    }

    private static class ModInfo {
        String name;
        String description;
        boolean enabled;

        public ModInfo(String name, String description, boolean enabled) {
            this.name = name;
            this.description = description;
            this.enabled = enabled;
        }
    }}