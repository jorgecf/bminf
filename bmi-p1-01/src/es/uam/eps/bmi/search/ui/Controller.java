package es.uam.eps.bmi.search.ui;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import es.uam.eps.bmi.search.SearchEngine;
import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.lucene.LuceneEngine;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.vsm.VSMEngine;

public class Controller {

	private static GUI interfaz;

	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		interfaz = new GUI();
		interfaz.setVisible(true);

		// cargamos el indice
		final SearchEngine engine = new VSMEngine("indexhockey");

		// listener del textfield
		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// obtenemos la query
				final String query = interfaz.getTextField().getText();
				final int cutoff = (int) interfaz.getSpinner().getValue();
				System.out.println("Query introducida: " + query + ", cutoff: " + cutoff);

				// realizamos la consulta

				SearchRanking ranked = null;
				try {
					ranked = engine.search(query, cutoff);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// pintamos los datos
				DefaultTableModel model = (DefaultTableModel) interfaz.getTable().getModel();

				// eliminamos entradas anteriores
				if (model.getRowCount() > 0) {
					for (int i = model.getRowCount() - 1; i > -1; i--) {
						model.removeRow(i);
					}
				}

				for (SearchRankingDoc result : ranked) {
					System.out.println("\t" + new TextResultDocRenderer(result));

					try {
						model.addRow(new Object[] { result.getScore(), result.getPath() });
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				// entrada pro defecto
				if (model.getRowCount() == 0) {
					model.addRow(new Object[] { 0, "no results found" });
				}

			}
		};
		interfaz.getTextField().addActionListener(action);

		// listener de la tabla
		interfaz.getTable().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				JTextArea text = new JTextArea("");
				int rowIndex = interfaz.getTable().rowAtPoint(e.getPoint());
				String filepath = (String) interfaz.getTable().getModel().getValueAt(rowIndex, 1);

				// mostramos el contenido del documento
				try {
					FileReader r = new FileReader(filepath);
					BufferedReader modReader = new BufferedReader(r);

					String line;
					while ((line = modReader.readLine()) != null) {
						text.append(line + "\n");
					}

					modReader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				interfaz.getDialog().getContentPane().add(text);
				interfaz.getDialog().setSize(500, 500);
				interfaz.getDialog().setResizable(false);
				interfaz.getDialog().setModal(true); // bloquea la ventana
														// principal
				interfaz.getDialog().setVisible(true);
			}
		});

	}

}
