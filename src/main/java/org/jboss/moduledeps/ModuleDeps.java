package org.jboss.moduledeps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModuleDeps {

	private Modules repoModules;
	private List<ModuleIdentifier> moduleIdentifiers;
	
	public ModuleDeps(Modules repoModules, List<ModuleIdentifier> moduleIdentifiers) {
		this.repoModules = repoModules;
		this.moduleIdentifiers = moduleIdentifiers;
	}

	public List<Module> transitive() {
        List<Module> modules = new ArrayList<Module>();
	    for (ModuleIdentifier mi : moduleIdentifiers) {
	    	modules.addAll(repoModules.transitive(mi));
	    }
	    return modules;
	}

	public Set<Module> all() {
        Set<Module> modules = new HashSet<Module>();
	    for (ModuleIdentifier mi : moduleIdentifiers) {
	    	modules.addAll(repoModules.all(mi));
	    }
	    return modules;
	}

	public List<Module> identified() {
        List<Module> modules = new ArrayList<Module>();
	    for (ModuleIdentifier mi : moduleIdentifiers) {
	    	Module module = repoModules.getModule(mi);
	        if (module != null) modules.add(module); else modules.add(new MissingModule(mi));
	    }
	    return modules;
	}
}

