package com.agile.support;

import com.agile.api.IAgileSession;
import com.agile.api.IDataObject;
import com.agile.api.INode;
import com.agile.px.*;

public class WFLPX implements ICustomAction{

	public ActionResult doAction(IAgileSession sess, INode node,
			IDataObject obj) {
		String mess = "WFLPX execute";
		System.out.println(mess);
		
		return new ActionResult(ActionResult.STRING, mess);
	}

}
