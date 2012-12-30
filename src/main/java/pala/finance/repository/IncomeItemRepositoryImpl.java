package pala.finance.repository;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import java.util.Date;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.transaction.annotation.Transactional;

import pala.bean.IncomeItem;
import pala.bean.Item;
import pala.finance.ui.IncomeType;

/**
 * Spring Data Neo4j backed application context for Worlds.
 */
public class IncomeItemRepositoryImpl implements MyIncomeItemRepository {

    @Autowired private IncomeItemRepository itemRepository;

    @Override
    @Transactional
    public IncomeItem addIncomeItem(IncomeType type, double cost, Date date) {
    	
    	IncomeItem createdItem = new IncomeItem(type, cost, date);
    	itemRepository.save(createdItem);
    	return createdItem;
    }
    
    @Override
    @Transactional
    public void deleteIncomeItem(long id) {
    	IncomeItem item = itemRepository.findOne(id);
    	itemRepository.delete(item);
    }

    @Override
    public IncomeItem findIncomeItemNamed(String name) {
        return itemRepository.findByPropertyValue("name", name);
    }

    @Override
    public Iterable<IncomeItem> findWorldsWithMoons(int moonCount) {
        return itemRepository.findAllByPropertyValue("moon-index", "moons", moonCount);
    }

    @Override
    public Iterable<IncomeItem> exploreWorldsBeyond(Item homeWorld) {
        TraversalDescription traversal = Traversal.description().relationships(withName("REACHABLE_BY_ROCKET"), Direction.OUTGOING);
        return itemRepository.findAllByTraversal(homeWorld, traversal);
    }

	@Override
	public EndResult<IncomeItem> findAllItems() {
		// TODO Auto-generated method stub
		return itemRepository.findAll();
	}

}
