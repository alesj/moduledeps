package org.jboss.moduledeps;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Module {
    private ModuleIdentifier module;
    private Set<ModuleIdentifier> dependencies = new LinkedHashSet<ModuleIdentifier>();

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

        return true;
    }

    @Override
    public int hashCode() {
        return module.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\nModule: " + module + "\n");
        builder.append("Dependencies: ").append(dependencies.size()).append("\n");
        for (ModuleIdentifier dep : dependencies) {
            builder.append("\t").append(dep).append("\n");
        }
        return builder.toString();
    }
}
