package pala.repository;

import org.springframework.transaction.annotation.Transactional;

import pala.bean.Item;

import java.util.Collection;

/**
 * @author mh
 * @since 01.04.11
 */
public interface MyItemRepository {

    @Transactional
    Item addItem(String name, String description);

    Item findItemNamed(String name);

    Iterable<Item> findWorldsWithMoons(int moonCount);

    Iterable<Item> exploreWorldsBeyond(Item homeWorld);
}
