package com.vjcspy.stockinfo.domain.tick;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TickRepository implements PanacheRepository<TickEntity> {
}
