public class Ball {
	/**
	 * Uses the Array cardinal system
	 */
	public int x;
	public int y;
	public int ID;
	public static int IDold = 0;
	/**
	 * Used when the balls are set in the table 
	 */
	public Ball(int setx, int sety) {
		this.x = setx;
		this.y = sety;
		this.ID = ++IDold;
	}
	/**
	 * User when the balls are moved
	 */
	public Ball(int i, int j, int id) {
		this.x = i;
		this.y = j;
		this.ID = id;
	}
	/**
	 * Copies a ball
	 */
	public Ball(Ball b) {
		this.x = b.x;
		this.y = b.y;
		this.ID = b.ID;
	}
}

/*
StringBuffer sb = new StringBuffer();
for(int i=0; i<b_son.sols.size();i++){
	sb.append(Integer.toString(b_son.sols.get(i)));
}
String s = sb.toString();
int l=s.length();
if(l>=4){
	/*miramos las dos ultimas
	if(s.substring(l-2,l).equalsIgnoreCase(s.substring(l-4,l-2))) return true;
	if(l>=6){
		/*miramos las tres ultimas
		if(s.substring(l-3,l).equalsIgnoreCase(s.substring(l-6,l-3)))return true;
		if(l>=8){
			/*miramos las 4 ultimas
			if(s.substring(l-4,l).equalsIgnoreCase(s.substring(l-8,l-4))) return true;
		}
		if(l>=10){
			/miramos las 5 ultimas
			if(s.substring(l-5,l).equalsIgnoreCase(s.substring(l-10,l-5))) return true;
		}
	}
}
return false; //LOLXD
 */