package trustChainDiscovery;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * An list of FriendData class
 * 
 * @author andy
 * 
 */
public class FriendDataList extends LinkedList<FriendData> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5845486198592966942L;

	public boolean hasThisFriend(FriendData f3) {
		for (int i = 0; i < this.size(); i++) {
			if (this.get(i).getFriendAddress().equals(f3.getFriendAddress())
					&& this.get(i).getFriendName().equals(f3.getFriendName())) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		String str = "index, name, address, layerNo, parentIndex\n\n";
		for (int i = 0; i < this.size(); i++) {
			str += i + ":   " + this.get(i).toString() + "\n";
		}
		return str;
	}

}
