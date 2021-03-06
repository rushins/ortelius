/*
 *
 *  DeployHub is an Agile Application Release Automation Solution
 *  Copyright (C) 2017 Catalyst Systems Corporation DBA OpenMake Software
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dmadmin.model;

//import java.util.HashMap;
import java.util.Hashtable;
//import java.util.Map;





import dmadmin.DMSession;
import dmadmin.PropertyDataSet;
import dmadmin.SummaryField;
import dmadmin.json.BooleanField;
import dmadmin.json.IJSONSerializable;
import dmadmin.util.CommandLine;
//import dmadmin.PropertyDataSet;

public class TaskApprove
	extends Task
{
	private static final long serialVersionUID = 1327862378913381548L;
	
	private Application m_app;
	private boolean m_approved;
	private String m_text;
	private Domain m_approvalDomain;
	private CommandLine m_cmd;
	
	
	public TaskApprove() {
	}
	
	public TaskApprove(DMSession sess, int id, String name) {
		super(sess, id, name);
		System.out.println("In constructor for TaskApprove");
	}
	
	public Application getApplication()  { return m_app; }
	public void setApplication(Application app)  { m_app = app; }
	
	public boolean isApproved()  { return m_approved; }
	public void setApproved(boolean approved)  { m_approved = approved; }

	public String getText()  { return m_text; }
	public void setText(String text)  { m_text = text; }
	
	public Domain getApprovalDomain() { return m_approvalDomain; }
	public void setApprovalDomain(Domain domain) { m_approvalDomain = domain; }
	
	
	@Override
	public boolean run() {
		if(m_app == null) {
			System.err.println("Application not set");
			return false;
		}
		//return m_app.approve(m_approvalDomain, m_approved, m_text);
		
		Domain domain = getDomain();
		Engine engine = (domain != null) ? domain.findNearestEngine() : null;
		if(engine == null) {
			System.err.println("Engine was null");
			return false;
		}
		m_cmd = engine.doApprove(this, m_app, m_approved, m_aps);
		return (m_cmd.run(true, m_text + "\n", true) == 0);
	}

 @Override
 public IJSONSerializable getSummaryJSON() {
  PropertyDataSet ds = new PropertyDataSet();
  ds.addProperty(SummaryField.NAME, "Name", getName());
  addCreatorModifier(ds);
  ds.addProperty(SummaryField.PRE_ACTION, "Pre-action", ((getPreAction() != null) ? getPreAction().getLinkJSON() : null));
  ds.addProperty(SummaryField.POST_ACTION, "Post-action", ((getPostAction() != null) ? getPostAction().getLinkJSON() : null));
  ds.addProperty(SummaryField.TASK_SHOWOUTPUT, "Show Output", new BooleanField(getShowOutput()));
  ds.addProperty(SummaryField.TASK_AVAILABLE_TO_SUBDOMAINS, "Available in SubDomains", new BooleanField(getSubDomains()));
  ds.addProperty(SummaryField.TASK_APPROVAL_DOMAIN, "Approval Domain", ((getApprovalDomain() != null) ? getApprovalDomain().getFullDomain() : null));
  ds.addProperty(SummaryField.SUCCESS_TEMPLATE, "Approved Notify Template", ((getSuccessTemplate() != null) ? getSuccessTemplate().getLinkJSON() : null));
  ds.addProperty(SummaryField.FAILURE_TEMPLATE, "Rejected Notify Template", ((getFailureTemplate() != null) ? getFailureTemplate().getLinkJSON() : null));
  return ds.getJSON();
 }
 
	public String getOutput() {
		return m_cmd.getOutput();
	}
	
	public boolean updateTask(Hashtable<String, String> changes) {
		// TODO Auto-generated method stub
		return false;
	}
}
