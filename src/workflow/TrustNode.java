package workflow;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;



//import org.eclipse.swt.widgets.Display;

//import artemis.vide.repository.connector.ConnectorInfo;


public class TrustNode implements Serializable,Cloneable{
	private static final long serialVersionUID = 1L;
	public TrustNode(String ID)
	{
		this.ID = ID;
	}	
	
	public TrustNode(String ID, double trustValue){
		this.ID = ID;
		this.trustValue = trustValue;
	}
		
//	public String toString()
//	{
//		String result = "";
//		result += "ID: " + this.ID;
//		for (Member member : members)
//			result += "\nmember: " + member.node.ID;
//		if (parent != null)
//			result +="\nparent: " + parent.ID;
//		return result+"\n";
//	}
	
	public double getDependenceValue(TrustNode node)
	{
		if (this == node)
			return 1;
		if (members.size() == 0)
			return 0;
		return calculateDependence(node);
	}
	
	public double getDependence(TrustNode node)
	{
		double result = getDependenceValue(node);
		result = ((int)(result*100))/100.0;
		return result;
	}
	
	public double calculate(boolean notify)
	{
		if (calculated)
			{
			return this.getTrustValue();
			
			}
		for(Member member:members)
		{
			member.node.calculate(notify);
		}
		calculateTrust();
		if (notify)
			;//notifyListeners();
		calculated = true;
		return this.getTrustValue();
	}
	
	public void clear()
	{
		this.calculated = false;
		for(Member member : members)
			member.getNode().clear();
	}
	
	public void evaluate()
	{
		clear();
		calculate(false);
	}
	
	public ArrayList<TrustNode> getComponnents()
	{
		ArrayList<TrustNode> result = new ArrayList<TrustNode>();
		
		for(Member member : members)
		{
			ArrayList<TrustNode> temp = member.node.getComponnents();
			for(TrustNode node : temp)
				if (!result.contains(node))
					result.add(node);
		}
		
		result.add(this);
		return result;
	}
	
	public ArrayList<String> getBasicComponnents()
	{
		ArrayList<String> result = new ArrayList<String>();
		
		for(Member member : members)
		{
			ArrayList<String> temp = member.node.getBasicComponnents();
			for(String node : temp)
				if (!result.contains(node))
					result.add(node);
		}
		
		if (this.getMembers().size() == 0) result.add(this.getID());
		return result;
	}
	
	public double getPropertyByName(String name, String tag)
	{
		if (this.getID().equals(name))
		{
			if (tag.equals("trust"))
				return this.getTrustValue();
			if (tag.equals("importance"))
			{
				TrustNode root = this;
				while(root.getParents().size() > 0)
					root = root.getParents().get(0);
				return root.getDependence(this);
			}
			if (tag.equals("cost"))
			{
				return this.cost;
			}
			if (tag.equals("total cost"))
			{
				return this.cost * this.getTimes();
			}
			return this.getTimes();
		}
		else
		{
			for(Member member : this.getMembers())
			{
				double result = member.node.getPropertyByName(name, tag);
				if (result >= 0)
					return result;
			}
		}
		return -1;
	}
	
	public void setPropertyByName(String name, String tag, double value)
	{
		if (this.getID().equals(name))
		{
			if (tag.equals("trust"))
				this.trustValue = value;
			this.times = value;
		}
		else
		{
			for(Member member : this.getMembers())
				member.node.setPropertyByName(name, tag,value);
		}
	}
	
	public void update()
	{
		calculateTrust();
		//notifyListeners();
		for(TrustNode parent : this.parents)
			parent.update();
	}
	
//	public void notifyListeners()
//	{
//		final int rounds = 40;
//		for (int count = 0; count < rounds; count++)
//		{
//			for (Object listener:listeners)
//			{
//				final Object temp = listener;
//				final int tempcount=count+1;
//				Display.getDefault().syncExec(new Runnable()
//				{
//					public void run()
//					{
////						if (temp instanceof MyComposite)
////							((MyComposite)temp).splash(tempcount,rounds);		
////						if (temp instanceof Info)
////							((Info)temp).splash(tempcount,rounds, trustValue);		
//					}
//				});
//			} 
//			try
//			{
//				Thread.currentThread().sleep(50);
//			}
//			catch(Exception e)
//			{
//				e.printStackTrace();
//			}
//		}
//	}
	
	protected void calculateTrust()
	{
		return;
	}
	
	protected double calculateDependence(TrustNode node)
	{
		return 0;
	}
	
	public double getTimes() {
		return times;
	}

	public void setTimes(double times) {
		this.times = times;
	}

	public String getID() {
		return ID;
	}

	public void setID(String id) {
		ID = id;
	}
	
	public ArrayList<TrustNode> getParents() {
		return this.parents;
	}

	public void addParent(TrustNode parent) {
		this.parents.add(parent);
	}

	public ArrayList<Member> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<Member> members) {
		this.members = members;
//		for (Member member : members)
//			member.getNode().addParent(this);
	}

	public double getTrustValue()
	{
		double result = this.trustValue;
		result = ((int)(result*100))/100.0;
		return result;
	}

	public double setTrustValue(double newvalue)
	{
		this.trustValue = newvalue;
		return this.trustValue;
	}
	
	public TrustNode find(String ID)
	{
		if (this.getID().equals(ID))
			return this;
		for(Member member : this.members)
		{
			TrustNode result = null;
			result = member.node.find(ID);
			if (result != null)
				return result;
		}
		return null;
	}
	
	public boolean isBasicComponent()
	{
		if (this.getMembers().size() > 0)
			return false;
		else
			return true;
	}
	
	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double trustValue = 1;
	public double times = 0;
	protected String ID;
	protected ArrayList<Member> members = new ArrayList<Member>();
	protected ArrayList<TrustNode> parents = new ArrayList<TrustNode>();
	protected Hashtable<TrustNode,Double> dependence = new Hashtable<TrustNode, Double>();
	protected ArrayList<Object> listeners = new ArrayList<Object>();
	protected double cost = 100;
	public boolean calculated = false;

	
	@Override
	public TrustNode clone() throws CloneNotSupportedException {
		TrustNode clonedInstance = new TrustNode(this.ID);
		return clonedInstance;
	}
}
