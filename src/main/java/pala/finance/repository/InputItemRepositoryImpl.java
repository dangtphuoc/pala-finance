package pala.finance.repository;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.transaction.annotation.Transactional;

import pala.bean.InputItem;
import pala.bean.Item;

/**
 * Spring Data Neo4j backed application context for Worlds.
 */
public class InputItemRepositoryImpl implements MyInputItemRepository {

    @Autowired private InputItemRepository itemRepository;
    
    @Override
    @Transactional
    public void deleteInputItem(long id) {
    	
    	itemRepository.delete(itemRepository.findOne(id));
    }

    @Override
    public InputItem findInputItemNamed(String name) {
        return itemRepository.findByPropertyValue("name", name);
    }

    @Override
    public Iterable<InputItem> findWorldsWithMoons(int moonCount) {
        return itemRepository.findAllByPropertyValue("moon-index", "moons", moonCount);
    }

    @Override
    public Iterable<InputItem> exploreWorldsBeyond(Item homeWorld) {
        TraversalDescription traversal = Traversal.description().relationships(withName("REACHABLE_BY_ROCKET"), Direction.OUTGOING);
        return itemRepository.findAllByTraversal(homeWorld, traversal);
    }

	@Override
	public EndResult<InputItem> findAllItems() {
		return itemRepository.findAll();
	}

	@Override
	public InputItem addItem(InputItem item) {
		InputItem inputItem = itemRepository.save(item);
		if(inputItem != null && inputItem.getAttachment() != null) {
			File srcFile = new File(inputItem.getAttachment());
			File dstFile = new File("attachment/" + inputItem.getId() + "_" + srcFile.getName());
			try {
				FileUtils.copyFile(srcFile, dstFile);
				inputItem.setAttachment(dstFile.getName());
				itemRepository.save(inputItem);
			} catch (IOException e) {
				itemRepository.delete(inputItem);
				inputItem = null;
			}
		}
		return inputItem;
	}

	@Override
	public InputItem findByID(long id) {
		return itemRepository.findOne(id);
	}

	@Override
	public InputItem saveInputItem(InputItem item) {
		InputItem oldItem = itemRepository.findOne(item.getId());
		if(item.getAttachment() != null) {
			if(oldItem.getAttachment() == null || !oldItem.getAttachment().equalsIgnoreCase(item.getAttachment())) {
				File srcFile = new File(item.getAttachment());
				File dstFile = new File("attachment/" + item.getId() + "_" + srcFile.getName());
				try {
					//delete old attachment
					if(oldItem.getAttachment() != null) {
						File oldAttachment = new File (oldItem.getAttachment());
						oldAttachment.delete();
					}
					FileUtils.copyFile(srcFile, dstFile);
					item.setAttachment(dstFile.getName());
					item = itemRepository.save(item);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			item = itemRepository.save(item);
		}
		return item;
	}
}
