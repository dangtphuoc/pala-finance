package pala.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;

import pala.bean.InputItem;
import pala.bean.Item;

/**
 * @author mh
 * @since 01.04.11
 */
public interface InputItemRepository extends MyInputItemRepository, GraphRepository<InputItem>, NamedIndexRepository<InputItem> {
}
