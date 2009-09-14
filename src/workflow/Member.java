package workflow;

import java.io.Serializable;



public class Member implements Serializable{

	private static final long serialVersionUID = 1L;
	public Member(TrustNode node, double probabiltiy)
	{
		this.node = node;
		this.probability = probabiltiy;
	}
	
	public Member(TrustNode node)
	{
		this(node, 1);
	}
	
	public Member(double probability)
	{
		this(null, probability);
	}
	
	public TrustNode getNode() {
		return node;
	}

	public void setNode(TrustNode node) {
		this.node = node;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}


	protected TrustNode node = null;
	protected double probability = 1;
}
