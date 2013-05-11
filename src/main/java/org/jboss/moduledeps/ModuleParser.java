package org.jboss.moduledeps;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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
            parseResourceRoot(root, module);
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

	private static void parseResourceRoot(Element root, Module module) {
		String resourceRootPath = getResourceRootPath(root);
		if (resourceRootPath != null) {
			module.addResourceRootPath(resourceRootPath);
		}
	}

	protected static ModuleIdentifier getModuleIdentifier(Element root) {
        return new ModuleIdentifier(root.getAttribute("name"), root.getAttribute("slot"));
    }

	private static String getResourceRootPath(Element root) {
		Element resources = XmlUtils.getChildElement(root, "resources");
		if (resources == null) {
			return null;
		}
		Element resourceRoot = XmlUtils.getChildElement(resources, "resource-root");
		return resourceRoot != null ? resourceRoot.getAttribute("path") : null;
	}
}
