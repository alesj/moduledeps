package org.jboss.moduledeps;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.beust.jcommander.JCommander;

/**
 * Example run arguments:
 *   -mp /home/matej/run/jboss/CapeDwarf_AS7_1.0.0.Beta4/modules 
 *   -a 
 *   -mip /home/matej/java/workspaces/jboss-as/capedwarf-jboss-as-prj/build/src/main/resources/modules
 *   -diff /home/matej/run/jboss/jboss-as-7.1.1.Final/modules
 * 
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author <a href="mailto:mlazar@redhat.com">Matej Lazar</a>
 */
public class Main {
    public static void main(String[] args) throws Exception {
    	
    	Options options = new Options();
		new JCommander(options, args);
    	
    	List<File> pathsModuleXmls = new ArrayList<File>();
    	findFromPaths(pathsModuleXmls, options.modulePaths);
    	
        Modules repoModules = Modules.build(pathsModuleXmls);

        List<ModuleIdentifier> sourceModuleIdentifiers = new ArrayList<ModuleIdentifier>();
        if (options.moduleIdentifiers.size() > 0) {
        	addModuleIdentifiers(sourceModuleIdentifiers, options.moduleIdentifiers);
        }
        if (options.moduleIdentifiersPath.size() > 0) {
        	addModuleIdentifiersFromPaths(sourceModuleIdentifiers, options.moduleIdentifiersPath);
        }
        
        ModuleDeps moduleDeps = new ModuleDeps(repoModules, sourceModuleIdentifiers); 
        
        Collection<Module> sourceModules;
        if (options.transitive) {
        	sourceModules = moduleDeps.transitive();
        } else if (options.all) {
        	sourceModules = moduleDeps.all();
        } else {
        	sourceModules = moduleDeps.identified();
        }
        
    	OutputFormater out = new OutputFormater(System.out);
    	out.printIdentifier(true);
    	out.printPath(true);
    	out.printDependencies(false);
        
    	int numRequiredModules = sourceModules.size();
    	int numBaseModules = 0;
    	
        if (options.baseModulePaths.size() > 0) {
        	List<File> baseModuleXmls = new ArrayList<File>();
        	findFromPaths(baseModuleXmls, options.baseModulePaths);
        	
            Modules baseModules = Modules.build(baseModuleXmls);
            numBaseModules = baseModules.getModules().size();
            
        	sourceModules.removeAll(baseModules.getModules());
        }
    	out.print(sourceModules);
    	
    	System.out.println("Stats:");
    	System.out.println("Initialy required modules with dependencies:" + numRequiredModules); 
    	System.out.println("Base modules:" + numBaseModules);
    	System.out.println("Overly modules: " + sourceModules.size());
    }

	private static void addModuleIdentifiers(List<ModuleIdentifier> mis, List<String> moduleIdentifiers) {
    	for (String moduleIdentifier : moduleIdentifiers) {
			mis.add(ModuleIdentifier.create(moduleIdentifier));
		}
	}

	private static void addModuleIdentifiersFromPaths(List<ModuleIdentifier> mis, List<String> moduleIdentifiersPath) throws Exception {
	    List<File> moduleXmls = new ArrayList<File>();
	    findFromPaths(moduleXmls, moduleIdentifiersPath);
	    Modules modules = Modules.build(moduleXmls);
	    mis.addAll(modules.getIdentifiers());
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

	private static void findFromPaths(List<File> moduleXmls, List<String> modulePathsString) {
		List<File> modulePaths = parsePaths(modulePathsString);
        for (File mp : modulePaths) {
            find(mp, moduleXmls);
		}
	}
}
