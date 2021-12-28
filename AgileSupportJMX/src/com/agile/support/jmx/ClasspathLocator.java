package com.agile.support.jmx;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class ClasspathLocator implements ClasspathLocatorMBean {

	@Override
	public String findLocation(String klass) {
		klass = klass.trim();
		try {
			Class clazz = Class.forName(klass);
			if (clazz == null) {
				return "Invalid class: " + klass;
			}

			ProtectionDomain protectionDomain = clazz.getProtectionDomain();
			CodeSource codeSource = protectionDomain.getCodeSource();
			File jarFile;

			if (codeSource != null && codeSource.getLocation() != null) {
				jarFile = new File(codeSource.getLocation().toURI());
			} else {
				String path = clazz.getResource(clazz.getSimpleName() + ".class").getPath();
				String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
				jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
				jarFile = new File(jarFilePath);
			}

			return klass + " -> " + jarFile.getAbsolutePath();
		} catch (Throwable e) {
			e.printStackTrace();
			return klass + " Not found";
		}
	}

}
