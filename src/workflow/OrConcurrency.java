package workflow;





public class OrConcurrency extends TrustNode {
	public OrConcurrency(String ID)
	{
		super(ID);
	}
	
	protected void calculateTrust()
	{
		trustValue = 1;
		for(Member member : members)
			trustValue *= (1 - member.node.trustValue);
		trustValue = 1 -trustValue;
	}
	
	protected double calculateDependence(TrustNode node)
	{
		if (members.size() == 0)
			return 0;
		double result = 0;
		for(Member member : members)
		{
			TrustNode tempnode = member.node;
			result +=  tempnode.getDependenceValue(node) * (1 - this.trustValue) / (1 - tempnode.trustValue);
		}
		return result;
	}
	
	public void setTimes(double times) 
	{
		this.times = times;
		
		for(Member member : members)
		{
			member.getNode().setTimes(times);
		}	
	}
	
	@Override
	public OrConcurrency clone() throws CloneNotSupportedException {
		OrConcurrency clonedInstance = new OrConcurrency(this.ID);
		return clonedInstance;
	}
}
