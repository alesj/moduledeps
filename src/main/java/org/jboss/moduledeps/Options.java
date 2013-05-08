package org.jboss.moduledeps;

import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

public class Options {
	
	@Parameter(names = "--help", help = true)
	private boolean help;
	
	@Parameter(names = "-r", description = "List transitive modules.")
	public Boolean transitive = false;
	
	@Parameter(names = "-a", description = "List all modules.")
	public Boolean all = false;

	@Parameter(names = "-mp", required=true, description = "Module paths.")
    public List<String> modulePaths = Lists.newArrayList();
	
	@Parameter(names = "-mi", description = "Module identifiers.")
	public List<String> moduleIdentifiers = Lists.newArrayList();
	
	@Parameter(names = "-mip", description = "Path to find all module identifiers.")
	public List<String> moduleIdentifiersPath = Lists.newArrayList();
	
	@Parameter(names = "-diff", description = "Paths used as modules base layers. If specified modules diff is printed.")
	public List<String> baseModulePaths = Lists.newArrayList();

}
