package com.agile.support.jmx;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;



public class AgileSupportJMXAgent {
	private final MBeanServer mbs;

	public AgileSupportJMXAgent() {
		mbs = ManagementFactory.getPlatformMBeanServer();
	}

	public void register() throws Exception {
		ObjectName objectName = new ObjectName("AgileSupport:type=ClasspathLocator");
		if (mbs.isRegistered(objectName)){
			mbs.unregisterMBean(objectName);
		}
		ClasspathLocator cpLoc = new ClasspathLocator();
		mbs.registerMBean(cpLoc, objectName);
			
	}

}
