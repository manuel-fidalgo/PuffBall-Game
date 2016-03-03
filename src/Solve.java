import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;


import javax.swing.JProgressBar;

public class Solve extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	public ArrayList<Integer> solucion;
	JProgressBar progressBar;
	JLabel label;
	final JButton cancelButton;
	final JButton okButton;

	/**
	 * Create the dialog.
	 * @param solucion2 
	 */
	public Solve(final Board current_board) {
		setTitle("Resolucion automatica...");
		setBounds(100, 100, 424, 134);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		getContentPane().add(contentPanel, BorderLayout.CENTER);

		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			label = new JLabel("Buscando solucion, Esto puede tardar unos segundos...");
			contentPanel.add(label);
		}
		{
			progressBar = new JProgressBar();
			contentPanel.add(progressBar);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setEnabled(false);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Ver animacion.");
				cancelButton.setEnabled(false);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Game_Window w = Game_Window.getGame_Window();
						dispose();
						w.watchAnimation(solucion);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		/*Una vez creada la ventana...*/
		/*Usar swingworker para esto...*/

		SwingWorker<ArrayList<Integer>,Integer> worker = new SwingWorker<ArrayList<Integer>, Integer>(){
			@Override
			protected ArrayList<Integer> doInBackground() throws Exception {
				publish(50);
				ArrayList<Integer> sol = current_board.solve();
				publish(100);
				return sol;
			}
			@Override
			protected void done() {
				try {
					solucion = get();
					if(solucion==null){
						label.setText("No hay solucion.");
						okButton.setEnabled(true);
					}else{
						label.setText("La solucion mas optima es:\n"+solucion.toString());
						okButton.setEnabled(true);
						cancelButton.setEnabled(true);
					}
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override 
			protected void process(List<Integer> chunks){
				progressBar.setValue(chunks.get(0));
			}
		};
		worker.execute();
	}

}