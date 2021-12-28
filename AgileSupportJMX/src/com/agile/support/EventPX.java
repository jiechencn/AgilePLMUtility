package com.agile.support;

import com.agile.api.IAgileSession;
import com.agile.api.INode;
import com.agile.px.*;

public class EventPX implements IEventAction{

	public EventActionResult doAction(IAgileSession sess, INode node,
			IEventInfo info) {
		String mess = "EventPX executed ";
		try{
		mess = mess + " for " + info.getEventSubscriberName() ;
		
		}catch(Exception e){
			//
		}
		System.out.println(mess);
		return new EventActionResult(info, new ActionResult(ActionResult.STRING, mess));
	}

}
