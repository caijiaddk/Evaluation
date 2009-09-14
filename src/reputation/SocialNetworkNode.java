package reputation;

public class SocialNetworkNode{
		
		int ID;
		SocialNetworkNode nextNode;
		boolean malicious;
		boolean checked;
		
		public boolean isMalicious() {
			return malicious;
		}

		public void setMalicious(boolean malicious) {
			this.malicious = malicious;
		}

		public int getID() {
			return ID;
		}

		public void setID(int id) {
			ID = id;
		}

		public SocialNetworkNode getNextNode() {
			return nextNode;
		}

		public void setNextNode(SocialNetworkNode nextNode) {
			this.nextNode = nextNode;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public SocialNetworkNode(SocialNetworkNode node ,int id){
			ID = id;
			nextNode = node;
		}
	}