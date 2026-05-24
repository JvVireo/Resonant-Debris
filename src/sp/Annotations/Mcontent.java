package sp.Annotations;

import java.lang.annotation.*;

/**
 * this annotation is used to mark the content of the mod.
 * This is also used to mark type of the content.
 * <br>it has four types:
 * <br><code>player</code>
 * <br><code>asteroid</code>
 * <br><code>enemy</code>
 * <br><code>scrap</code>
 * <br>mod is mustn't have more than 1 player type content.
 * @apiNote all content must extend his type and have "modded" setted on true
 * */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mcontent{
    ContentTypes ContentType();
}