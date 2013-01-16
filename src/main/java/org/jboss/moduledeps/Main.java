package org.jboss.moduledeps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0)
            throw new IllegalArgumentException("Invalid args - null or empty!");

        File root = new File(args[0]);
        List<File> moduleXmls = new ArrayList<File>();
        find(root, moduleXmls);
        Modules modules = Modules.build(moduleXmls);

        if (args.length > 1) {
            ModuleIdentifier mi = ModuleIdentifier.create(args[1]);
            if (args.length > 2) {
                if ("-r".equals(args[2])) {
                    modules.transitive(mi);
                } else if ("-a".equals(args[2])) {
                    modules.all(mi);
                }
            } else {
                modules.print(mi);
            }
        }
    }

    private static void find(File current, List<File> moduleXmls) {
        if (current.isDirectory()) {
            for (File f : current.listFiles()) {
                find(f, moduleXmls);
            }
        } else if (current.getName().equals("module.xml")) {
            moduleXmls.add(current);
        }
    }
}
