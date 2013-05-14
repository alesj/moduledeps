package org.jboss.moduledeps;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ModuleParser {
    public static Module parse(File moduleXml) throws Exception {
        InputStream is = new FileInputStream(moduleXml);
        try {
            Document document = XmlUtils.parseXml(is);
            Element root = document.getDocumentElement();
            ModuleIdentifier main = getModuleIdentifier(root);
            Module module = new Module(main);
            module.setPath(moduleXml);
            parseResourceRoots(root, module);
            Element dependencies = XmlUtils.getChildElement(root, "dependencies");
            if (dependencies != null) {
                for (Element dependency :XmlUtils.getElements(dependencies, "module")) {
                    module.addDependency(getModuleIdentifier(dependency));
                }
            }
            return module;
        } finally {
            is.close();
        }
    }

	private static void parseResourceRoots(Element root, Module module) {
		List<String> resourceRootPaths = getResourceRootPaths(root);
		for (String resourceRootPath : resourceRootPaths) {
			module.addResourceRootPath(resourceRootPath);
		}
	}

	protected static ModuleIdentifier getModuleIdentifier(Element root) {
        return new ModuleIdentifier(root.getAttribute("name"), root.getAttribute("slot"));
    }

	private static List<String> getResourceRootPaths(Element root) {
		List<String> resourcePaths = new ArrayList<String>();
		Element resources = XmlUtils.getChildElement(root, "resources");
		if (resources == null) {
			return resourcePaths;
		}
		List<Element> resourceRoots = XmlUtils.getElements(resources, "resource-root");
		for (Element resourceRoot : resourceRoots) {
			if (resourceRoot != null) {
				resourcePaths.add(resourceRoot.getAttribute("path"));
			}
		}
		return resourcePaths;
	}
}
