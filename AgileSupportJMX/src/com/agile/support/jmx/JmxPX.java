package com.agile.support.jmx;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import com.agile.api.IAgileSession;
import com.agile.api.IDataObject;
import com.agile.api.INode;
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;

public class JmxPX implements ICustomAction {

	@Override
	public ActionResult doAction(IAgileSession sess, INode node, IDataObject obj) {
		
		try {
			new AgileSupportJMXAgent().register();
		} catch (Exception e) {
			e.printStackTrace();
			return new ActionResult(ActionResult.EXCEPTION, e);
		}
		
		return new ActionResult(ActionResult.STRING, "AgileSupportJMXAgent started.");
	}
	
}
