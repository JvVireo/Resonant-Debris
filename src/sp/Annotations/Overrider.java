package sp.Annotations;

import java.lang.annotation.*;

/**indicates the mod is a special mod that can't add new classes(only override)
 * @see java.lang.annotation.Annotation
 * @see mod
 * @see sp.Mods.Mod**/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Overrider {
}