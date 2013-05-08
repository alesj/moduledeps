package org.jboss.moduledeps;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author <a href="mailto:mlazar@redhat.com">Matej Lazar</a>
 */
public class Modules {
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

    public Module getModule(ModuleIdentifier mi) {
        return modules.get(mi);
    }

    public List<Module> transitive(ModuleIdentifier mi) {
    	List<Module> modulesList = new ArrayList<Module>();
    	Module module = modules.get(mi);
        if (module != null) {
            modulesList.add(module);
            Set<ModuleIdentifier> visited = new HashSet<ModuleIdentifier>();
            List<Module> transitive = new ArrayList<Module>();
            recurse(module, visited, transitive);
            modulesList.addAll(transitive);
        }
        return modulesList;
    }

    public Set<Module> all(ModuleIdentifier mi) {
    	Set<Module> moduleSet = new HashSet<Module>();
        Module module = modules.get(mi);
        if (module != null) {
        	moduleSet.add(module);
            Set<ModuleIdentifier> visited = new HashSet<ModuleIdentifier>();
            Set<Module> transitive = new TreeSet<Module>();
            recurse(module, visited, transitive);
            for (Module transitiveModule : transitive) {
                moduleSet.add(transitiveModule);
			}
        }
        return moduleSet;
    }

    private void recurse(Module module, Set<ModuleIdentifier> visited, Collection<Module> transitive) {
        for (ModuleIdentifier dep : module.getDependencies()) {
            if (visited.add(dep)) {
                Module tm = modules.get(dep);
                if (tm != null) {
                    transitive.add(tm);
                    recurse(tm, visited, transitive);
                } else {
                	transitive.add(new MissingModule(dep));
                }
            }
        }
    }
    
	public Collection<ModuleIdentifier> getIdentifiers() {
		return modules.keySet();
	}
}
