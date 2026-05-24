package sp.Mods;

import sp.Units.*;
import sp.Panels.*;

import java.util.ArrayList;


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

}
