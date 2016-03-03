import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.SpinnerNumberModel;

public class Declaration extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	

	/**
	 * Create the dialog.
	 */
	public Declaration() {
		setTitle("Dimensiones");
		setBounds(100, 100, 273, 293);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setResizable(false);
		
		final JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(4, 1, 7, 1));
		spinner.setBounds(159, 39, 86, 20);
		spinner.setValue(4);
		contentPanel.add(spinner);
		
		final JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(4, 1, 13, 1));
		spinner_1.setBounds(159, 96, 86, 20);
		spinner_1.setValue(4);
		contentPanel.add(spinner_1);
		
		final JSpinner spinner_2 = new JSpinner();
		spinner_2.setModel(new SpinnerNumberModel(5, 1, 10, 1));
		spinner_2.setBounds(159, 154, 86, 20);
		spinner_2.setValue(5);
		contentPanel.add(spinner_2);
		
		JLabel lblNumeroDeFilas = new JLabel("Numero de filas:");
		lblNumeroDeFilas.setBounds(10, 35, 139, 28);
		contentPanel.add(lblNumeroDeFilas);
		
		JLabel lblNumeroDeColumnas = new JLabel("Numero de columnas:");
		lblNumeroDeColumnas.setBounds(10, 89, 139, 34);
		contentPanel.add(lblNumeroDeColumnas);
		
		JLabel lblNumeroDeBolas = new JLabel("Numero de bolas:");
		lblNumeroDeBolas.setBounds(10, 146, 118, 37);
		contentPanel.add(lblNumeroDeBolas);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.WHITE);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setBackground(new Color(102, 205, 170));
				okButton.setForeground(new Color(0, 0, 0));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						/*Revisar que los parametros sean correctos y crear el tablero*/
						int rows = (int) spinner.getValue();
						int columns = (int) spinner_1.getValue();
						int balls = (int) spinner_2.getValue();
						Edition_Window edit_win = new Edition_Window();
						edit_win.current_editing_board = new Board(rows,columns);
						edit_win.current_editing_board.setNumElements(balls);
						edit_win.drawBoard();
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setBackground(new Color(102, 205, 170));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
