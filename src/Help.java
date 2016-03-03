import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Color;

public class Help extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the dialog.
	 */
	public Help() {
		setResizable(false);
		setBounds(100, 100, 539, 344);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.WHITE);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setBackground(new Color(102, 205, 170));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			{
				JTextArea txtrRegErGe = new JTextArea();
				txtrRegErGe.setFont(new Font("Times New Roman", Font.PLAIN, 13));
				txtrRegErGe.setText("Ayuda para el profesor:\r\n-En caso de salir del programa pulsando la tecla de cerrar ventana no\r\nse guardara ninguno de los tableros que esten en ejecucion, \r\npara que salga el dialogo preguntando que si se quieren guardar hay que salir en\r\nArchivo>Salir\r\n\r\n-Tanto el calculo de las solucion automatica como la implementacion de la\r\nanimacion corren en otro hilo diferente (SwingWorker<?,?>) para evitar que se \r\ncongele la pantalla.\r\n\r\n-El programa esta basado en ventanas de tama\u00F1o fijo, ni la ventana de resolucion\r\ncuando tiene un tablero cargado ni ninguna de las de edicion son redimensionables,\r\nsolo sera redimensionable la pantalla inicial.\r\n\r\n-El tama\u00F1o de ventana vendra dado por al numero de celdas que contenga, \r\ncada boton ocupa unos 100x100 pixeles,\r\nEN CASO DE CREAR UNA VENTANA DE MAYOR TAMA\u00D1O QUE LA PANTALLA\r\nESTA SE RESIMENSIONARA PROVOCANDO QUE ALGUNOS COMPONENTES NO\r\nSE VEAN CORRECTAMENTE.\r\ntambien pueden producirse este tipo de errores en SSOO en los que tienen un tama\u00F1o\r\nde ventana por defecto.\r\n\r\n-En las operaciones de \"Deshacer\" y \"Rehacer\" se colocara la ventana sobre la\r\nque se ha aplicado la operacion en el punto donde se creo.\r\n\r\nNotas:\r\n-Por convenio se recomienda guardar los tableros sin ningun tipo de extension.\r\n\r\n-Se ha definido un numero maximo de filas/columnas/bolas para hacer un juego mas\r\nagradable y evitar que se creen tableros muy complejos que sobrecarguen la aplicacion\r\n\r\n-Este juego ha basado su dise\u00F1o en los antiguos juegos en 8 bits, en especial el PAC-MAN\r\ncuyos fantansmas son los protagonistas en este juego.\r\n\r\n@manuel_fidalgo, 19/enero/2015.\r\n\r\nAyuda para el usuario:\r\nArchivo:\r\nNuevo: Abre una nueva ventana de edici\u00F3n \r\nCargar: Carga un solitario para resolver desde un fichero de texto\r\nSalvar: Graba el solitario actual al fichero asociado al mismo. \r\nSalvar como: Graba solitario con la funcion guardar como.\r\nSalir: Sale de la aplicaci\u00F3n.\r\n\r\nEditar:\r\nDeshacer. Deshace la \u00FAltima operaci\u00F3n que se ha realizado en la ventana activa.\r\nRehacer. Rehace una operaci\u00F3n previamente deshecha por el usuario.\r\nEditar: Abre en una ventana de edici\u00F3n el solitario actual que se est\u00E1 resolviendo. \r\n\r\nResolver:\r\nResolver: Abre en la ventana de resoluci\u00F3n el solitario que se est\u00E1 editando. \r\nResolver autom\u00E1ticamente. Resuelve autom\u00E1ticamente el solitario\r\n\r\nAyuda: mostrar\u00E1 una ayuda al usuario para usar el programa.");
				scrollPane.setViewportView(txtrRegErGe);
			}
		}
	}

}
