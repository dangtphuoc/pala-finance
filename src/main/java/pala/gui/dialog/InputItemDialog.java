package pala.gui.dialog;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import pala.bean.InputItem;
import pala.bean.Item;
import pala.repository.ApplicationContent;
import pala.repository.ItemRepositoryImpl;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

import org.joda.time.DateTime;

import java.text.Format;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;

import net.sf.nachocalendar.components.DateField;
import net.sf.nachocalendar.CalendarFactory;

public class InputItemDialog extends JDialog {
	
	private boolean ok;
	private JComboBox cbxItem;
	private JFormattedTextField txtCost;
	private DateField dfTime;
	private InputItem inputItem;

	/**
	 * @wbp.parser.constructor
	 */
	public InputItemDialog(JFrame parent) {
		super(parent, "Item Dialog", true);
		if (parent != null) {
			Dimension parentSize = parent.getSize();
			Point p = parent.getLocation();
			setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		}
		getContentPane().setLayout(null);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setOk(false);
				setVisible(false);
				dispose();
			}
		});
		btnCancel.setBounds(165, 114, 89, 23);
		getContentPane().add(btnCancel);

		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setOk(true);
				setVisible(false);
				dispose();
			}
		});
		btnOk.setBounds(66, 114, 89, 23);
		getContentPane().add(btnOk);
		
		JLabel label = new JLabel("Item:");
		label.setBounds(10, 14, 46, 14);
		getContentPane().add(label);
		
		cbxItem = new JComboBox();
		cbxItem.setBounds(66, 11, 188, 17);
		getContentPane().add(cbxItem);
		
		JLabel label_1 = new JLabel("Cost:");
		label_1.setBounds(10, 45, 46, 14);
		getContentPane().add(label_1);
		
		txtCost = new JFormattedTextField(NumberFormat.getNumberInstance());
		txtCost.setColumns(10);
		txtCost.setBounds(66, 42, 188, 20);
		getContentPane().add(txtCost);
		
		dfTime = CalendarFactory.createDateField();
		dfTime.setBounds(66, 73, 188, 20);
		getContentPane().add(dfTime);
		
		JLabel label_2 = new JLabel("Time:");
		label_2.setBounds(10, 79, 46, 14);
		getContentPane().add(label_2);
		setSize(295, 213);
		
		inputItem = new InputItem();
		initializeCxbItem();
	}
	
	private void initializeCxbItem() {
		ItemRepositoryImpl itemRepo = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
		Iterator<Item> items = itemRepo.findAllItems().iterator();
		DefaultComboBoxModel model = (DefaultComboBoxModel)cbxItem.getModel();
		model.removeAllElements();
		while(items.hasNext()) {
			Item item = items.next();
			if(item.isActive()) {
				model.addElement(item);
			}
		}
	}

	public InputItemDialog(JFrame parent, InputItem item) {
		this(parent);
		inputItem = item;
		cbxItem.setSelectedItem(item.getItem());
		txtCost.setValue(item.getCost());
		dfTime.setValue(item.getDate());
	}

	public InputItem getInputItem() {
		inputItem.setItem((Item) cbxItem.getSelectedItem());
		inputItem.setCost(((Number)txtCost.getValue()).longValue());
		Date dateTime = (Date) dfTime.getValue();
		DateTime jodaDateTime = new DateTime(dateTime);
		Date date = jodaDateTime.toLocalDate().toDate();
		inputItem.setDateTime(dateTime);
		inputItem.setDate(date);
		return inputItem;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}
}
