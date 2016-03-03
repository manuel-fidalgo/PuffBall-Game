import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;


public class Edition_Window extends JFrame {

	/**
	 * 
	 */
	public ImageIcon vacia = new ImageIcon(getClass().getResource("vacia.jpg"));
	public ImageIcon circulo_vacio = new ImageIcon(getClass().getResource("circulo.jpg"));
	public ImageIcon circulo_objetivo = new ImageIcon(getClass().getResource("circulo_objetivo.jpg"));
	public ImageIcon objetivo = new ImageIcon(getClass().getResource("objetivo.jpg"));

	public JMenu mnNewMenu_2;
	private static final long serialVersionUID = 1L;
	public static Edition_Window esta_ventana;
	private static File ficheroSeleccionado ;
	private JPanel contentPane;
	public Board current_editing_board;
	public boolean saved=false;
	public JPanel panel = new JPanel();
	public JPanel panel_1;
	public ArrayList<Board> tableros;
	int index_movimientos=0;
	int index;

	/*ActionListener de todas las celdas de la ventana editar*/
	ActionListener al = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			Object botonpulsado = e.getSource();
			if(botonpulsado instanceof JButton){
				borrarDeshechas();
				saved=false;
				index_movimientos=-1;
				index++;
				String text = ((JButton) botonpulsado).getToolTipText();
				String[] xy  = text.split(",");
				int x = Integer.parseInt(xy[0].trim());
				int y = Integer.parseInt(xy[1].trim());

				/*Creara una nueva bola en al punto de la matriz selecionado*/
				if(index<=current_editing_board.numelements){
					label.setText("Click en cualquier casilla para colocar la bola "+Integer.toString(index+1)+".");
					try {
						/*Se usa este metodo para que no aumente sin querer el indice de la bola metida*/
						current_editing_board.setBall(x,y,index);
					} catch (SolapacionException e1) {
						JOptionPane.showMessageDialog(null,"No se pueden superponer bolas.");
						index--;
						label.setText("Click en cualquier casilla para colocar la bola "+Integer.toString(index+1)+".");
					}
					if(index==current_editing_board.numelements){
						label.setText("Click en cualquier casilla para colocar una posicion objetivo.");
					}
				}else if(index<=current_editing_board.numelements*2){
					try {
						current_editing_board.setObjetive(x,y);
					} catch (SolapacionException e1) {
						JOptionPane.showMessageDialog(null,"No se pueden superponer posiciones objetivo");
						index--;
					}
					if(index==current_editing_board.numelements*2){
						/*Posiciones llenas*/
						mnNewMenu_2.setEnabled(true);
						label.setText("Tablero completo, click en resolver");
					}
				}
				tableros.add(current_editing_board.copy());
				refresh();
			}
		}
	};
	private JLabel label = new JLabel("Click en cualquier casilla para colocar la bola 1.");


	/**
	 * Create the frame passing like parameter the principal window;
	 */
	public Edition_Window() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Edition_Window.class.getResource("icon.jpg")));
		/*creamos la lista de tableros dados para rehacer y deshacer*/
		tableros = new ArrayList<Board>();
		setTitle("Edicion de puzzle.");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 536, 490);
		setVisible(true);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Archivo");
		mnNewMenu.setBackground(Color.WHITE);
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("Nuevo");
		mntmNewMenuItem.setEnabled(false);
		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Cargar");
		mntmNewMenuItem_1.setBackground(Color.WHITE);
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(current_editing_board!=null && !saved){
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
					File ficheroSeleccionado = fileChooser.getSelectedFile();
					/*Leemos fichero selecionado*/
					Board board_readed=null;
					try {
						BufferedReader s = new BufferedReader(new FileReader (ficheroSeleccionado));
						board_readed = Game_Window.readFile(s);
						current_editing_board = board_readed;
						drawBoard();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Error en la lectura");
					} 
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Salvar");
		mntmNewMenuItem_2.setBackground(Color.WHITE);
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				guardar();
			}
		});
		mnNewMenu.add(mntmNewMenuItem_2);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Salvar como");
		mntmNewMenuItem_3.setBackground(Color.WHITE);
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardarComo();
			}
		});
		mnNewMenu.add(mntmNewMenuItem_3);

		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Salir");
		mntmNewMenuItem_4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Game_Window gw = Game_Window.getGame_Window();
				boolean areSaved=true;
				Edition_Window ew;
				for(int i=0; i<gw.ventanas_edicion.size();i++){
					ew = gw.ventanas_edicion.get(i);
					if(ew!=null && !ew.saved)areSaved = false;
				}
				if(!gw.saved)areSaved=false;
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
						for(int i=0; i<gw.ventanas_edicion.size();i++){
							w = gw.ventanas_edicion.get(i);
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
		mntmNewMenuItem_4.setBackground(Color.WHITE);
		mnNewMenu.add(mntmNewMenuItem_4);

		JMenu mnNewMenu_1 = new JMenu("Editar");
		mnNewMenu_1.setBackground(Color.WHITE);
		menuBar.add(mnNewMenu_1);

		JMenuItem mntmNewMenuItem_5 = new JMenuItem("Deshacer");
		mntmNewMenuItem_5.setBackground(Color.WHITE);
		mnNewMenu_1.add(mntmNewMenuItem_5);

		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deshacer();
			}
		});

		JMenuItem mntmNewMenuItem_6 = new JMenuItem("Rehacer");
		mntmNewMenuItem_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rehacer();
			}
		});
		mntmNewMenuItem_6.setBackground(Color.WHITE);
		mnNewMenu_1.add(mntmNewMenuItem_6);

		JMenuItem mntmNewMenuItem_7 = new JMenuItem("Editar");
		mntmNewMenuItem_7.setBackground(Color.WHITE);
		mntmNewMenuItem_7.setEnabled(false);
		mnNewMenu_1.add(mntmNewMenuItem_7);

		mnNewMenu_2 = new JMenu("Resolver");
		mnNewMenu_2.setEnabled(false);
		mnNewMenu_2.setBackground(Color.WHITE);
		menuBar.add(mnNewMenu_2);

		JMenuItem mntmResolver = new JMenuItem("Resolver");
		mntmResolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game_Window w = Game_Window.getGame_Window();
				Board b = current_editing_board.copy();
				w.setDrawBoard(b);
			}
		});
		mnNewMenu_2.add(mntmResolver);

		JMenuItem mntmNewMenuItem_8 = new JMenuItem("Resolver Automaticamente");
		mntmNewMenuItem_8.setBackground(Color.WHITE);
		mntmNewMenuItem_8.setEnabled(false);
		mnNewMenu_2.add(mntmNewMenuItem_8);

		JMenu mnNewMenu_3 = new JMenu("Ayuda");
		mnNewMenu_3.setBackground(Color.WHITE);
		menuBar.add(mnNewMenu_3);
		contentPane = new JPanel();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		label.setBackground(Color.WHITE);
		panel.add(label, BorderLayout.SOUTH);

		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel.add(panel_1, BorderLayout.CENTER);
		agnadir();
		esta_ventana=this;
	}
	private void agnadir() {
		Game_Window w = Game_Window.getGame_Window();
		w.ventanas_edicion.add(this);

	}
	private void reDrawBoard() {
		panel_1.removeAll();
		panel_1.setLayout(new GridLayout(current_editing_board.board.length,current_editing_board.board[0].length));
		setBounds(100, 100, 520, 520);
		JButton celda;
		for(int i=0; i<this.current_editing_board.board.length;i++){
			for(int j=0; j<this.current_editing_board.board[i].length;j++){
				celda = this.current_editing_board.board[i][j].boton;
				celda.setHorizontalTextPosition(SwingConstants.CENTER);
				celda.setVerticalTextPosition(SwingConstants.CENTER);
				/*si hay bola*/
				if(current_editing_board.board[i][j].b!=null){
					celda.setIcon(circulo_vacio);
				}
				/*si hay posision Objetivo*/
				if(current_editing_board.board[i][j].op!=null){
					celda.setIcon(objetivo);
				}
				/*No hay nada*/
				if(current_editing_board.board[i][j].op==null && current_editing_board.board[i][j].b==null ){
					celda.setIcon(vacia);
				}
				/*Ambas cosas */
				if(current_editing_board.board[i][j].op!=null && current_editing_board.board[i][j].b!=null ){
					celda.setIcon(circulo_objetivo);
				}
				celda.addActionListener(al);
				celda.setSize(100,100);
				this.panel_1.add(celda);
			}
		}
		setSize(current_editing_board.board[0].length*100+5,current_editing_board.board.length*100+60);
		setResizable(false);
		panel.repaint();
	}

	public void drawBoard() {
		JButton celda;
		panel_1.removeAll();
		tableros.add(current_editing_board.copy());
		/*Crea el layout*/
		panel_1.setLayout(new GridLayout(current_editing_board.board.length,current_editing_board.board[0].length));
		/*Recorre el tablero actual y lo dibuja*/
		for(int i=0; i<this.current_editing_board.board.length;i++){
			for(int j=0; j<this.current_editing_board.board[i].length;j++){
				celda = this.current_editing_board.board[i][j].boton;
				celda.setHorizontalTextPosition(SwingConstants.CENTER);
				celda.setVerticalTextPosition(SwingConstants.CENTER);
				celda.addActionListener(al);
				celda.setIcon(vacia);
				celda.setSize(100,100);
				this.panel_1.add(celda);
			}
		}
		setSize(current_editing_board.board[0].length*100+9,current_editing_board.board.length*100+60);
		setResizable(false);
		panel_1.repaint();
	}
	public void refresh(){
		JButton celda;
		for(int i=0; i<this.current_editing_board.board.length;i++){
			for(int j=0; j<this.current_editing_board.board[i].length;j++){
				celda = this.current_editing_board.board[i][j].boton;
				/*si hay bola*/
				if(current_editing_board.board[i][j].b!=null){
					celda.setIcon(circulo_vacio);
				}
				/*si hay posision Objetivo*/
				if(current_editing_board.board[i][j].op!=null){
					celda.setIcon(objetivo);
				}
				/*No hay nada*/
				if(current_editing_board.board[i][j].op==null && current_editing_board.board[i][j].b==null ){
					celda.setIcon(vacia);
				}
				/*Ambas cosas */
				if(current_editing_board.board[i][j].op!=null && current_editing_board.board[i][j].b!=null ){
					celda.setIcon(circulo_objetivo);
				}
			}
		}
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
			String[] lineas = current_editing_board.getLines();
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
						String[] lineas = current_editing_board.getLines();
						for (int i = 0; i < 4; i++) {
							pw.println(lineas[i]);
						}
						pw.close();
					}
				}else{
					PrintWriter pw = new PrintWriter(new FileWriter(f));
					String[] lineas = current_editing_board.getLines();
					for (int i = 0; i < 4; i++) {
						pw.println(lineas[i]);
					}
					pw.close();
				}
			}
		}catch (Exception ex){ 
			JOptionPane.showMessageDialog(null,"Ha ocurrido un error al guardar\n"); 
		} 
		saved=true;

	}
	public void deshacer(){
		if((tableros.size()-1)+index_movimientos>=0){ /*Que a la bola a la que vayamos a acceder exista*/
			current_editing_board = tableros.get((tableros.size()-1)+index_movimientos).copy(); /*sacamos la bola que sea en funcion del indicxe*/
			/*Decrementamos los indices de bolas y de movimientos*/
			index--;
			index_movimientos--;
			label.setText("");
			reDrawBoard();
		}
	}
	public void rehacer(){
		if(index_movimientos<-1 && (tableros.size()-1)+index_movimientos>=0){
			index_movimientos++;
			index++;
			current_editing_board = tableros.get((tableros.size())+index_movimientos).copy();
			label.setText("");
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