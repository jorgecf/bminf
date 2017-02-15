package es.uam.eps.bmi.search.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.BoxLayout;
import javax.swing.JDialog;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class GUI extends JFrame {

	// private JFrame frame;
	private JTextField textField;
	private JSpinner spinner;
	private JTable table;
	private JDialog dialog;

	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { GUI window = new GUI();
	 * window.setVisible(true); } catch (Exception e) { e.printStackTrace(); } }
	 * }); }
	 */
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

		String[] columns = new String[] { "score", "filepath" };
		Object[][] data = new Object[][] { { 0, "no results found" } };

		DefaultTableModel tmodel = new DefaultTableModel(data, columns) {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(tmodel);
		table.setBounds(12, 113, 914, 375);
		getContentPane().add(table);

		textField = new JTextField();
		textField.setBounds(465, 12, 449, 63);
		this.getContentPane().add(textField);
		textField.setColumns(10);

		// spinner
		SpinnerNumberModel model = new SpinnerNumberModel(5, 1, 9, 1);
		spinner = new JSpinner(model);
		spinner.setBounds(369, 12, 84, 63);
		getContentPane().add(spinner);

		dialog = new JDialog();

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

	public JDialog getDialog() {
		return dialog;
	}

}
