package sp.Panels;

import NativeLibraries.log.log;
import sp.Units.*;
import sp.ResonantDebris;
import sp.Mods.ClassMap;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics;
import java.util.*;

import  NativeLibraries.SoundLibrary;


import static sp.ResonantDebris.frame;
import static sp.Panels.SettingsPanel.Difficulty;


public class GamePanel extends JPanel implements KeyListener, MouseListener {
    public static MenuPanel Mpanel;
    public static Player player;
    public static int points;
    private String message = "Points: " + points;
    private int playerX = frame.getWidth() / 2;
    private int playerY = 890;
    public ArrayList<Star> stars = new ArrayList<Star>();
    public static ArrayList<Asteroid> Asteroids = new ArrayList<Asteroid>();
    public static ArrayList<Scrap> scrap = new ArrayList<Scrap>();
    protected final Color textColor = new Color(0x66FCF1);
    private Timer gameTimer;
    private int SpawnTicks = 0;
    private int SpawnCoolDown = 0;
    private final st st = new st();
    private final ArrayList<Enemy> enemys = new ArrayList<Enemy>();
    public static ArrayList<bullet> bullets = new ArrayList<>();
    public ArrayList<bullet> NSbullets = bullets;
    private boolean isGameOver = false;
    private static boolean gameRunning = true;
    public boolean inited = false;


    public void reset() {
        points = 0;
        message = "Points: " + points;
        player.x = playerX;
        player.y = playerY;
        stars.clear();
        Asteroids.clear();
        scrap.clear();
        //enemys.clear();
        isGameOver = false;
        gameRunning = true;
    }
    @Override
    public  void paintComponent(Graphics g) {
        if (gameRunning) {
            super.paintComponent(g);
            setBackground(new Color(0x0B0C15));


            player.draw(g);

            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString(message, 50, 50);
            for (int i = 0; i < stars.size(); i++) {
                Star DStar = stars.get(i);
                Random random = new Random();
                int colorChoice = random.nextInt(3);
                Color color = null;
                switch (colorChoice) {
                    case 0:

                        color = new Color(255, 255, 255);
                        break;
                    case 1:

                        int blueComponent = random.nextInt(64);
                        color = new Color(171, 179, 255);
                        break;
                    case 2:

                        break;
                    default:
                        break;
                }
                g.setColor(color);
                g.drawRect(DStar.x, DStar.y, DStar.size, DStar.size);
                if (colorChoice == 2) {
                    g.setColor(new Color(255, 180, 154));
                    g.drawRect(DStar.x, DStar.y, DStar.size + 1, DStar.size + 1);
                }
            }
            if (MenuPanel.Arr) {
                for (Asteroid DAsteroid : Asteroids) {
                    DAsteroid.draw(g);
                }
                for (Scrap DScrap : scrap) {
                    DScrap.draw(g);
                }
                for (bullet Dbullet : bullets) {
                    Dbullet.draw(g);
                }

                for (Enemy enemy : enemys) {
                    enemy.draw(g);
                }
            }
        }
    }
    public void init(){
        if(!inited) {
            setBounds(0, 0, frame.getWidth(), frame.getHeight());
            log.Info("Width: " + this.getWidth() + ", Height: " + this.getHeight());


            setFocusable(true);
            addKeyListener(this);
            addMouseListener(this);
        }
        player = (Player) CreatePlayer(frame.getWidth() / 2,frame.getHeight() - 128);
        //Enemy.add(playerX,100,enemys);

        this.requestFocusInWindow();
        SoundLibrary.stopAudio("assets/music/Space.wav");
        SoundLibrary.playAudio("assets/music/ElectricDebris.wav", 0.8f,true);


        switch (Difficulty) {
            case 0, 1:
                SpawnCoolDown = 9;
                break;
            case 2:
                SpawnCoolDown = 6;
                break;
            case 3:
                SpawnCoolDown = 3;
                break;
            case 4:
                SpawnCoolDown = 1;
                break;
        }

        repaint();
        System.out.println(SpawnCoolDown);
        gameTimer = new Timer(200, e -> {
            if(gameRunning) {
                SwingUtilities.invokeLater(() -> {
                    this.Update();
                    for (Asteroid asteroid : new ArrayList<>(Asteroids)) {
                        asteroid.AI();
                    }

                    for (Scrap scrapItem : new ArrayList<>(scrap)) {
                        scrapItem.AI();
                    }
                    for(Enemy enemy : enemys){
                        enemy.AI();
                    }
                    for(bullet bullet : bullets){
                        bullet.BAI();
                    }
                    SpawnTicks++;


                    if (SpawnTicks >= SpawnCoolDown) {
                        Random r = new Random();
                        int Sch = r.nextInt(5);
                        if (Sch == 1) {

                            int ech = r.nextInt(5);
                            if (ech == 1){
                              Enemy.add(r.nextInt(1001),0, enemys);
                            }else{
                                Scrap.add(r.nextInt(1001), 0, scrap);
                            }
                        } else {
                            Asteroid.add(r.nextInt(1001), 0, Asteroids);
                        }


                        SpawnTicks = 0;
                    }
                });
            }
        });
        gameTimer.start();
        inited = true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      if (e.getButton() == e.BUTTON1){
          player.shoot();

      }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public class st extends StopManager{
        @Override
        public void act(stopWariants sw) {
             if(isGameOver){
                 sw.GameOver();
             }else{
                 sw.Stop();
             }

        }


    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        int keyUni = e.getKeyCode();
        //System.out.println("keyPress!:" + player.x + "," + player.y);
        SwingUtilities.invokeLater(() -> {
        if (keyUni == KeyEvent.VK_A) {
            player.moveLeft();
        }

        if (keyUni == KeyEvent.VK_D){
            player.moveRight();
        }

        if (keyUni == KeyEvent.VK_W) {
            player.moveUp();
        }

        if (keyUni == KeyEvent.VK_S) {
            player.moveDown();
        }
        });
        repaint();

        if (keyUni == KeyEvent.VK_ESCAPE) {
            if (gameRunning) {

                gameRunning = false;
                SoundLibrary.stopAudio("assets/music/ElectricDebris.wav");


                PausePanel pausePanel = new PausePanel(this, points, MenuPanel.Gpoints, new PausePanel.PauseActionListener(){

                    @Override
                    public void onContinue() {
                        returnPanel();
                        gameRunning = true;
                        SoundLibrary.playAudio("assets/music/ElectricDebris.wav",0.8f, true);
                        repaint();
                    }

                    @Override
                    public void onExit() {
                         st.exec();
                    }
                });
                pausePanel.requestFocusInWindow();
                pausePanel.setBounds(0,0,frame.getWidth(),frame.getHeight());
                pausePanel.setOpaque(false);

                ResonantDebris.laureat.add(pausePanel,JLayeredPane.PALETTE_LAYER);
                ResonantDebris.laureat.revalidate();
                ResonantDebris.laureat.repaint();
                //pausePanel.requestFocusInWindow();
            } else {

                gameRunning = true;
                SoundLibrary.playAudio("assets/music/ElectricDebris.wav",0.8f, true);


                ResonantDebris.frame.getContentPane().removeAll();
                ResonantDebris.frame.add(this);
                ResonantDebris.frame.revalidate();
                ResonantDebris.frame.repaint();
                requestFocusInWindow();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public boolean checkPlayerDeath() {
        Rectangle playerHitbox = player.getHitbox();

        for (Asteroid asteroid : Asteroids) {
            if (playerHitbox.intersects(asteroid.getHitbox())) {
                return true;
            }
            for (bullet bullet : bullets) {
                if (playerHitbox.intersects(bullet.getHitbox()) && bullet.shooter != player) {
                    return true;
                }
            }
        }
        return false;
    }

    public long checkPlayerAndScrapCollision() {
        Rectangle playerHitbox = player.getHitbox();
        for (Scrap scrap1 : scrap) {
            if (playerHitbox.intersects(scrap1.getHitbox())) {
                return scrap1.id;
            }
        }
        return -1;
    }
    public void Collisions() {
        if (!isGameOver) {
            long ps = checkPlayerAndScrapCollision();

            if (ps != -1) {
                for (int asi = 0; asi < scrap.size(); asi++) {

                    if (scrap.get(asi).id == ps) {
                        points = points + 25 * Difficulty;
                        message = "Points: " + points;
                        scrap.remove(asi);
                        repaint();
                        break;
                    }
                }
            }
            boolean dead = checkPlayerDeath();
            if (dead) {
                isGameOver = true;
                st.exec();
            }
            Iterator<Enemy> enemyIterator = enemys.iterator();
            while (enemyIterator.hasNext()) {
                Enemy en = enemyIterator.next();
                Rectangle enemyHitbox = en.getHitbox();

                Iterator<bullet> bulletIterator = bullets.iterator(); // итерация для Bullets
                while(bulletIterator.hasNext()){
                    bullet bull = bulletIterator.next();
                    if (enemyHitbox.intersects(bull.getHitbox()) && bull.shooter != en) {
                        Scrap.add(en.x, en.y, scrap);
                        enemyIterator.remove(); // Используем iterator.remove()
                        // Важно: здесь нужно прервать внутренний цикл, если враг уничтожен.
                        //  Иначе можно получить ConcurrentModificationException при следующей итерации bullet.
                        bulletIterator = bullets.iterator(); //Сбрасываем итератор bullets для избежания пропуска
                        break; //Выходим из цикла bullets, так как враг уже удален
                    }
                }
            }
        }
    }

    public void Update() {
        /*if (!gameRunning) {

            SoundLibrary.stopMusic();
            gameTimer.stop();
            gameTimer = null;

            isGameOver = true;
            gameRunning = false;



            ResonantDebris.panel.Mpanel.Gpanel = null;


            MenuPanel panel = new MenuPanel();


            frame.getContentPane().removeAll();
            frame.add(panel);
            frame.revalidate();
            frame.repaint();


            panel.requestFocusInWindow();

            reset();
        }*/

        if (playerX - 48 > this.getWidth()){
            playerX = this.getWidth() - 48;
        }
        if (playerY - 64 > this.getHeight()){
            playerY = this.getHeight() - 64;
        }

        Random random = new Random();
        Collisions();
        stars.clear();
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(frame.getWidth());
            int y = random.nextInt(frame.getHeight());


            if (random.nextDouble() < 0.05) {
                int clusterX = x + random.nextInt(20) - 10;
                int clusterY = y + random.nextInt(20) - 10;
                x = Math.max(0, Math.min(clusterX, 1000));
                y = Math.max(0, Math.min(clusterY, 1000));
            }

            int size = random.nextInt(3) + 1;
            stars.add(new Star(x, y, size));
            repaint();
        }
    }


    public void returnPanel(){
        ResonantDebris.frame.getContentPane().removeAll();
        ResonantDebris.frame.add(ClassMap.GamePanel);
        ResonantDebris.frame.revalidate();
        ResonantDebris.frame.repaint();
        requestFocusInWindow();
    }

    public static boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean running) {
        gameRunning = running;
    }
    public interface stopWariants{
        void Stop();
        void GameOver();
    }
    public abstract class StopManager{
        public abstract void act(stopWariants sw);
        public final void exec(){
            act(new stopWariants() {
                @Override
                public void Stop() {
                    SoundLibrary.stopAudio("assets/music/ElectricDebris.wav");

                    MenuPanel.Gpanel = null;


                    gameTimer.stop();
                    gameTimer = null;

                    MenuPanel.Gpoints = MenuPanel.Gpoints + points;

                    gameRunning = false;



                    SwingUtilities.invokeLater(() -> {

                        frame.getContentPane().removeAll();

                        frame.getContentPane().add(ClassMap.MenuPanel);
                        ClassMap.MenuPanel.init();
                        //frame.setSize(800, 600);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    });
                    reset();
                }

                @Override
                public void GameOver() {
                    SoundLibrary.stopAudio("assets/music/ElectricDebris.wav");

                    MenuPanel.Gpanel = null;


                    gameTimer.stop();
                    gameTimer = null;

                    MenuPanel.Gpoints = MenuPanel.Gpoints + points;
                    gameRunning = false;
                    isGameOver = true;


                    SwingUtilities.invokeLater(() -> {

                        frame.getContentPane().removeAll();
                        GameOverPanel panel = new GameOverPanel();
                        frame.getContentPane().add(panel);

                        //frame.setSize(800, 600);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                        isGameOver = false;
                    });
                    reset();
                }
            });
        }
    }
    public Unit CreatePlayer(int sx,int sy){
        return new Player(sx,sy,0);
    }

    public void setIsGameRunning(boolean go){
        gameRunning = go;
    }
}