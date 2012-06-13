package pala.repository;

import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.transaction.annotation.Transactional;

import pala.bean.Item;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author mh
 * @since 01.04.11
 */
public interface MyItemRepository {

    @Transactional
    Item addItem(String name, String description);
    
    @Transactional
    void deleteItem(long id);

    Item findItemNamed(String name);

    Iterable<Item> findWorldsWithMoons(int moonCount);

    Iterable<Item> exploreWorldsBeyond(Item homeWorld);
    
    EndResult<Item> findAllItems();
}
