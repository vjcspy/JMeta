package com.vjcspy.stockinfo.controller;

import com.vjcspy.stockinfo.domain.cor.CorEntity;
import com.vjcspy.stockinfo.domain.cor.CorDto;
import com.vjcspy.stockinfo.domain.cor.CorMapper;
import com.vjcspy.stockinfo.domain.cor.CorRepository;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.stream.Collectors;

@Path("/cor")
@Consumes(MediaType.APPLICATION_JSON)
public class CorResource {

    @Inject
    CorRepository repository;

    @GET
    public Uni<List<CorDto>> getAll(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("size") @DefaultValue("20") int size) {
        return repository.findAll()
            .page(page, size)
            .list()
            .onItem().transform(list ->
                list.stream()
                    .map(CorMapper::toDTO)
                    .collect(Collectors.toList())
            );
    }

    @GET
    @Path("/{id}")
    public Uni<CorDto> getById(@PathParam("id") Long id) {
        return repository.findById(id)
            .onItem().ifNotNull().transform(CorMapper::toDTO)
            .onItem().ifNull().failWith(() -> new NotFoundException("CorEntity not found"));
    }

    @POST
    public Uni<CorDto> create(CorDto dto) {
        CorEntity entity = CorMapper.toEntity(dto);
        return repository.persist(entity)
            .replaceWith(CorMapper.toDTO(entity));
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Uni<CorDto> update(@PathParam("id") Long id, CorDto dto) {
        if (dto == null) {
            return Uni.createFrom().failure(new BadRequestException("Invalid input"));
        }

        // Validate required fields
        if (dto.code == null || dto.exchange == null || dto.name == null || dto.totalShares == null) {
            return Uni.createFrom().failure(new BadRequestException("Required fields cannot be null"));
        }

        return repository.findById(id)
            .onItem().ifNull().failWith(() -> new NotFoundException("CorEntity not found"))
            .onItem().transform(entity -> {
                // Update only non-null fields
                if (dto.code != null) entity.code = dto.code;
                if (dto.exchange != null) entity.exchange = dto.exchange;
                if (dto.name != null) entity.name = dto.name;
                if (dto.refId != null) entity.refId = dto.refId;
                if (dto.catId != null) entity.catId = dto.catId;
                if (dto.industryName1 != null) entity.industryName1 = dto.industryName1;
                if (dto.industryName2 != null) entity.industryName2 = dto.industryName2;
                if (dto.industryName3 != null) entity.industryName3 = dto.industryName3;
                if (dto.totalShares != null) entity.totalShares = dto.totalShares;
                if (dto.firstTradeDate != null) entity.firstTradeDate = dto.firstTradeDate;
                return entity;
            })
            .call(entity -> repository.persist(entity))
            .map(CorMapper::toDTO);
    }

    @DELETE
    @Path("/{id}")
    public Uni<Void> delete(@PathParam("id") Long id) {
        return repository.deleteById(id)
            .replaceWithVoid();
    }
}
