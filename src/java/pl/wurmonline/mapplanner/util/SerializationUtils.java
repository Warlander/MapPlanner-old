package pl.wurmonline.mapplanner.util;

import java.lang.annotation.Annotation;
import pl.wurmonline.mapplanner.model.Identifier;

public class SerializationUtils {
    
    public static String getIdentifier(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Identifier) {
                return ((Identifier) annotation).value();
            }
        }
        return clazz.getSimpleName();
    }
    
}
