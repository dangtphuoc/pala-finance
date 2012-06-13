package pala.repository;

import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.transaction.annotation.Transactional;

import pala.bean.InputItem;
import pala.bean.Item;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * @author mh
 * @since 01.04.11
 */
public interface MyInputItemRepository {

    @Transactional
    InputItem addItem(Item item, double cost, Date date);
    
    @Transactional
    void deleteInputItem(long id);

    InputItem findInputItemNamed(String name);

    Iterable<InputItem> findWorldsWithMoons(int moonCount);

    Iterable<InputItem> exploreWorldsBeyond(Item homeWorld);
    
    EndResult<InputItem> findAllItems();
}
