package org.jboss.moduledeps;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Module {
    private ModuleIdentifier module;
    private Set<ModuleIdentifier> dependencies = new LinkedHashSet<ModuleIdentifier>();
	private Set<String> resources = new HashSet<String>();
	private File path;

    public Module(ModuleIdentifier module) {
        this.module = module;
    }

    public void addDependency(ModuleIdentifier mi) {
        dependencies.add(mi);
    }

    public ModuleIdentifier getModule() {
        return module;
    }

    public Set<ModuleIdentifier> getDependencies() {
        return dependencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Module module1 = (Module) o;

        if (!module.equals(module1.module)) return false;

        if (resources.size() > 0) {
        	return compareResources(module1.resources);
        }
        return true;
    }

    private boolean compareResources(Set<String> otherResources) {
		return resources.equals(otherResources);
	}

	@Override
    public int hashCode() {
        return module.hashCode();
    }

    @Override
    public String toString() {
        return "Module: " + module + "";
    }

	public void addResourceRootPath(String resourceRootPath) {
		resources.add(resourceRootPath);
	}

	public void setPath(File path) {
		this.path = path;
	}
	
	public File getPath() {
		return path;
	}
}
