package pala.finance.utility;

import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import pala.bean.Item;
import pala.finance.repository.ItemRepository;

@Component
public class UIUtil {
	private static ApplicationContext applicationContext;
	public static void initializeInputComboBox(JComboBox cbxItem, boolean showAllItem) {
		ItemRepository itemRepo = applicationContext.getBean(ItemRepository.class);
		Iterator<Item> items = itemRepo.findAllItems().iterator();
		DefaultComboBoxModel model = (DefaultComboBoxModel)cbxItem.getModel();
		model.removeAllElements();
		if(showAllItem) {
			Item item = new Item(ResourceBundle.getBundle("pala.finance.messages").getString("MainApp.cbxAllItem.text"), ResourceBundle.getBundle("pala.finance.messages").getString("MainApp.cbxAllItem.text"));
			model.addElement(item);
		}
		while(items.hasNext()) {
			Item item = items.next();
			if(item.isActive()) {
				model.addElement(item);
			}
		}
	}
	
	@Autowired
	public void setApplicationContent(ApplicationContext applicationContext) {
		UIUtil.applicationContext = applicationContext;
	}
}
