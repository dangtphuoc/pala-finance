package pala.repository;

import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.transaction.annotation.Transactional;

import pala.bean.IncomeItem;
import pala.bean.InputItem;
import pala.bean.Item;
import pala.gui.IncomeType;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * @author mh
 * @since 01.04.11
 */
public interface MyIncomeItemRepository {

    @Transactional
    IncomeItem addIncomeItem(IncomeType type, double cost, Date date);
    
    @Transactional
    void deleteIncomeItem(long id);

    IncomeItem findIncomeItemNamed(String name);

    Iterable<IncomeItem> findWorldsWithMoons(int moonCount);

    Iterable<IncomeItem> exploreWorldsBeyond(Item homeWorld);
    
    EndResult<IncomeItem> findAllItems();
}
