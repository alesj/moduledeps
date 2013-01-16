package org.jboss.moduledeps;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

    protected static ModuleIdentifier getModuleIdentifier(Element root) {
        return new ModuleIdentifier(root.getAttribute("name"), root.getAttribute("slot"));
    }
}
