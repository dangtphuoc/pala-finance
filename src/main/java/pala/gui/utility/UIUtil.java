package pala.gui.utility;

import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import pala.bean.Item;
import pala.repository.ApplicationContent;
import pala.repository.ItemRepositoryImpl;

public class UIUtil {
	public static void initializeInputComboBox(JComboBox cbxItem, boolean showAllItem) {
		ItemRepositoryImpl itemRepo = ApplicationContent.applicationContext.getBean(ItemRepositoryImpl.class);
		Iterator<Item> items = itemRepo.findAllItems().iterator();
		DefaultComboBoxModel model = (DefaultComboBoxModel)cbxItem.getModel();
		model.removeAllElements();
		if(showAllItem) {
			Item item = new Item(ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.cbxAllItem.text"), ResourceBundle.getBundle("pala.gui.messages").getString("MainApp.cbxAllItem.text"));
			model.addElement(item);
		}
		while(items.hasNext()) {
			Item item = items.next();
			if(item.isActive()) {
				model.addElement(item);
			}
		}
	}
}
