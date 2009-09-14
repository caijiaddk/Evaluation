package workflow;




public class Repetition extends TrustNode {
	public Repetition(String ID)
	{
		super(ID);
	}
	
	protected void calculateTrust()
	{
		Member member = members.get(0);
		trustValue = (1 - member.probability) * member.node.trustValue / (1 - member.node.trustValue * member.probability);
	}
	
	protected double calculateDependence(TrustNode node)
	{
		if (members.size() == 0)
			return 0;
		Member member = members.get(0);
		return member.node.getDependenceValue(node) * (1 - member.probability) / Math.pow(1 - member.probability * member.node.trustValue, 2);
	}
	
	public void setTimes(double times)
	{
		this.times = times;
		
		for (Member member : members)
		{
			member.getNode().setTimes(times /(1 - member.probability));
		}
	}
	
	@Override
	public Repetition clone() throws CloneNotSupportedException {
		Repetition clonedInstance = new Repetition(this.ID);
		return clonedInstance;
	}
}
