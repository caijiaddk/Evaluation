package trustChainDiscovery;

import java.io.Serializable;
import java.util.LinkedList;



/**
 * An class to store a friend's information.
 * 
 * @author andy pa
 */
public class FriendData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8892249548680167434L;

	/**
	 * friend's host name
	 */
	private String friendName;

	/**
	 * friend's IP address
	 */
	private String friendAddress;

	/**
	 * the friend's layer number about one user's collecting friends request
	 */
	private int layerNo;

	/**
	 * the friend's parent about one user's collecting friends request
	 */
	private int parent;

	/**
	 * store the result about this friend
	 */
	private LinkedList<int[]> result;

	/**
	 * store the use's expectation
	 */


	public FriendData() {

	}

	public FriendData(String na, String add) {
		friendName = na;
		friendAddress = add;
	}

	public FriendData(String na, String add, int la, int pa) {
		friendName = na;
		friendAddress = add;
		layerNo = la;
		parent = pa;
	}

	public void setResult(LinkedList<int[]> result) {
		this.result = result;
	}

	public LinkedList<int[]> getResult() {
		return result;
	}

	/**
	 * 
	 * @param name
	 */
	public void setFriendName(String name) {
		friendName = name;
	}

	/**
	 * Set friend address when the IP's format is correct.
	 * 
	 * @param address
	 * @throws Exception
	 */
//	public void setFriendAddress(String address) throws Exception {
//		StringPatternMatcher IP_DOMAIN_MATCHER = StringPatternMatcherFactory
//				.createStringPatternMatcher(StringPatternMatcherFactory.PATTERN_DOMAIN
//						+ StringPatternMatcherFactory.PATTERN_IP);
//		if (IP_DOMAIN_MATCHER.match(address)) {
//			friendAddress = address;
//		} else {
//			throw new Exception("the format of the address is not correct.");
//		}
//	}

	/**
	 * 
	 * @return friendName
	 */
	public String getFriendName() {
		return friendName;
	}

	/**
	 * 
	 * @return friendAddress
	 */
	public String getFriendAddress() {
		return friendAddress;
	}

	public String toString() {
		return friendName + "@" + friendAddress + "   " + this.layerNo + "   "
				+ this.parent;
	}

	public int getLayerNo() {
		return layerNo;
	}

	public void setLayerNo(int layerNo) {
		this.layerNo = layerNo;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}


}
