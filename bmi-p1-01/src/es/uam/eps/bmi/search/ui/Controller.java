package es.uam.eps.bmi.search.ui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import es.uam.eps.bmi.search.SearchEngine;
import es.uam.eps.bmi.search.index.lucene.LuceneIndexBuilder;
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

		// activamos el file chooser
		interfaz.getFileChooser().setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		interfaz.getFileChooser().setDialogTitle("Elija una coleccion para crear un indice");
		interfaz.getFileChooser().setCurrentDirectory(new File("collections"));
		int result = interfaz.getFileChooser().showOpenDialog(interfaz);
		if (result == JFileChooser.APPROVE_OPTION) {

			JDialog dialog = new JDialog();

			// nada ams abrirse, empieza indexado
			dialog.addComponentListener(new ComponentAdapter() {
				public void componentShown(ComponentEvent e) {
					File collectionFile = interfaz.getFileChooser().getSelectedFile();

					LuceneIndexBuilder builder = new LuceneIndexBuilder();
					try {
						builder.build(collectionFile.getAbsolutePath(), "indexgui");
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					System.out.println("Controller.main()   " + collectionFile.getAbsolutePath());

					dialog.setVisible(false);
				}
			});

			dialog.setTitle("Indexando...");
			dialog.setSize(500, 0);
			dialog.setResizable(false);
			dialog.setModal(true); // bloquea la ventana
			dialog.setVisible(true);
		} else {
			interfaz.setVisible(false);
			interfaz.dispose();
			return;
		}

		// cargamos el indice
		final SearchEngine engine = new VSMEngine("indexgui");

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

			}
		};
		interfaz.getTextField().addActionListener(action);

		// listener de la tabla
		interfaz.getTable().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				boolean flag_web = false;

				JTextArea text = new JTextArea("");

				int rowIndex = interfaz.getTable().rowAtPoint(e.getPoint());
				String filepath = (String) interfaz.getTable().getModel().getValueAt(rowIndex, 1);

				// mostramos el contenido del documento
				try {

					// si es una web la abrimos con el navegador
					if (filepath.startsWith("http://") || filepath.startsWith("https://")) {
						Desktop.getDesktop().browse(new URI(filepath));
						flag_web = true;
					} else {
						FileReader r = new FileReader(filepath);
						BufferedReader modReader = new BufferedReader(r);

						String line;
						while ((line = modReader.readLine()) != null) {
							text.append(line + "\n");
						}

						modReader.close();
						r.close();
					}
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}

				if (flag_web == false) {
					JDialog dialog = new JDialog();

					text.setEditable(false);

					dialog.getContentPane().add(text);
					dialog.setSize(500, 500);
					dialog.setResizable(false);
					dialog.setModal(true); // bloquea la ventana
					dialog.setVisible(true);
				}
			}
		});
	}
}
