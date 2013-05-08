package org.jboss.moduledeps;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.beust.jcommander.JCommander;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author <a href="mailto:mlazar@redhat.com">Matej Lazar</a>
 */
public class Main {
    public static void main(String[] args) throws Exception {
    	
    	Options options = new Options();
		new JCommander(options, args);
    	
    	List<File> repoModuleXmls = new ArrayList<File>();
    	findFromPaths(repoModuleXmls, options.modulePaths);
    	
        Modules repoModules = Modules.build(repoModuleXmls);

        List<ModuleIdentifier> sourceModuleIdentifiers = new ArrayList<ModuleIdentifier>();
        if (options.moduleIdentifiers.size() > 0) {
        	addModuleIdentifiers(sourceModuleIdentifiers, options.moduleIdentifiers);
        }
        if (options.moduleIdentifiersPath.size() > 0) {
        	addModuleIdentifiersFromPaths(sourceModuleIdentifiers, options.moduleIdentifiersPath);
        }
        
        ModuleDeps moduleDeps = new ModuleDeps(repoModules, sourceModuleIdentifiers); 
        
        Collection<Module> modules;
        if (options.transitive) {
        	modules = moduleDeps.transitive();
        } else if (options.all) {
        	modules = moduleDeps.all();
        } else {
        	modules = moduleDeps.identified();
        }
        
    	OutputFormater out = new OutputFormater(System.out);
        
        if (options.baseModulePaths.size() > 0) {
        	List<File> baseModuleXmls = new ArrayList<File>();
        	findFromPaths(repoModuleXmls, options.baseModulePaths);
        	
            Modules baseModules = Modules.build(baseModuleXmls);
        	ModuleDeps baseModuleDeps = new ModuleDeps(baseModules, sourceModuleIdentifiers);
        	
        	List<Module> baseModulesTransitive = baseModuleDeps.transitive();
        	modules.removeAll(baseModulesTransitive);
        }
    	out.print(modules);
    }

    private static void addModuleIdentifiersFromPaths(List<ModuleIdentifier> mis, List<String> moduleIdentifiersPath) throws Exception {
    	List<File> moduleXmls = new ArrayList<File>();
    	findFromPaths(moduleXmls, moduleIdentifiersPath);
    	Modules modules = Modules.build(moduleXmls);
    	mis.addAll(modules.getIdentifiers());
	}

	private static void addModuleIdentifiers(List<ModuleIdentifier> mis, List<String> moduleIdentifiers) {
    	for (String moduleIdentifier : moduleIdentifiers) {
			mis.add(ModuleIdentifier.create(moduleIdentifier));
		}
	}

	private static List<File> parsePaths(List<String> modulePaths) {
    	List<File> paths = new ArrayList<File>();
    	for (String pathname : modulePaths) {
			paths.add(new File(pathname));
		}
		return paths;
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

	private static void findFromPaths(List<File> moduleXmls, List<String> moduleIdentifiersPath) {
		List<File> modulePaths = parsePaths(moduleIdentifiersPath);
        for (File mp : modulePaths) {
            find(mp, moduleXmls);
		}
	}
}
