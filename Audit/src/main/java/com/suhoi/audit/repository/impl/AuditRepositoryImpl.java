package com.suhoi.audit.repository.impl;

import com.suhoi.audit.model.Audit;
import com.suhoi.audit.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.suhoi.audit.util.ClickHouseQuery.GET_ALL_AUDIT_QUERY;
import static com.suhoi.audit.util.ClickHouseQuery.SAVE_AUDIT_QUERY;

@Repository
@RequiredArgsConstructor
public class AuditRepositoryImpl implements AuditRepository {

    private final DataSource dataSource;

    public Audit save(Audit audit) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SAVE_AUDIT_QUERY)) {
            ps.setObject(1, audit.getUuid());
            ps.setObject(2, audit.getUsername());
            ps.setObject(3, audit.getAction());
            ps.setObject(4, audit.getTimestamp());
            ps.executeUpdate();
            return audit;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Audit> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_ALL_AUDIT_QUERY)) {
            ResultSet resultSet = ps.executeQuery();
            List<Audit> audits = new ArrayList<>();
            while (resultSet.next()) {
                Audit audit = Audit.builder()
                        .uuid((UUID) resultSet.getObject("uuid"))
                        .username(resultSet.getString("username"))
                        .action(resultSet.getString("action"))
                        .timestamp(resultSet.getTimestamp("timestamp"))
                        .build();
                audits.add(audit);
            }
            return audits;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
