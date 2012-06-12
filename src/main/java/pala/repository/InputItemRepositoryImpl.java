package pala.repository;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import static org.springframework.data.neo4j.examples.hellograph.RelationshipTypes.REACHABLE_BY_ROCKET;

import java.util.Date;
import java.util.Iterator;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
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
    public InputItem addItem(Item item, double cost, Date date) {
    	
    	InputItem createdItem = new InputItem(item, cost, date);
    	itemRepository.save(createdItem);
    	return createdItem;
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
        TraversalDescription traversal = Traversal.description().relationships(withName(REACHABLE_BY_ROCKET), Direction.OUTGOING);
        return itemRepository.findAllByTraversal(homeWorld, traversal);
    }

	@Override
	public EndResult<InputItem> findAllItems() {
		// TODO Auto-generated method stub
		return itemRepository.findAll();
	}

}
