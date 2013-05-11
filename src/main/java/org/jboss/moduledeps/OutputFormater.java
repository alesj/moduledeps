package org.jboss.moduledeps;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Set;

public class OutputFormater {

	private PrintStream out;

	private boolean printIdentifier = true;
	private boolean printPath = false;
	private boolean printDependencies = true;

	public OutputFormater(PrintStream out) {
		this.out = out;
	}
	
	public void print(Module module) {
        if (printIdentifier ) {
        	out.println(module);
        }
        if (printDependencies) {
	        printDependencies(module);
        }
        if (printPath) {
        	out.println(module.getPath());
        }
    }

	private void printDependencies(Module module) {
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
	
	public void printIdentifier(boolean printIdentifier) {
		this.printIdentifier = printIdentifier;
	}
	
	public void printPath(boolean printPath) {
		this.printPath = printPath;
	}

	public void printDependencies(boolean printDependencies) {
		this.printDependencies = printDependencies;
	}
	
}
