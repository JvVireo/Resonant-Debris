package sp.Annotations;

import java.lang.annotation.*;

/**indicates the main class of mod is actually mod
 * @see java.lang.annotation.Annotation
 * @see  sp.Mods.Mod**/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface mod {
}