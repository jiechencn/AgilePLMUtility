package com.dahuatech.evtpx;

import com.agile.api.APIException;
import com.agile.api.ChangeConstants;
import com.agile.api.IAgileClass;
import com.agile.api.IAgileList;
import com.agile.api.IAgileSession;
import com.agile.api.ICell;
import com.agile.api.IChange;
import com.agile.api.INode;
import com.agile.api.IUser;
import com.agile.api.IWorkflow;
import com.agile.api.UserConstants;
import com.agile.px.ActionResult;
import com.agile.px.EventActionResult;
import com.agile.px.ICreateEventInfo;
import com.agile.px.IEventAction;
import com.agile.px.IEventInfo;


public class SetWorkflowCA
  implements IEventAction
{
  public EventActionResult doAction(IAgileSession session, INode node, IEventInfo eventInfo)
  {
    StringBuffer history = new StringBuffer();
    
    ICreateEventInfo info = (ICreateEventInfo)eventInfo;
    ActionResult ar = null;
    IChange change = null;
    try
    {
      change = (IChange)info.getDataObject();
      
      IUser user = (IUser)session.getObject(42, "developer");
      IWorkflow[] workflows = change.getWorkflows(); 
      System.out.println("---------  begin of set workflow");
      for (int i=0;i<workflows.length;i++){
    	  System.out.println(workflows[i]);
      }

      if (workflows.length > 0) {
        change.setValue(ChangeConstants.ATT_COVER_PAGE_WORKFLOW, workflows[0]);
        System.out.println("set to=" + workflows[0]);
        
      }
      System.out.println("---------  end of set workflow");
      String superClassAPIName = change.getAgileClass().getSuperClass().getAPIName();
      if (superClassAPIName.equals("ManufacturerOrdersClass"))
      {
        change.setValue(ChangeConstants.ATT_COVER_PAGE_COMPONENT_ENGINEER, user);
        history.append("set component Engineer workflow.");
      }
      else
      {
        change.setValue(ChangeConstants.ATT_COVER_PAGE_CHANGE_ANALYST, user);
        history.append("set change Analyst workflow.");
      }
      //String plStr = Utils.GenerateProductLine(change);
      //history.append(plStr);
      

      ICell userCell = change.getCell(ChangeConstants.ATT_COVER_PAGE_ORIGINATOR);
      IAgileList userList = (IAgileList)userCell.getValue();
      IUser createUser = (IUser)userList.getSelection()[0].getValue();
      
      String userDepartment = "";
      if (createUser != null)
      {
        ICell departmentCell = createUser.getCell(UserConstants.ATT_PAGE_TWO_MULTITEXT10);
        if ((departmentCell != null) && (departmentCell.getValue() != null)) {
          userDepartment = departmentCell.getValue().toString();
        }
      }
      if (!userDepartment.equals(""))
      {
        change.setValue(ChangeConstants.ATT_PAGE_TWO_MULTITEXT10, userDepartment);
        history.append(" 设置创建者所属部门：" + userDepartment + "成功");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      history.append(e.getMessage());
    }
    ar = new ActionResult(0, history.toString());
    return new EventActionResult(eventInfo, ar);
  }
}
