package pala.repository;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import static org.springframework.data.neo4j.examples.hellograph.RelationshipTypes.REACHABLE_BY_ROCKET;

import java.util.Iterator;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.transaction.annotation.Transactional;

import pala.bean.Item;

/**
 * Spring Data Neo4j backed application context for Worlds.
 */
public class ItemRepositoryImpl implements MyItemRepository {

    @Autowired private ItemRepository itemRepository;

    @Override
    @Transactional
    public Item addItem(String name, String description) {
    	
        Item createdItem = findItemNamed(name);
        if (createdItem == null) {
        	createdItem = new Item(name, description);
        	itemRepository.save(createdItem);
        }
    	return createdItem;
    }
    
    @Override
    @Transactional
    public void deleteItem(long id) {
    	Item item = itemRepository.findOne(id);
    	if(item != null) {
    		item.setActive(false);
    		itemRepository.save(item);
    	}
    }

    @Override
    public Item findItemNamed(String name) {
        return itemRepository.findByPropertyValue("name", name);
    }

    @Override
    public Iterable<Item> findWorldsWithMoons(int moonCount) {
        return itemRepository.findAllByPropertyValue("moon-index", "moons", moonCount);
    }

    @Override
    public Iterable<Item> exploreWorldsBeyond(Item homeWorld) {
        TraversalDescription traversal = Traversal.description().relationships(withName(REACHABLE_BY_ROCKET), Direction.OUTGOING);
        return itemRepository.findAllByTraversal(homeWorld, traversal);
    }

	@Override
	public EndResult<Item> findAllItems() {
		// TODO Auto-generated method stub
		return itemRepository.findAll();
	}

}
