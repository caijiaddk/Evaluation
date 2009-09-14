package trustChainDiscovery;


/**
 * An Node to be painted in the BootGui, it maybe a friend or a host.
 * 
 * @author andy
 * 
 */
public class NodeView {
	private int x;
	private int y;

	private FriendData friend;

	public NodeView() {

	}

	public NodeView(int x, int y, FriendData friend) {
		this.x = x;
		this.y = y;
		this.friend = friend;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public FriendData getFriend() {
		return friend;
	}

	public void setFriend(FriendData friend) {
		this.friend = friend;
	}

	public String toString() {
		return "(" + x + "," + y + ")" + friend.toString();
	}

}
