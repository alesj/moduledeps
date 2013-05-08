package org.jboss.moduledeps;

public class MissingModule extends Module {

	public MissingModule(ModuleIdentifier module) {
		super(module);
	}

    @Override
    public String toString() {
        return "MISSING MODULE: " + getModule();
    }
}
