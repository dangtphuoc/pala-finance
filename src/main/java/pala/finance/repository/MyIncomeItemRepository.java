package pala.finance.repository;

import java.util.Date;

import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.transaction.annotation.Transactional;

import pala.bean.IncomeItem;
import pala.bean.Item;
import pala.finance.ui.IncomeType;

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
