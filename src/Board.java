import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class Board{
	public Cell board[][];
	public int numelements;
	private ArrayList<Integer> sols;
	public static boolean log=false;
	/**
	 * Creates a new board
	 * */
	public int[] buscarBola(int i){
		for (int j = 0; j < board.length; j++) {
			for (int j2 = 0; j2 < board[0].length; j2++) {
				if(board[j][j2].b != null && board[j][j2].b.ID==i){
					int [] a = {j,j2};
					return a;
				}
			}
		}
		return null;
	}
	public Board(int a, int b) {
		/*Crates the Array and inizialice the cells*/
		board = new Cell[a][b];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j]= new Cell(i,j);
			}
		}
		sols = new ArrayList<Integer>();
	}
	/**
	 * Copies a board;
	 */
	public Board(Board b) {
		numelements = b.numelements;
		board = new Cell[b.board.length][b.board[0].length];
		sols = new ArrayList<Integer>();

		for (int i = 0; i < b.sols.size(); i++) {
			this.sols.add(b.sols.get(i));
		}
		for (int i = 0; i < b.board.length; i++) {
			for (int j = 0; j < b.board[i].length; j++) {
				board[i][j] = new Cell(i,j);
				board[i][j].empty = b.board[i][j].empty;
				if(b.board[i][j].b!=null) board[i][j].b = new Ball(b.board[i][j].b);
				else  board[i][j].b = null;
				if(b.board[i][j].op !=null)board[i][j].op = new ObjetivePosition(b.board[i][j].op);
				else board[i][j].op = null;
			}
		}
	}
	public void setNumElements(int num){
		this.numelements = num;
	}
	/**
	 * Both uses the Array cardinality System
	 * and control if in the input any ball or objective is out of the board.
	 */
	public void setObjetive(int x, int y) throws SolapacionException, ArrayIndexOutOfBoundsException{
		/*A cell cant contain two elements of the same type*/
		if(board[x][y].op!=null) throw new SolapacionException();
		board[x][y].op = new ObjetivePosition(x,y);

	}
	/*Used the first time that a ball is created*/
	public void setBall(int x, int y) throws SolapacionException,ArrayIndexOutOfBoundsException {
		/*A cell cant contain two elements of the same type*/
		if(board[x][y].b!=null) throw new SolapacionException();
		board[x][y].b = new Ball(x,y);
		board[x][y].empty = false;
	}
	public void setBall(int x, int y, int index) throws SolapacionException,ArrayIndexOutOfBoundsException{
		if(board[x][y].b!=null) throw new SolapacionException();
		board[x][y].b = new Ball(x,y,index);
		board[x][y].empty = false;
	}
	/**
	 * the ball with "n" as "ID" blows and moves the others in the same row and column 
	 */
	public void blow(int n){
		/*Search the ball*/
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				/*this ball will puff the others...*/
				/*we have to move all the balls in the i row and j column*/
				if(board[i][j].b!=null && board[i][j].b.ID == n){
					/*if a<i move left, a=i no move and a>i move left*/
					/*LOOK OUT with the end of the board*/ //board[i][a] //board[b][j]
					/*the row, left part*/
					for (int a = 1; a < j; a++) {
						/*Copy the old ball into the new cell if the ball are empty*/
						if(board[i][a-1].b==null && board[i][a].b!=null){
							board[i][a-1].b = new Ball(i,a-1,board[i][a].b.ID);
							/*Deletes the old ball*/
							board[i][a].b=null;
						}
					}
					/*the row, rigth part*/
					for (int b = board[i].length-2; b>j; b--) {
						if(board[i][b+1].b==null && board[i][b].b!=null){
							board[i][b+1].b = new Ball(i,b+1,board[i][b].b.ID);
							board[i][b].b=null;
						}
					}

					/*the column, up part*/
					for (int c = 1; c < i; c++) {
						if(board[c-1][j].b==null && board[c][j].b!=null){
							board[c-1][j].b = new Ball(c+1,j,board[c][j].b.ID);
							board[c][j].b=null;
						}
					}
					/*the column, down part*/
					for (int d = board.length-2; d>i ; d--) {
						if(board[d+1][j].b==null && board[d][j].b!=null){
							board[d+1][j].b = new Ball(d+1,j,board[d][j].b.ID);
							board[d][j].b=null;
						}
					}
					/*Finishes the method for optimization*/
					return;
				}
			}
		}
	}
	/**
	 * Solves the board problem using "Level order method" 
	 * @return 
	 */
	public ArrayList<Integer> solve() {
		/*Arraylist whit all the boards in the grap*/
		ArrayList<Board> queue = new ArrayList<Board>();
		queue.add(this);
		int index = 0;
		/*if no are new sons means that the puzzle has no solutions*/
		out:
			while(index < queue.size()){
				Board b = queue.get(index);
				if(b.isSolved()){
					return b.sols;
				}
				index++;
				/*Creates all the sons*/
				ArrayList<Board> sons = b.giveSons();
				for(int a =0; a<sons.size(); a++ ){
					Board b_son = sons.get(a);
					/*if the son created is equal to the father, don add to the queue, neither if the soon is equal to other table with derived solutions*/
					if(!areEquals(b,b_son)){
						if(isNew(b_son,queue)){
							try{
								queue.add(b_son);
							}catch(java.lang.OutOfMemoryError e){
								//The queue is full
								break out;
							}
						}
					}
				}
			}
		//LLegados hasta aqui deducimo que no tiene solucion
		return null;
	}
	/**
	 * @param b_son the board that we are going to analyze
	 * @param b other board in the queue with the balls in the same positions
	 * @return true if is a loop
	 */
	private boolean isLoop(Board b_son, Board b){
		/*returns true if b.sols € b_son.sols*/
		/*b_son = a ; b = b*/
		StringBuffer sb_a = new StringBuffer();
		for(int i=0; i<b_son.sols.size();i++){
			sb_a.append(Integer.toString(b_son.sols.get(i)));
		}

		StringBuffer sb_b = new StringBuffer();
		for(int i=0; i<b.sols.size();i++){
			sb_b.append(Integer.toString(b.sols.get(i)));
		}
		String s_a = sb_a.toString();
		String s_b = sb_b.toString();
		int val = s_a.indexOf(s_b);

		if(val==0){
			return true; 
		}else{
			return false;
		}
	}
	/**
	 * Returns true if both are equal
	 * Compares the positions of the balls.
	 */
	private boolean areEquals(Board a, Board b){
		for (int i = 0; i < a.board.length; i++) {
			for (int j = 0; j < a.board[i].length; j++) {
				/*Compares if in [i][j] position are a ball in a and in b*/
				if((a.board[i][j].b != null && b.board[i][j].b == null) || (a.board[i][j].b == null && b.board[i][j].b != null)) return false;
				/*Compares if the balls are the same*/
				if(a.board[i][j].b != null && b.board[i][j].b != null){
					if(a.board[i][j].b.ID != b.board[i][j].b.ID){
						return false;
					}
				}
			}
		}
		return true;
	}
	/**
	 * Returns true if the board in argument isn't in the queue(comparing the partial solutions)
	 */
	public boolean isNew(Board b_son, ArrayList<Board> queue) {
		for (int i = 0; i < queue.size(); i++) {
			Board b = queue.get(i);
			if(areEquals(b_son,b)){	
				if(isLoop(b_son,b)){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns all the sons in the graph
	 */
	private ArrayList<Board> giveSons() {
		ArrayList<Board> sons = new ArrayList<Board>();
		for(int i=1; i<=this.numelements;i++){
			/*Copy the board, blow the current ball, add to the solutions list and add to the board list */
			Board b = this.copy();
			b.blow(i);
			b.sols.add(i);
			sons.add(b);
		}
		return sons;
	}
	/**
	 * Copies the board
	 */
	public Board copy() {
		return new Board(this);
	}
	/**
	 * @return true if the puzzle is solved
	 */
	public boolean isSolved(){
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if(board[i][j].op !=null && board[i][j].b == null) return false;
			}
		}
		return true;
	}
	@Override
	public String toString() {
		String fin = "\n";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				fin = fin + board[i][j].toString();
			}
			fin = fin+"\n";
		}
		return fin+sols.toString()+"\n\n";
	}
	/**
	Se escribiran con el formato de el ejercicio anterior (1,1)
	 */
	public String[] getLines() {
		String[] lines = new String[4];
		lines[0]= Integer.toString(board.length)+" "+Integer.toString(board[0].length);
		lines[1]= Integer.toString(numelements);

		/**/
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if(board[i][j].op!=null){
					sb.append(Integer.toString(i+1)+" "+Integer.toString(j+1)+" ");
				}
			}
		}
		lines[2]= sb.toString();
		lines[2].trim();
		StringBuffer sb_1 = new StringBuffer();
		for(int z=1; z<=numelements;z++){
			/*busca la bola z*/
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					if(board[i][j].b!=null){ 
						if (board[i][j].b.ID == z){
							sb_1.append(Integer.toString(i+1)+" "+Integer.toString(j+1)+" ");
						}
					}
				}
			}
		}
		lines[3] = sb_1.toString();
		lines[3].trim();
		return lines;
	}
}
/**
 * Cell class, each position of the board array will contain a cell object
 */
class Cell {
	/**
	 * Each cell can contain a ball, a cell or can be empty;
	 */
	public ImageIcon vacia = new ImageIcon(getClass().getResource("vacia.jpg"));
	Border borde = new LineBorder(Color.black,1);
	public ObjetivePosition op;
	public Ball b;
	public boolean empty;
	public JButton boton;
	int fil;
	int col;

	public Cell(int x, int y){
		empty = true;
		boton = new JButton(vacia);
		boton.setToolTipText(Integer.toString(x)+","+Integer.toString(y));
		boton.setSize(100,100);
		boton.setBorder(borde);
		
		fil=x;
		col=y;
	}
	public String toString() {
		if(this.op==null && this.b==null){
			return 	"[   ]";
		}else if(this.op != null && this.b !=null){
			return 	"[("+Integer.toString(this.b.ID) +")]";
		}else if(this.b != null){
			return 	"[ "+Integer.toString(this.b.ID)+" ]";
		}else{
			return 	"[( )]";
		}
	}

}