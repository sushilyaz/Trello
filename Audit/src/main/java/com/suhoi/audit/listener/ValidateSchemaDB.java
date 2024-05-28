package com.suhoi.audit.listener;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.suhoi.audit.util.ClickHouseQuery.CREATE_TABLE_IF_NOT_EXIST_QUERY;

@Component
@RequiredArgsConstructor
public class ValidateSchemaDB implements ApplicationListener<ContextRefreshedEvent> {

    private final DataSource dataSource;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(CREATE_TABLE_IF_NOT_EXIST_QUERY)) {
            ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
