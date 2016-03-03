import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;

public class Game_Window {

	public ImageIcon vacia = new ImageIcon(getClass().getResource("vacia.jpg"));
	public ImageIcon circulo_vacio = new ImageIcon(getClass().getResource("circulo.jpg"));
	public ImageIcon circulo_objetivo = new ImageIcon(getClass().getResource("circulo_objetivo.jpg"));
	public ImageIcon objetivo = new ImageIcon(getClass().getResource("objetivo.jpg"));
	public ImageIcon animacion = new ImageIcon(getClass().getResource("icon.jpg"));

	private JFrame frmVentanaPrincipal;
	public static Game_Window esta_ventana;
	public Help help = new Help();
	public Declaration declaration;
	public Board current_board;
	public boolean saved=false;
//	public Solve solve;
	private File ficheroSeleccionado;
	public JPanel panel = new JPanel();
	public Image img = new Image();
	public int index_movimientos;
	public int index;
	public ArrayList<Board> tableros;
	JLabel lblNewLabel;
	/*cada vez que se cree una ventana de edicion se añadira aqui*/
	public ArrayList<Edition_Window> ventanas_edicion;


	ActionListener action_listener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			Object botonpulsado = e.getSource();
			if(botonpulsado instanceof JButton){
				/*Action Listener*/
				String text = ((JButton) botonpulsado).getToolTipText();
				String[] xy  = text.split(",");
				int x = Integer.parseInt(xy[0].trim());
				int y = Integer.parseInt(xy[1].trim());
				if(current_board.board[x][y].b!=null){
					borrarDeshechas();
					index++;
					index_movimientos=-1;
					Ball b = current_board.board[x][y].b;
					current_board.blow(b.ID);
					if(current_board.isSolved()) JOptionPane.showMessageDialog(frmVentanaPrincipal,"Tablero resuelto!");
					saved=false;
					tableros.add(current_board.copy());
					refresh();
				}
			}
		}
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game_Window window = new Game_Window();
					window.frmVentanaPrincipal.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Game_Window() {
		initialize();
		esta_ventana = this;
	}
	public static Game_Window getGame_Window(){
		if(esta_ventana != null){
			return esta_ventana;
		}else{
			return new Game_Window();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		ventanas_edicion = new ArrayList<Edition_Window>();
		frmVentanaPrincipal = new JFrame();
		frmVentanaPrincipal.setIconImage(Toolkit.getDefaultToolkit().getImage(Game_Window.class.getResource("icon.jpg")));
		//frmVentanaPrincipal.setBackground(new Color(245, 255, 250));
		frmVentanaPrincipal.setTitle("Ventana principal.");
		frmVentanaPrincipal.setBounds(10, 10, 900, 600);
		frmVentanaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);
		menuBar.setForeground(Color.WHITE);
		frmVentanaPrincipal.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Archivo");
		mnNewMenu.setBackground(Color.WHITE);
		menuBar.add(mnNewMenu);

		JMenuItem mntmNuevo = new JMenuItem("Nuevo");
		mntmNuevo.setBackground(Color.WHITE);
		mntmNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				declaration = new Declaration();
				declaration.setVisible(true);
			}
		});
		mnNewMenu.add(mntmNuevo);

		JMenuItem mntmCargar = new JMenuItem("Cargar");
		mntmCargar.setBackground(Color.WHITE);
		mntmCargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(current_board!=null && !saved){
					int selection = JOptionPane.showOptionDialog(
							null, 
							"El tablero actual no se ha guardado,\ndesea guardarlo?", 
							"Aviso", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.WARNING_MESSAGE, 
							null, 
							null, 
							null);
					if(selection==JOptionPane.YES_OPTION){
						guardar();
					}
				}
				JFileChooser fileChooser = new JFileChooser(".");
				int status = fileChooser.showOpenDialog(null);
				if (status == JFileChooser.APPROVE_OPTION) {
					ficheroSeleccionado = fileChooser.getSelectedFile();
					/*Leemos fichero selecionado*/
					Board board_readed = null;
					try {
						BufferedReader s = new BufferedReader(new FileReader (ficheroSeleccionado));
						board_readed = readFile(s);
						current_board = board_readed;
						drawBoard();
					} catch (Exception e1) {
						/*Error de solapacion o de formato*/
						JOptionPane.showMessageDialog(null, "Error en la lectura");
					}
				}
			}

		});
		mnNewMenu.add(mntmCargar);

		JMenuItem mntmSalvar = new JMenuItem("Salvar");
		mntmSalvar.setBackground(Color.WHITE);
		mntmSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardar();
			}
		});
		mnNewMenu.add(mntmSalvar);

		JMenuItem mntmSalvarComo = new JMenuItem("Salvar Como");
		mntmSalvarComo.setBackground(Color.WHITE);
		mntmSalvarComo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardarComo();
			}
		});
		mnNewMenu.add(mntmSalvarComo);

		JMenuItem mntmSalir = new JMenuItem("Salir");
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean areSaved=true;
				Edition_Window ew;
				for(int i=0; i<ventanas_edicion.size();i++){
					ew = ventanas_edicion.get(i);
					if(ew!=null && !ew.saved)areSaved = false;
				}
				if(!saved)areSaved=false;
				if(areSaved){
					System.exit(0);
				}else{
					int selection = JOptionPane.showOptionDialog(
							null, 
							"Uno o varios taberos no han sido guardados."
									+ "¿Quiere guardarlos?",  
									"Aviso", 
									JOptionPane.YES_NO_OPTION, 
									JOptionPane.WARNING_MESSAGE, 
									null, 
									null, 
									null);
					if(selection == JOptionPane.YES_OPTION){
						/*guardamos todos los tableros acivos*/
						if(!saved){
							guardar();
						}
						Edition_Window w;
						for(int i=0; i<ventanas_edicion.size();i++){
							w = ventanas_edicion.get(i);
							if(w!=null && !w.saved){
								w.guardar();
								w.setVisible(false);
							}
						}
					}
					System.exit(0);
				}	
			}
		});
		mntmSalir.setBackground(Color.WHITE);
		mnNewMenu.add(mntmSalir);

		JMenu mnNewMenu_1 = new JMenu("Editar");
		mnNewMenu_1.setBackground(Color.WHITE);
		menuBar.add(mnNewMenu_1);

		JMenuItem mntmDeshacer = new JMenuItem("Deshacer");
		mntmDeshacer.setBackground(Color.WHITE);
		mntmDeshacer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deshacer();
			}
		});
		mnNewMenu_1.add(mntmDeshacer);

		JMenuItem mntmRehacer = new JMenuItem("Rehacer");
		mntmRehacer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rehacer();
			}
		});
		mntmRehacer.setBackground(Color.WHITE);
		mnNewMenu_1.add(mntmRehacer);

		JMenuItem mntmEditar = new JMenuItem("Editar");
		mntmEditar.setBackground(Color.WHITE);
		mntmEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Edition_Window ew = new Edition_Window();
				ew.current_editing_board = new Board(current_board.board.length,current_board.board[0].length);
				ew.current_editing_board.setNumElements(current_board.numelements);
				ew.drawBoard();
			}
		});
		mnNewMenu_1.add(mntmEditar);

		JMenu mnNewMenu_2 = new JMenu("Resolver");
		mnNewMenu_2.setBackground(Color.WHITE);
		menuBar.add(mnNewMenu_2);

		JMenuItem mntmResolver = new JMenuItem("Resolver");
		mntmResolver.setBackground(Color.WHITE);
		mntmResolver.setEnabled(false);
		mnNewMenu_2.add(mntmResolver);

		JMenuItem mntmResolverAutomaticamente = new JMenuItem("Resolver Automaticamente");
		mntmResolverAutomaticamente.setBackground(Color.WHITE);
		mntmResolverAutomaticamente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Solve solve = new Solve(current_board.copy());
				solve.setVisible(true);
			}
		});
		mnNewMenu_2.add(mntmResolverAutomaticamente);

		JMenu mnAyuda = new JMenu("Ayuda");
		mnAyuda.setBackground(Color.WHITE);
		menuBar.add(mnAyuda);

		JMenuItem mntmMostrarAyuda = new JMenuItem("Mostrar ayuda");
		mntmMostrarAyuda.setBackground(Color.WHITE);
		mntmMostrarAyuda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				help.setVisible(true);
			}
		});
		mnAyuda.add(mntmMostrarAyuda);
		frmVentanaPrincipal.getContentPane().setLayout(new BorderLayout(0, 0));
		panel.setBackground(new Color(245, 255, 250));
		frmVentanaPrincipal.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		lblNewLabel = new JLabel("Crea tus propios tableros!");
		lblNewLabel.setBackground(Color.WHITE);
		frmVentanaPrincipal.getContentPane().add(lblNewLabel, BorderLayout.SOUTH);

		panel.add(img, BorderLayout.CENTER);
		panel.repaint();

	}

	protected void guardar() {
		if(ficheroSeleccionado==null){
			guardarComo();
		}else{
			escribirFichero();
		}
		saved=true;
	}

	private void escribirFichero() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(ficheroSeleccionado));
			String[] lineas = current_board.getLines();
			for(int i=0 ; i<4; i++){
				pw.println(lineas[i]);
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	protected void guardarComo() {
		JFileChooser jfile = new JFileChooser(".");
		jfile.setApproveButtonText("Guardar");
		int status = jfile.showOpenDialog(null);
		String ruta = ""; 
		try{ 
			if(status == JFileChooser.APPROVE_OPTION){ 
				ruta = jfile.getSelectedFile().getAbsolutePath(); 
				/*Escribir sobre el fichero selecionado*/
				File f = new File(ruta); 
				if(f.exists()){
					int selection = JOptionPane.showOptionDialog(
							null, 
							"Ya existe un fichero con ese nombre,\n¿Desea remplazarlo?", 
							"Aviso", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.WARNING_MESSAGE, 
							null, 
							null, 
							null);
					if(selection==JOptionPane.YES_OPTION){
						PrintWriter pw = new PrintWriter(new FileWriter(f));
						String[] lineas = current_board.getLines();
						for (int i = 0; i < 4; i++) {
							pw.println(lineas[i]);
						}
						pw.close();
					}
				}else{
					PrintWriter pw = new PrintWriter(new FileWriter(f));
					String[] lineas = current_board.getLines();
					for (int i = 0; i < 4; i++) {
						pw.println(lineas[i]);
					}
					pw.close();
				}
			}
		}catch (Exception ex){ 
			ex.printStackTrace(); 
		} 
		saved=true;
	}

	public void setDrawBoard(Board b) {
		current_board = b;
		drawBoard();

	}
	private void reDrawBoard() {
		panel.removeAll();
		panel.setLayout(new GridLayout(current_board.board.length,current_board.board[0].length));
		frmVentanaPrincipal.setBounds(100, 100, 520, 520);
		JButton celda;
		for(int i=0; i<this.current_board.board.length;i++){
			for(int j=0; j<this.current_board.board[i].length;j++){
				celda = this.current_board.board[i][j].boton;
				celda.setHorizontalTextPosition(SwingConstants.CENTER);
				celda.setVerticalTextPosition(SwingConstants.CENTER);
				/*si hay bola*/
				if(current_board.board[i][j].b!=null){
					celda.setIcon(circulo_vacio);
				}
				/*si hay posision Objetivo*/
				if(current_board.board[i][j].op!=null){
					celda.setIcon(objetivo);
				}
				/*No hay nada*/
				if(current_board.board[i][j].op==null && current_board.board[i][j].b==null ){
					celda.setIcon(vacia);
				}
				/*Ambas cosas */
				if(current_board.board[i][j].op!=null && current_board.board[i][j].b!=null ){
					celda.setIcon(circulo_objetivo);
				}
				celda.addActionListener(action_listener);
				celda.setSize(100,100);
				this.panel.add(celda);
			}
		}
		frmVentanaPrincipal.setSize(current_board.board[0].length*100+5,current_board.board.length*100+60);
		frmVentanaPrincipal.setResizable(false);
		panel.repaint();
	}



	public void drawBoard() {
		lblNewLabel.setText("Click en una bola para soplar!");
		tableros = new ArrayList<Board>();
		tableros.add(current_board.copy());
		panel.removeAll();
		panel.setLayout(new GridLayout(current_board.board.length,current_board.board[0].length));
		frmVentanaPrincipal.setBounds(100, 100, 520, 520);
		JButton celda;
		for(int i=0; i<this.current_board.board.length;i++){
			for(int j=0; j<this.current_board.board[i].length;j++){
				celda = this.current_board.board[i][j].boton;
				celda.setHorizontalTextPosition(SwingConstants.CENTER);
				celda.setVerticalTextPosition(SwingConstants.CENTER);
				/*si hay bola*/
				if(current_board.board[i][j].b!=null){
					celda.setIcon(circulo_vacio);
				}
				/*si hay posision Objetivo*/
				if(current_board.board[i][j].op!=null){
					celda.setIcon(objetivo);
				}
				/*No hay nada*/
				if(current_board.board[i][j].op==null && current_board.board[i][j].b==null ){
					celda.setIcon(vacia);
				}
				/*Ambas cosas */
				if(current_board.board[i][j].op!=null && current_board.board[i][j].b!=null ){
					celda.setIcon(circulo_objetivo);
				}
				celda.addActionListener(action_listener);
				celda.setSize(100,100);
				this.panel.add(celda);
			}
		}
		frmVentanaPrincipal.setSize(current_board.board[0].length*100+5,current_board.board.length*100+60);
		frmVentanaPrincipal.setResizable(false);
		panel.repaint();
	}
	private void refresh(){
		JButton celda;
		for(int i=0; i<this.current_board.board.length;i++){
			for(int j=0; j<this.current_board.board[i].length;j++){
				celda = this.current_board.board[i][j].boton;
				/*si hay bola*/
				if(current_board.board[i][j].b!=null){
					celda.setIcon(circulo_vacio);
				}
				/*si hay posision Objetivo*/
				if(current_board.board[i][j].op!=null){
					celda.setIcon(objetivo);
				}
				/*No hay nada*/
				if(current_board.board[i][j].op==null && current_board.board[i][j].b==null ){
					celda.setIcon(vacia);
				}
				/*Ambas cosas */
				if(current_board.board[i][j].op!=null && current_board.board[i][j].b!=null ){
					celda.setIcon(circulo_objetivo);
				}
				panel.add(celda);
			}
		}
	}

	public void disableButtons() {
		if(panel.getComponentCount()!=0){
			for(int i=0; i<current_board.board.length;i++){
				for(int j=0; j<current_board.board[i].length;j++){
					panel.remove(current_board.board[i][j].boton);
				}
			}
			panel.repaint();
		}
	}

	public void watchAnimation(final ArrayList<Integer> solucion) {

		disableButtons();
		SwingWorker<Void,Void> wk= new SwingWorker<Void,Void>(){
			@Override
			protected Void doInBackground() throws Exception {
				int[] xy;
				for(int i =0 ; i<solucion.size();i++){
					try{
						xy = current_board.buscarBola(solucion.get(i));
						JButton celda = current_board.board[xy[0]][xy[1]].boton;
						celda.setIcon(animacion);
						panel.repaint();
						Thread.sleep(500);
						current_board.blow(solucion.get(i));
						panel.repaint();
						refresh();
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch(java.lang.ArrayIndexOutOfBoundsException e1){
						System.out.println("Excetion estraña");
					}
				}
				return null;
			}

		};
		wk.execute();
	}
	public static Board readFile(BufferedReader s) throws ArrayIndexOutOfBoundsException, SolapacionException, IOException {
		int id=1;
		String[] lines = new String[4];
		for (int i = 0; i < lines.length; i++) {
			lines[i]= s.readLine();
		}
		/*Line #1*/
		String[] l1 = lines[0].split(" ");
		int[] l1int = new int[2];
		for (int i = 0; i < l1int.length; i++) {
			l1[i].trim();
			l1int[i]= Integer.parseInt(l1[i]);
		}
		Board b = new Board(l1int[0],l1int[1]);
		/*Line #2*/
		String[] l2 = lines[1].split(" ");
		int[] l2int = new int[1];

		for (int i = 0; i < l2int.length; i++) {
			l2[i].trim();
			l2int[i]= Integer.parseInt(l2[i]);
		}
		if(l2int.length!=1) throw new IOException();
		int pairs = l2int[0];
		b.setNumElements(pairs);
		/*Line #3*/
		String[] l3 = lines[2].split(" ");
		int[] l3int = new int[2*pairs];

		for (int i = 0; i < l3int.length; i=i+2) {
			l3[i].trim();
			l3int[i]=Integer.parseInt(l3[i]);
			l3[i+1].trim();
			l3int[i+1]=Integer.parseInt(l3[i+1]);
			b.setObjetive(setx(l3int[i]), sety(l3int[i+1]));
		}
		/*Line #4*/
		String[] l4 = lines[3].split(" ");
		int[] l4int = new int[2*pairs];

		for (int i = 0; i < l4int.length; i=i+2) {
			l4[i].trim();
			l4int[i]=Integer.parseInt(l4[i]);
			l4[i+1].trim();
			l4int[i+1]=Integer.parseInt(l4[i+1]);
			b.setBall(setx(l4int[i]), sety(l4int[i+1]),id++);
		}
		s.close();
		return b;
	}

	private static int sety(int i) {
		// TODO Auto-generated method stub
		return i-1;
	}

	private static int setx(int i) {
		// TODO Auto-generated method stub
		return i-1;
	}
	public void deshacer(){
		if((tableros.size()-1)+index_movimientos>=0){ /*Que a la bola a la que vayamos a acceder exista*/
			current_board = tableros.get((tableros.size()-1)+index_movimientos).copy(); /*sacamos la bola que sea en funcion del indicxe*/
			/*Decrementamos los indices de bolas y de movimientos*/
			index--;
			index_movimientos--;
			reDrawBoard();
		}
	}
	public void rehacer(){
		if(index_movimientos<-1 && (tableros.size())+index_movimientos>=0){
			index_movimientos++;
			index++;
			current_board = tableros.get((tableros.size())+index_movimientos).copy();
			reDrawBoard();
		}
	}
	public void borrarDeshechas(){
		if(index_movimientos<-1 && index>0){
			/*borramos de la lista las |index_movimientos|-1 ultimas bolas*/
			for(int i=0; i<Math.abs(index_movimientos)-1;i++){
				tableros.remove(tableros.size()-1);
			}
		}
	}

}
