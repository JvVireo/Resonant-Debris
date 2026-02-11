package sp.Units;

import NativeLibraries.log.log;
import sp.Panels.GamePanel;
import sp.ResonantDebris;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * class <code>Unit</code> represents a basic unit that can move.
 * <p>
 * it's base to all units in Resonant Debris
 * @since build 4
 *
 */
public class Unit {

    public int x;
    public int y;
    protected String imageFile;
    protected BufferedImage image;
    private Rectangle hitbox = null; //  Хитбокс игрока
    private int width;  //  Ширина спрайта/хитбокса
    private int height; //  Высота спрайта/хитбокса
    public long id = 0;

    protected Unit(int startX, int startY, long id, String imageFile) {
        this.x = startX;
        this.y = startY;
        this.id = id;
        InputStream inputStream = null;
        try {
            if(ResonantDebris.GetSeason() == 1){
                imageFile = "assets/sprites/Units/NYUnits/" + imageFile;
            }else{
                imageFile = "assets/sprites/Units/" + imageFile;
            }
            //imageFile = "assets/sprites/enemy.png";
            inputStream = GamePanel.class.getClassLoader().getResourceAsStream(imageFile);
            image = ImageIO.read(inputStream);

            //  Поместите изображение player.png в папку assets
            width = 16;   //  Получаем ширину изображения
            height = 32; //  Получаем высоту изображения
        } catch (IOException e) {
            //log.Error("Не удалось загрузить изображение игрока!");
            image = null; //  или создайте резервное изображение
            width = 32;  //  Дефолтная ширина, если изображение не загружено
            height = 32; //  Дефолтная высота, если изображение не загружено
        } finally {
            // Гарантированно закрываем поток в блоке finally
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.Error("Ошибка при закрытии потока: " + e.getMessage());
            }
        }
        int Hix = x + 24;
        int Hiy = y + 16;
        hitbox = new Rectangle(Hix, Hiy, width, height); //  Создаем хитбокс
    }
    public Unit(){}
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, null);
        } else {
            g.setColor(Color.red);
            g.fillRect(x, y, width, height); //  Если изображение не загружено, рисуем синий квадрат
        }


        //g.setColor(Color.RED);
        //g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }


    public void moveLeft() {
        x = x - 10;
        if (x < 0) {
            x = 0;

        }
        updateHitbox();
    }

    public void moveRight() {
        x = x + 10;

        updateHitbox();
    }

    public void moveUp() {
        y = y - 10;
        if (y < 0) {
            y = 0;

        }
        updateHitbox();
    }

    public void moveDown() {
        y = y + 10;

        updateHitbox();
    }

    //  Метод для обновления позиции хитбокса
    protected void updateHitbox() {
        int Hix = x + 24;
        int Hiy = y + 16;
        hitbox.x = Hix;
        hitbox.y = Hiy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}