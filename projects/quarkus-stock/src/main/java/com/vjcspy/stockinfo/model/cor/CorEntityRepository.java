package com.vjcspy.stockinfo.model.cor;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CorEntityRepository implements PanacheRepository<CorEntity> {
}
