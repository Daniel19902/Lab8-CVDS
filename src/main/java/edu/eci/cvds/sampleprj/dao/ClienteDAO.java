package edu.eci.cvds.sampleprj.dao;

import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;

import java.util.Date;
import java.util.List;

public interface ClienteDAO {

    public List<Cliente> consultarClientes() throws PersistenceException;

    public Cliente consultarCliente(long id)throws PersistenceException;

    public void agregarItemRentadoACliente(long idCliente,int idit, Date fechaInicio, Date fechaFin) throws PersistenceException;

    public void registrarCliente(Cliente cliente) throws PersistenceException;

    public void vetarCliente(long docu, boolean estado) throws PersistenceException;

    public List<ItemRentado> consultarItemsCliente(long idCliente) throws PersistenceException;
}
