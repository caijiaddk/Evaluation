package workflow;




public class Selection extends TrustNode {
	public Selection(String ID)
	{
		super(ID);
	}
	
	protected void calculateTrust()
	{
		trustValue = 0;
		for(Member member : members)
			trustValue += (member.node.trustValue * member.probability);
	}
	
	protected double calculateDependence(TrustNode node)
	{
		if (members.size() == 0)
			return 0;
		double result = 0;
		for(Member member : members)
		{
			TrustNode tempnode = member.node;
			result +=  tempnode.getDependenceValue(node) * member.probability;
		}
		return result;
	}
	
	public void setTimes(double times)
	{
		this.times = times;
		
		for (Member member : members)
		{
			member.getNode().setTimes(times*member.probability);
		}
	}
	
	@Override
	public Selection clone() throws CloneNotSupportedException {
		Selection clonedInstance = new Selection(this.ID);
		return clonedInstance;
	}
}
