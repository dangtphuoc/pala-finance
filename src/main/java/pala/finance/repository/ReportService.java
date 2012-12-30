package pala.finance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;

import pala.bean.InputItem;
import pala.bean.ReportByItemResult;
import pala.bean.ReportByMonthResult;

import java.util.Date;
import java.util.List;

/**
 * @author mh
 * @since 02.04.11
 */
public interface ReportService extends GraphRepository<InputItem>,
        NamedIndexRepository<InputItem>,
        RelationshipOperationsRepository<InputItem> {
	InputItem findById(String id);

    @Query("start n=node:__types__(className=\"pala.bean.InputItem\") where n.dateTime >= {0} and n.dateTime <= {1} return n.date, sum(n.cost) order by n.date")
	public List<ReportByMonthResult> reportByMonth(String fromDate, String toDate);
    
    @Query("start n=node:__types__(className=\"pala.bean.InputItem\") match n-->item where n.dateTime >= {0} and n.dateTime <= {1} return item.name, sum(n.cost) order by item.name")
	public List<ReportByItemResult> reportByItem(String fromDate, String toDate);
}
