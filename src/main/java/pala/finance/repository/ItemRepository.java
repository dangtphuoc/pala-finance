package pala.finance.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;

import pala.bean.Item;

/**
 * @author mh
 * @since 01.04.11
 */
public interface ItemRepository extends MyItemRepository, GraphRepository<Item>, NamedIndexRepository<Item> {
}
