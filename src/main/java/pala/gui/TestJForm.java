/*
 * Created by JFormDesigner on Sun Jun 17 23:19:29 ICT 2012
 */

package pala.gui;

import java.awt.*;
import javax.swing.*;

/**
 * @author Phuoc Dang
 */
public class TestJForm extends JPanel {
	public TestJForm() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Phuoc Dang
		label1 = new JLabel();
		scrollPane1 = new JScrollPane();
		table1 = new JTable();
		button1 = new JButton();
		formattedTextField1 = new JFormattedTextField();

		//======== this ========

		// JFormDesigner evaluation mark
		setBorder(new javax.swing.border.CompoundBorder(
			new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
				"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
				java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

		setLayout(null);

		//---- label1 ----
		label1.setText("text");
		add(label1);
		label1.setBounds(20, 15, 75, label1.getPreferredSize().height);

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(table1);
		}
		add(scrollPane1);
		scrollPane1.setBounds(40, 80, 275, 140);

		//---- button1 ----
		button1.setText("text");
		add(button1);
		button1.setBounds(new Rectangle(new Point(80, 15), button1.getPreferredSize()));
		add(formattedTextField1);
		formattedTextField1.setBounds(95, 250, 90, formattedTextField1.getPreferredSize().height);

		{ // compute preferred size
			Dimension preferredSize = new Dimension();
			for(int i = 0; i < getComponentCount(); i++) {
				Rectangle bounds = getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
			}
			Insets insets = getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			setMinimumSize(preferredSize);
			setPreferredSize(preferredSize);
		}
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Phuoc Dang
	private JLabel label1;
	private JScrollPane scrollPane1;
	private JTable table1;
	private JButton button1;
	private JFormattedTextField formattedTextField1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
