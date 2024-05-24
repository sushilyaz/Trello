package com.suhoi.demo.container;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

public abstract class PostgresContainer {
    private static final String POSTGRES_IMAGE_VERSION = "postgres:16";

    protected static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>(POSTGRES_IMAGE_VERSION)
            .withDatabaseName("suhoiTest")
            .withUsername("suhoiTest")
            .withPassword("qwerty")
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5433), new ExposedPort(5432)))
            ));

    @BeforeAll
    public static void init() {
        System.out.println("start container");
        POSTGRES_CONTAINER.start();
    }
}
