package com.fpdual.service;
import java.sql.Connection;

public class FavoriteService {
    private final Connection connector;

    public FavoriteService(Connection connector) {
        this.connector = connector;
    }
}
