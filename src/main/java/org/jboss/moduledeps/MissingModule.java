package org.jboss.moduledeps;

import java.io.File;

public class MissingModule extends Module {

	public MissingModule(ModuleIdentifier module) {
		super(module);
	}
	
    @Override
    public String toString() {
        return "MISSING MODULE: " + getModule();
    }
}
