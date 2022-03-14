package com.promineotech.jeep.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import lombok.extern.slf4j.Slf4j;

@Service
@Component
@Slf4j
public class DefaultJeepSalesDao implements JeepSalesDao {

  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;
  
  @Override
  public List<Jeep> fetchJeeps(JeepModel model, String trim) {
    log.info("Dao: model={}, trim={}", model, trim);
    
    String sql = "" + "SELECT * "  + "FROM models " + "WHERE model_id = :model_id" 
    + " AND trim_level = :trim_level";
    
    Map<String, Object> params = 
        new HashMap<>();
        params.put("model_id", model.toString());
        params.put("trim_level", trim);
    
    return jdbcTemplate.query(sql, params, new RowMapper<>() {

      @Override
      public Jeep mapRow(ResultSet rs, int rowNum) throws SQLException {
        //formatter:off
        
        return Jeep.builder()
            .modelPK(rs.getInt("model_pk"))
            .modelId(JeepModel.valueOf(rs.getString("model_id")))
            .trimLevel(rs.getString("trim_level"))
            .numDoors(rs.getInt("num_doors"))
            .wheelSize(rs.getInt("wheel_size"))
            .basePrice(rs.getBigDecimal("base_price"))
            .build();
        //formatter:on
      }});
  }

}
