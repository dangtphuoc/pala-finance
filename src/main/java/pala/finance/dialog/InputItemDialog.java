package pala.finance.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

import net.sf.nachocalendar.CalendarFactory;
import net.sf.nachocalendar.components.DateField;

import org.joda.time.DateTime;

import pala.bean.InputItem;
import pala.bean.Item;
import pala.common.utillity.ErrorConstants;
import pala.finance.handler.CallBackHandler;
import pala.finance.utility.UIUtil;

public class InputItemDialog extends JDialog {
	
	private boolean ok;
	private JComboBox cbxItem;
	private JFormattedTextField txtCost;
	private DateField dfTime;
	private InputItem inputItem;
	private JTextPane txtDetail;
	private File attachedFile;
	private JFileChooser fc = new JFileChooser();
	private JTextField txtAttachedFile;
	private CallBackHandler  handler;

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
		btnCancel.setBounds(209, 257, 89, 23);
		getContentPane().add(btnCancel);

		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setOk(true);
				if(handler != null) {
					InputItem inputItem = getInputItem();
					int result = handler.addInputItem(inputItem);
					if(result == ErrorConstants.SUCCESS) {
						clearDialog();
					}
				} else {
					setVisible(false);
					dispose();
				}
			}
		});
		btnOk.setBounds(110, 257, 89, 23);
		getContentPane().add(btnOk);
		
		JLabel label = new JLabel("Item:");
		label.setBounds(10, 14, 90, 14);
		getContentPane().add(label);
		
		cbxItem = new JComboBox();
		cbxItem.setBounds(110, 14, 188, 17);
		getContentPane().add(cbxItem);
		
		JLabel label_1 = new JLabel("Cost:");
		label_1.setBounds(10, 185, 90, 14);
		getContentPane().add(label_1);
		
		txtCost = new JFormattedTextField(NumberFormat.getNumberInstance());
		txtCost.setColumns(10);
		txtCost.setBounds(110, 185, 188, 20);
		getContentPane().add(txtCost);
		
		dfTime = CalendarFactory.createDateField();
		dfTime.setBounds(110, 216, 188, 20);
		getContentPane().add(dfTime);
		
		JLabel label_2 = new JLabel("Time:");
		label_2.setBounds(10, 219, 90, 14);
		getContentPane().add(label_2);
		
		JLabel lblDetail = new JLabel("Detail:");
		lblDetail.setBounds(10, 42, 90, 14);
		getContentPane().add(lblDetail);
		
		JLabel lblAttachment = new JLabel("Attachment:");
		lblAttachment.setBounds(10, 157, 90, 14);
		getContentPane().add(lblAttachment);
		
		txtDetail = new JTextPane();
		txtDetail.setBorder(new LineBorder(Color.LIGHT_GRAY));
		txtDetail.setBounds(110, 42, 329, 98);
		getContentPane().add(txtDetail);
		
		JButton btnAddAnImage = new JButton("Add image");
		btnAddAnImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int retValue = fc.showOpenDialog(InputItemDialog.this);
				if(retValue == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					if(file.exists() && file.isFile()) {
						attachedFile = file;
						txtAttachedFile.setText(file.getName());
						
					}
				}
			}
		});
		btnAddAnImage.setBounds(350, 153, 89, 23);
		getContentPane().add(btnAddAnImage);
		
		txtAttachedFile = new JTextField();
		txtAttachedFile.setEditable(false);
		txtAttachedFile.setBounds(110, 154, 230, 20);
		getContentPane().add(txtAttachedFile);
		txtAttachedFile.setColumns(10);
		setSize(505, 407);
		
		inputItem = new InputItem();
		UIUtil.initializeInputComboBox(cbxItem, false);
	}
	
	protected void clearDialog() {
		cbxItem.setSelectedIndex(0);
		txtDetail.setText("");
		txtAttachedFile.setText("");
		attachedFile = null;
		txtCost.setText("");
		//dfTime.setValue(new Date());
		inputItem = new InputItem();
	}

	public InputItemDialog(JFrame parent, InputItem item) {
		this(parent);
		inputItem = item;
		cbxItem.setSelectedItem(item.getItem());
		txtCost.setValue(item.getCost());
		dfTime.setValue(item.getDate());
		txtDetail.setText(item.getDetail());
		txtAttachedFile.setText(item.getAttachment());
	}

	public InputItemDialog(JFrame parent, CallBackHandler handler) {
		this(parent);
		this.handler = handler;
	}
	
	public InputItem getInputItem() {
		inputItem.setItem((Item) cbxItem.getSelectedItem());
		inputItem.setCost(((Number)txtCost.getValue()).longValue());
		Date dateTime = (Date) dfTime.getValue();
		DateTime jodaDateTime = new DateTime(dateTime);
		Date date = jodaDateTime.toLocalDate().toDate();
		inputItem.setDateTime(dateTime);
		inputItem.setDate(date);
		inputItem.setDetail(txtDetail.getText());
		if(attachedFile != null) {
			inputItem.setAttachment(attachedFile.getAbsolutePath());
		}
		return inputItem;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}
}
