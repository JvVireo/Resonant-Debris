package sp.Annotations;

import java.lang.annotation.*;

/** this annotation is used to mark main class of mod*/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Main{}