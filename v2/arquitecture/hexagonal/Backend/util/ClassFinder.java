package [packageName].util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class ClassFinder {
    public static Class<?> findClassBySimpleName(String simpleName) {
        try (ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .scan()) {
            return scanResult.getAllClasses().stream()
                    .filter(ci -> ci.getSimpleName().equals(simpleName))
                    .findFirst()
                    .map(ci -> {
                        try {
                            return Class.forName(ci.getName());
                        } catch (ClassNotFoundException e) {
                            return null;
                        }
                    })
                    .orElse(null);
        }
    }

}
