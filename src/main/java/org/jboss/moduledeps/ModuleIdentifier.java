package org.jboss.moduledeps;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ModuleIdentifier implements Comparable<ModuleIdentifier> {
    private String name;
    private String slot;

    public ModuleIdentifier(String name, String slot) {
        this.name = name;
        if (slot == null || slot.length() == 0)
            slot = "main";
        this.slot = slot;
    }

    public static ModuleIdentifier create(String string) {
        String[] split = string.split(":");
        return new ModuleIdentifier(split[0], split.length > 1 ? split[1] : null);
    }

    public String getName() {
        return name;
    }

    public String getSlot() {
        return slot;
    }

    public int compareTo(ModuleIdentifier o) {
        int diff = name.compareTo(o.getName());
        if (diff != 0)
            return diff;
        return slot.compareTo(o.getSlot());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModuleIdentifier that = (ModuleIdentifier) o;

        if (!name.equals(that.name)) return false;
        if (!slot.equals(that.slot)) return false;

        return true;
    }

    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + slot.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name + ":" + slot;
    }
}
