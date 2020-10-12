package edu.eci.cvds.sampleprj.dao;
import edu.eci.cvds.samples.entities.Item;

import java.util.List;


public interface ItemDAO {

    public void save(Item it)throws PersistenceException;

    Item load(int id) throws PersistenceException;

    List<Item> consultarItemsDisponibles() throws PersistenceException;

    void actualizarTarifa(int id, long tarifa) throws PersistenceException;

}
