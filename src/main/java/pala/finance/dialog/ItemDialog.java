package pala.finance.dialog;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import pala.bean.Item;

public class ItemDialog extends JDialog {
	private JTextField txtName;
	private JTextField txtDescription;
	
	private boolean ok;

	/**
	 * @wbp.parser.constructor
	 */
	public ItemDialog(JFrame parent) {
		super(parent, "Item Dialog", true);
		if (parent != null) {
			Dimension parentSize = parent.getSize();
			Point p = parent.getLocation();
			setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		}
		getContentPane().setLayout(null);

		JLabel label = new JLabel("Item Name:");
		label.setBounds(10, 14, 71, 14);
		getContentPane().add(label);

		txtName = new JTextField();
		txtName.setColumns(10);
		txtName.setBounds(89, 11, 213, 20);
		getContentPane().add(txtName);

		JLabel label_1 = new JLabel("Description:");
		label_1.setBounds(10, 45, 71, 14);
		getContentPane().add(label_1);

		txtDescription = new JTextField();
		txtDescription.setColumns(10);
		txtDescription.setBounds(89, 42, 213, 20);
		getContentPane().add(txtDescription);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setOk(false);
				setVisible(false);
				dispose();
			}
		});
		btnCancel.setBounds(213, 86, 89, 23);
		getContentPane().add(btnCancel);

		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setOk(true);
				setVisible(false);
				dispose();
			}
		});
		btnOk.setBounds(111, 86, 89, 23);
		getContentPane().add(btnOk);
		setSize(355, 163);
	}
	
	public ItemDialog(JFrame parent, Item item) {
		this(parent);
		txtName.setText(item.getName());
		txtDescription.setText(item.getDescription());
	}

	public String getName() {
		return txtName.getText();
	}

	public String getDescription() {
		return txtDescription.getText();
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}
}
