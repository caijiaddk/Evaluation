package displaySWT;

class SocialNetworkNode{
		
		int ID;
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

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public SocialNetworkNode(int id){
			ID = id;
		}
	}