package com.vjcspy.stockinfo.controller;

import com.vjcspy.stockinfo.domain.tick.TickEntity;
import com.vjcspy.stockinfo.domain.tick.TickDto;
import com.vjcspy.stockinfo.domain.tick.TickMapper;
import com.vjcspy.stockinfo.domain.tick.TickRepository;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Path("/ticks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TickResource {

    @Inject
    TickRepository repository;

    @Inject
    TickMapper tickMapper;

    @GET
    public Uni<List<TickDto>> getAll(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("size") @DefaultValue("20") int size) {
        return repository.findAll()
            .page(page, size)
            .list()
            .onItem().transform(list ->
                list.stream()
                    .map(tickMapper::toDto)
                    .collect(Collectors.toList())
            );
    }

    @GET
    @Path("/{id}")
    public Uni<TickDto> getById(@PathParam("id") Long id) {
        return repository.findById(id)
            .onItem().ifNotNull().transform(tickMapper::toDto)
            .onItem().ifNull().failWith(() -> new NotFoundException("Tick not found"));
    }

    @GET
    @Path("/symbol/{symbol}")
    public Uni<List<TickDto>> getBySymbol(
        @PathParam("symbol") String symbol,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("size") @DefaultValue("20") int size) {
        return repository.find("symbol", symbol)
            .page(page, size)
            .list()
            .onItem().transform(list ->
                list.stream()
                    .map(tickMapper::toDto)
                    .collect(Collectors.toList())
            );
    }

    @GET
    @Path("/symbol/{symbol}/date/{date}")
    public Uni<TickDto> getBySymbolAndDate(
        @PathParam("symbol") String symbol,
        @PathParam("date") LocalDate date) {
        return repository.find("symbol = ?1 and date = ?2", symbol, date)
            .firstResult()
            .onItem().ifNotNull().transform(tickMapper::toDto)
            .onItem().ifNull().failWith(() -> new NotFoundException("Tick not found for symbol and date"));
    }

    @GET
    @Path("/date/{date}")
    public Uni<List<TickDto>> getByDate(
        @PathParam("date") LocalDate date,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("size") @DefaultValue("20") int size) {
        return repository.find("date", date)
            .page(page, size)
            .list()
            .onItem().transform(list ->
                list.stream()
                    .map(tickMapper::toDto)
                    .collect(Collectors.toList())
            );
    }

    @GET
    @Path("/symbol/{symbol}/latest")
    public Uni<TickDto> getLatestBySymbol(@PathParam("symbol") String symbol) {
        return repository.find("symbol = ?1 order by date desc", symbol)
            .firstResult()
            .onItem().ifNotNull().transform(tickMapper::toDto)
            .onItem().ifNull().failWith(() -> new NotFoundException("No ticks found for symbol"));
    }

    @POST
    @Transactional
    public Uni<TickDto> create(TickDto dto) {
        if (dto == null) {
            return Uni.createFrom().failure(new BadRequestException("Invalid input"));
        }

        // Validate required fields
        if (dto.getSymbol() == null || dto.getDate() == null) {
            return Uni.createFrom().failure(new BadRequestException("Symbol and date are required"));
        }

        TickEntity entity = tickMapper.toEntity(dto);
        return repository.persist(entity)
            .replaceWith(tickMapper.toDto(entity));
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Uni<TickDto> update(@PathParam("id") Long id, TickDto dto) {
        if (dto == null) {
            return Uni.createFrom().failure(new BadRequestException("Invalid input"));
        }

        // Validate required fields
        if (dto.getSymbol() == null || dto.getDate() == null) {
            return Uni.createFrom().failure(new BadRequestException("Symbol and date are required"));
        }

        return repository.findById(id)
            .onItem().ifNull().failWith(() -> new NotFoundException("Tick not found"))
            .onItem().transform(entity -> {
                if (dto.getSymbol() != null) entity.symbol = dto.getSymbol();
                if (dto.getDate() != null) entity.date = dto.getDate();
                if (dto.getMeta() != null) entity.meta = dto.getMeta();
                return entity;
            })
            .call(entity -> repository.persist(entity))
            .map(tickMapper::toDto);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Uni<Void> delete(@PathParam("id") Long id) {
        return repository.deleteById(id)
            .replaceWithVoid();
    }

    @DELETE
    @Path("/symbol/{symbol}")
    @Transactional
    public Uni<Long> deleteBySymbol(@PathParam("symbol") String symbol) {
        return repository.delete("symbol", symbol);
    }

    @DELETE
    @Path("/symbol/{symbol}/date/{date}")
    @Transactional
    public Uni<Boolean> deleteBySymbolAndDate(
        @PathParam("symbol") String symbol,
        @PathParam("date") LocalDate date) {
        return repository.delete("symbol = ?1 and date = ?2", symbol, date)
            .onItem().transform(count -> count > 0);
    }
}
