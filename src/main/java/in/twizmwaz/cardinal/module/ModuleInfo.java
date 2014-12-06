package in.twizmwaz.cardinal.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kevin on 11/28/14.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {

    public abstract String name();

    public abstract Class<? extends Module>[] dependency() default {};

    public abstract Class<?extends ModuleBuilder> builder();

    public boolean multiple() default true;


}
