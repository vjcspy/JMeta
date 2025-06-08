package com.vjcspy.stockinfo.model.tick;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TickEntityRepository implements PanacheRepository<TickEntity> {
}
