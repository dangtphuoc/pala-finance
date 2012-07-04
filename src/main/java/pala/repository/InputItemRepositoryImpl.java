package pala.repository;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import java.util.Date;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.transaction.annotation.Transactional;

import pala.bean.InputItem;
import pala.bean.Item;
import pala.gui.Month;

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
		
		return itemRepository.save(item);
	}

	@Override
	public InputItem findByID(long id) {
		return itemRepository.findOne(id);
	}

	@Override
	public InputItem saveInputItem(InputItem item) {
		return itemRepository.save(item);
	}
}
