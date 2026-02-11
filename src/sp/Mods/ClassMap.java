package sp.Mods;

import sp.Units.*;
import sp.Panels.*;


/**
 * Provides classMap for all classes in the game
 * <p>needed for overriding classes by mods
 * @since build 4
 */
public class ClassMap {
    public static MenuPanel MenuPanel = new MenuPanel();
    public static GamePanel GamePanel = new GamePanel();
    public static PausePanel PausePanel = new PausePanel();
    public static GameOverPanel GameOverPanel = new GameOverPanel(1);
    public static SettingsPanel SettingsPanel = new SettingsPanel();
    public static LoadPanel LoadPanel = new LoadPanel();
    public static ModPanel ModPanel = new ModPanel();
    public static Unit Player =  new Player(),
    Enemy = new Enemy(),
    Scrap = new Scrap(),
    Asteroid = new Asteroid();
    public static bullet Bullet = new bullet();
    public static Star Star = new Star();

}