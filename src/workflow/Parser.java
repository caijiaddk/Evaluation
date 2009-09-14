package workflow;

import org.w3c.dom.*;




import javax.xml.parsers.*;
import java.util.*;

public class Parser
{
	public Parser()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
		
			Document doc = builder.parse("resource/test.xml");
			
			Element process = doc.getDocumentElement();
			NodeList components = process.getChildNodes();
			for(int count = 0; count < components.getLength(); count++)
			{
				if (!(components.item(count) instanceof Element)) 
					continue;
				Element component = (Element)components.item(count);
				String type = component.getTagName();
				String ID = component.getAttribute("ID");
				
				if (type.equals("sequence"))
					elements.put(ID, new Sequence(ID));
				
				if (type.equals("selection"))
					elements.put(ID, new Selection(ID));
				
				if (type.equals("orconcurrency"))
					elements.put(ID, new OrConcurrency(ID));
				
				if (type.equals("andconcurrency"))
					elements.put(ID, new AndConcurrency(ID));
				
				if (type.equals("repetition"))
					elements.put(ID, new Repetition(ID));
				
				if (type.equals("component"))
					elements.put(ID, new TrustNode(ID));
			}
			
			for(int count = 0; count < components.getLength(); count++)
			{
				if (!(components.item(count) instanceof Element)) 
					continue;
				Element component = (Element)components.item(count);
				NodeList nodelist = component.getElementsByTagName("member");
				String type = component.getTagName();
				String ID = component.getAttribute("ID");
				
				for(int temp = 0; temp < nodelist.getLength(); temp ++)
				{
					Element member = (Element)nodelist.item(temp);
					if (type.equals("sequence"))
						elements.get(ID).members.add(new Member(elements.get(member.getAttribute("reference"))));
					
					if (type.equals("orconcurrency"))
						elements.get(ID).members.add(new Member(elements.get(member.getAttribute("reference"))));

					if (type.equals("andconcurrency"))
						elements.get(ID).members.add(new Member(elements.get(member.getAttribute("reference"))));
					
					if (type.equals("selection"))
						elements.get(ID).members.add(new Member(elements.get(member.getAttribute("reference")), Double.parseDouble(member.getAttribute("probability"))));
					
					if (type.equals("repetition"))
						elements.get(ID).members.add(new Member(elements.get(member.getAttribute("reference")), Double.parseDouble(member.getAttribute("probability"))));
						
					elements.get(member.getAttribute("reference")).addParent(elements.get(ID));
				}
			}
			this.root = elements.get(process.getAttribute("reference"));

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Parser(Element element)
	{
		Element child = ((Element)(element.getFirstChild().getNextSibling()));
		String type = child.getNodeName();
		String ID = child.getAttribute("ID");
		if ("sequence".equals(type))
			root = new Sequence(ID);
		if ("selection".equals(type))
			root = new Selection(ID);
		if ("andconcurrency".equals(type))
			root = new AndConcurrency(ID);
		if ("orconcurrency".equals(type))
			root = new OrConcurrency(ID);
		if (type.equals("repetition"))
			root = new Repetition(ID);
		if (root == null)
			root = new TrustNode(ID);
	}
	
	public void initiate(Hashtable<String, Double> data)
	{
		for(String ID : data.keySet())
		{
			elements.get(ID).setTrustValue(data.get(ID));
		}
	}
	
	public double getTrustValue(String ID)
	{
		return elements.get(ID).getTrustValue();
	}
	
	public void setTrustValue(String ID, double newvalue)
	{
		TrustNode element = elements.get(ID);
		element.setTrustValue(newvalue);
	}
	
	public double getDependence(String ID1, String ID2)
	{
		return elements.get(ID1).getDependenceValue(elements.get(ID2));
	}
	
	public TrustNode getRoot()
	{
		return this.root;
	}
	
	public static void main(String args[])
	{
		Parser parser = new Parser();
		Hashtable<String, Double> data = new Hashtable<String, Double>();
		data.put("a", 0.9);
		data.put("b", 0.9);
		data.put("c", 0.9);
		data.put("d", 0.9);
		data.put("e", 0.9);
		data.put("f", 0.9);
		parser.initiate(data);
		parser.getRoot().calculate(false);
		System.out.println(parser.getTrustValue("l"));
		System.out.println(parser.getDependence("l", "g"));
	}
	
	Hashtable<String, TrustNode> elements = new Hashtable<String, TrustNode>();
	TrustNode root = null;
}