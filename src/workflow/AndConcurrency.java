package workflow;




public class AndConcurrency extends TrustNode{
	public AndConcurrency(String ID)
	{
		super(ID);
	}
	
	protected void calculateTrust()
	{
		trustValue = 1;
		for(Member member : members)
			trustValue *= member.node.trustValue;
	}
	
	protected double calculateDependence(TrustNode node)
	{
		if (members.size() == 0)
			return 0;
		double result = 0;
		for(Member member : members)
		{
			TrustNode tempnode = member.node;
			result +=  tempnode.getDependenceValue(node) * this.trustValue / tempnode.trustValue;
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
	public AndConcurrency clone() throws CloneNotSupportedException {
		AndConcurrency clonedInstance = new AndConcurrency(this.ID);
		return clonedInstance;
	}
}
