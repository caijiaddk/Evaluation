package workflow;




public class Sequence extends TrustNode{
	public Sequence(String ID)
	{
		super(ID);
	}
	
	protected void calculateTrust()
	{
		this.trustValue = 1;
		for(Member member : members)
			this.trustValue *= member.node.trustValue;
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
			times *= member.getNode().getTrustValue(); 
		}	
	}
	
	
	@Override
	public Sequence clone() throws CloneNotSupportedException {
		Sequence clonedInstance = new Sequence(this.ID);
		return clonedInstance;
	}
	
	
}
