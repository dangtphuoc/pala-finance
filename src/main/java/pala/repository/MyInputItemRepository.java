package pala.repository;

import org.joda.time.DateTime;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.transaction.annotation.Transactional;

import pala.bean.InputItem;
import pala.bean.Item;
import pala.bean.ReportByMonthResult;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author mh
 * @since 01.04.11
 */
public interface MyInputItemRepository {
    
    @Transactional
    InputItem addItem(InputItem item);
    
    @Transactional
    InputItem saveInputItem(InputItem item);
    
    @Transactional
    void deleteInputItem(long id);

    InputItem findInputItemNamed(String name);

    Iterable<InputItem> findWorldsWithMoons(int moonCount);

    Iterable<InputItem> exploreWorldsBeyond(Item homeWorld);
    
    EndResult<InputItem> findAllItems();
    
    InputItem findByID(long id);
}
