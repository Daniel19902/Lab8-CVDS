package edu.eci.cvds.sampleprj.dao.mybatis;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.eci.cvds.sampleprj.dao.ClienteDAO;
import edu.eci.cvds.sampleprj.dao.PersistenceException;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ItemMapper;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.entities.TipoItem;
import org.apache.ibatis.annotations.Param;
import org.mybatis.guice.transactional.Transactional;


import java.sql.SQLException;


import java.util.Date;
import java.util.List;

public class MyBATISClienteDAO implements ClienteDAO {

    @Inject
    private ClienteMapper clienteMapper;

    @Override
    public List<Cliente> consultarClientes() throws PersistenceException {
        try {
            return clienteMapper.consultarClientes();
        }catch (org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al consular los clientes",e);
        }
    }

    @Override
    public Cliente consultarCliente(long id) throws PersistenceException {
        try{
            return clienteMapper.consultarCliente((int) id);
        }catch (org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al consultar cliente"+id,e);
        }
    }
    @Transactional
    @Override
    public void agregarItemRentadoACliente(long idCliente,int idit, Date fechaInicio, Date fechaFin) throws PersistenceException {
        try{
            clienteMapper.agregarItemRentadoACliente(idCliente, idit , fechaInicio, fechaFin);
        }catch (org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al agregar Item rentado al cliente"+idCliente, e);
        }
    }

    @Transactional
    @Override
    public void registrarCliente(Cliente cliente) throws PersistenceException {
        try{
            clienteMapper.registrarCliente(cliente);
        }
        catch(org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al registrar al cliente "+cliente.getNombre() ,e);
        }

    }
    @Transactional
    @Override
    public void vetarCliente(long docu, boolean estado) throws PersistenceException {
        try{
            clienteMapper.vetarCliente(docu, estado);
        }catch (org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al vetar cliente", e);
        }

    }

    @Override
    public List<ItemRentado> consultarItemsCliente(long idCliente) throws PersistenceException {
        try {
            return clienteMapper.consultarItems(idCliente);
        }catch (org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al consultar clientes", e);
        }
    }
}
