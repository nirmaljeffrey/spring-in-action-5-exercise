package sia.tacocloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JdbcOrderRepository implements OrderRepository {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert orderInserter;
    private SimpleJdbcInsert orderTacosInserter;
    private ObjectMapper objectMapper;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderInserter = new SimpleJdbcInsert(jdbcTemplate).withTableName("taco_order").usingGeneratedKeyColumns("id");
        this.orderTacosInserter = new SimpleJdbcInsert(jdbcTemplate).withTableName("taco_order_tacos");
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Order save(Order order) {
        order.setPlacedAt(new Date());
        long orderId = saveOrderDetails(order);
        for(Taco taco : order.getTacos()){
            saveTacoForOrder(taco,orderId);
        }
        return order;
    }

    private long saveOrderDetails(Order order) {
        Map<String,Object> values = objectMapper.convertValue(order,Map.class);
        values.put("placedAt",order.getPlacedAt());
       return orderInserter.executeAndReturnKey(values).longValue();
    }


    private void saveTacoForOrder(Taco taco, long orderId){
        Map<String,Object> values =  new HashMap<>();
        values.put("tacoOrder",orderId);
        values.put("taco",taco.getId());
        orderTacosInserter.execute(values);
    }

}
