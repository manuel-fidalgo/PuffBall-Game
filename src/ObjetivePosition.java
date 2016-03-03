public class ObjetivePosition {
	/**
	 * Uses the Array cardinal system
	 */
	public int x;
	public int y;

	public ObjetivePosition(int setx, int sety){
		this.x = setx;
		this.y = sety;
	}
	public ObjetivePosition(ObjetivePosition o){
		this.x = o.x;
		this.y = o.y;

	}
}