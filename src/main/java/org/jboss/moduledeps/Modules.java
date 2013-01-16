package org.jboss.moduledeps;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Modules {
    private PrintStream out = System.out;
    private Map<ModuleIdentifier, Module> modules = new LinkedHashMap<ModuleIdentifier, Module>();

    private Modules() {
    }

    public static Modules build(List<File> moduleXmls) throws Exception {
        Modules m = new Modules();
        for (File moduleXml : moduleXmls) {
            Module module = ModuleParser.parse(moduleXml);
            m.modules.put(module.getModule(), module);
        }
        return m;
    }

    public void print(ModuleIdentifier mi) {
        Module module = modules.get(mi);
        out.println(module != null ? module : "No such module: " + mi);
    }

    public void transitive(ModuleIdentifier mi) {
        Module module = modules.get(mi);
        if (module != null) {
            out.println(module);
            Set<ModuleIdentifier> visited = new HashSet<ModuleIdentifier>();
            List<Module> transitive = new ArrayList<Module>();
            recurse(module, visited, transitive);
            out.println("Dependencies = " + transitive.size());
            for (Module t : transitive) {
                out.println(t);
            }
        } else {
            out.println("No such module: " + mi);
        }
    }

    public void all(ModuleIdentifier mi) {
        Module module = modules.get(mi);
        if (module != null) {
            out.println(module);
            Set<ModuleIdentifier> visited = new HashSet<ModuleIdentifier>();
            Set<ModuleIdentifier> transitive = new TreeSet<ModuleIdentifier>();
            recurse(module, visited, transitive);
            out.println("Dependencies = " + transitive.size());
            for (ModuleIdentifier t : transitive) {
                out.println("\t" + t);
            }
        } else {
            out.println("No such module: " + mi);
        }
    }

    private void recurse(Module module, Set<ModuleIdentifier> visited, List<Module> transitive) {
        for (ModuleIdentifier dep : module.getDependencies()) {
            if (visited.add(dep)) {
                Module tm = modules.get(dep);
                if (tm != null) {
                    transitive.add(tm);
                    recurse(tm, visited, transitive);
                } else {
                    out.println("No such transitive module in '" + module.getModule() + "' -- " + dep + "\n");
                }
            }
        }
    }

    private void recurse(Module module, Set<ModuleIdentifier> visited, Set<ModuleIdentifier> transitive) {
        for (ModuleIdentifier dep : module.getDependencies()) {
            if (visited.add(dep)) {
                Module tm = modules.get(dep);
                if (tm != null) {
                    transitive.add(tm.getModule());
                    recurse(tm, visited, transitive);
                } else {
                    out.println("No such transitive module in '" + module.getModule() + "' -- " + dep + "\n");
                }
            }
        }
    }
}
