package es.uam.eps.bmi.search.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame {

	private JTextField textField;
	private JSpinner spinner;
	private JTable table;
	private JFileChooser fileChooser;
	private File collectionFile;

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		this.setBounds(100, 100, 934, 521);
		getContentPane().setLayout(null);

		// file chooser
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		// table
		String[] columns = new String[] { "score", "filepath" };
		Object[][] data = new Object[][] {};

		DefaultTableModel tmodel = new DefaultTableModel(data, columns) {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(tmodel);
		table.setBounds(12, 113, 914, 375);
		getContentPane().add(table);

		// textfield
		textField = new JTextField();
		textField.setBounds(465, 12, 449, 63);
		this.getContentPane().add(textField);
		textField.setColumns(10);

		// spinner
		SpinnerNumberModel model = new SpinnerNumberModel(5, 1, 9, 1);
		spinner = new JSpinner(model);
		spinner.setBounds(369, 12, 84, 63);
		getContentPane().add(spinner);

	}

	public JTextField getTextField() {
		return textField;
	}

	public JSpinner getSpinner() {
		return spinner;
	}

	public JTable getTable() {
		return table;
	}

	public JFileChooser getFileChooser() {
		return fileChooser;
	}

	public File getCollectionFile() {
		return collectionFile;
	}
}
