package org.jboss.moduledeps;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class OutputFormater {

	private PrintStream out;

	public OutputFormater(PrintStream out) {
		this.out = out;
	}
	
	public void print(Module module) {
        out.println(module);
        Set<ModuleIdentifier> dependencies = module.getDependencies();
        out.println("Dependencies = " + dependencies.size());
        for (ModuleIdentifier m : dependencies) {
            out.println(m);
        }
	}

	public void print(Collection<Module> transitiveModules) {
		for (Module module : transitiveModules) {
			print(module);
		}
	}
}
