package com.example.pokemonregions.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PokemonTeam implements Serializable {
    private String id;
    private String name;
    private int number;
    private String userId;
    private List<Pokemon> pokemons;

    public PokemonTeam(){
        this.id = "";
        this.name = "";
        this.number = 0;
        this.userId = "";
        this.pokemons = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PokemonTeam that = (PokemonTeam) o;
        return number == that.number && id.equals(that.id) && name.equals(that.name) && userId.equals(that.userId) && pokemons.equals(that.pokemons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, number, userId, pokemons);
    }

}
