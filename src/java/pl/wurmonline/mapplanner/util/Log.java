package pl.wurmonline.mapplanner.util;

import java.time.Instant;

public class Log {
    
    public static void info(Object object, String message) {
        info(object, object.getClass(), message);
    }
    
    public static void info(Class clazz, String message) {
        info(null, clazz, message);
    }
    
    private static void info(Object object, Class clazz, String message) {
        StringBuilder build = new StringBuilder();
        build.append("[").append(Instant.now().toString()).append("] ");
        build.append("{").append(clazz.getSimpleName()).append("} ");
        if (object != null) {
            build.append("(").append(object.toString()).append(") ");
        }
        build.append(message);
        
        System.out.println(build.toString());
    }
    
    public static void error(Exception ex) {
        ex.printStackTrace(System.err);
    }
    
}
