package edu.eci.cvds.samples.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.eci.cvds.sampleprj.dao.*;

import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.entities.TipoItem;
import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquiler;
import org.mybatis.guice.transactional.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Singleton
public class ServiciosAlquilerImpl implements ServiciosAlquiler {

    @Inject
    private ItemDAO itemDAO;
    @Inject
    private ClienteDAO clienteDAO;
    @Inject
    private ItemRentadoDAO itemRentadoDAO;
    @Inject
    private TipoItemDAO tipoItemDAO;

    @Override
    public int valorMultaRetrasoxDia(int itemId) throws ExcepcionServiciosAlquiler{
        try {
            return (int) itemDAO.load(itemId).getTarifaxDia();
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("error al consultar multa retraso x dia", e);
        }

    }

    @Override
    public Cliente consultarCliente(long docu) throws ExcepcionServiciosAlquiler {
        try {
            return clienteDAO.consultarCliente(docu);
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("Error al consultar cliente con "+docu, e);
        }
    }

    @Override
    public List<ItemRentado> consultarItemsCliente(long idCliente) throws ExcepcionServiciosAlquiler {
        try {
            return clienteDAO.consultarItemsCliente(idCliente);
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("Error al consultar items rentados del cliente "+idCliente, e);
        }
    }

    @Override
    public List<Cliente> consultarClientes() throws ExcepcionServiciosAlquiler {
        try {
            return clienteDAO.consultarClientes();
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("Error al consultar clientes", e);
        }
    }

    @Override
    public Item consultarItem(int id) throws ExcepcionServiciosAlquiler {
        try {
            return itemDAO.load(id);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar el item "+id,ex);
        }
    }

    @Override
    public List<Item> consultarItemsDisponibles() throws ExcepcionServiciosAlquiler{
        try {
            return itemDAO.consultarItemsDisponibles();
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("Error al consultar el items disponibles ",e);
        }
    }

    @Override
    public long consultarMultaAlquiler(int iditem, Date fechaDevolucion) throws ExcepcionServiciosAlquiler {
        List<Cliente> clientes = consultarClientes();
        for (Cliente cliente : clientes) {
            ArrayList<ItemRentado> rentados = cliente.getRentados();
            for (ItemRentado rentado : rentados) {
                if (rentado.getItem().getId() == iditem) {
                    LocalDate fechafinrenta = rentado.getFechafinrenta().toLocalDate();
                    LocalDate fechadevolucion = fechaDevolucion.toLocalDate();
                    long diasRetraso = ChronoUnit.DAYS.between(fechafinrenta, fechadevolucion);
                    if (diasRetraso < 0) {
                        return 0;
                    }
                    return diasRetraso * valorMultaRetrasoxDia(rentado.getId());
                }
            }
        }
        throw  new ExcepcionServiciosAlquiler("El item"+iditem+"no se encuentra rentado");
    }

    @Override
    public TipoItem consultarTipoItem(int id) throws ExcepcionServiciosAlquiler {
        try{
            if(tipoItemDAO.load(id)==null){
                throw new ExcepcionServiciosAlquiler("el tipo item no existe");
            }
            return tipoItemDAO.load(id);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar el tipo de item" , ex);
        }
    }

    @Override
    public List<TipoItem> consultarTiposItem() throws ExcepcionServiciosAlquiler {
        try {
            return tipoItemDAO.loadTipoItems();
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("Error al consultar el tipos items", e);
        }
    }
    @Transactional
    @Override
    public void registrarAlquilerCliente(Date date, long docu, Item item, int numdias) throws ExcepcionServiciosAlquiler {
        try{
            if(clienteDAO.consultarCliente(docu)==null){
                throw new ExcepcionServiciosAlquiler("El cliente no existe") ;
            }
            int idItem = item.getId();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, numdias);
            clienteDAO.agregarItemRentadoACliente(docu,item.getId(),date,new java.sql.Date(calendar.getTime().getTime()));
        }  catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al agregar item rentado al cliente" , ex);
        }
    }
    @Transactional
    @Override
    public void registrarCliente(Cliente c) throws ExcepcionServiciosAlquiler {
        try {
            clienteDAO.registrarCliente(c);
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("Error al registrar cliente "+c.getNombre(), e);
        }
    }

    @Override
    public long consultarCostoAlquiler(int iditem, int numdias) throws ExcepcionServiciosAlquiler {
        try {
            if (itemDAO.load(iditem) == null ){
                throw new ExcepcionServiciosAlquiler("El Item no existe");
            }
            long tarifa = itemDAO.load(iditem).getTarifaxDia();
            return tarifa * numdias;
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("Error al consultar alquiler"+iditem, e);
        }
    }

    @Override
    public void actualizarTarifaItem(int id, long tarifa) throws ExcepcionServiciosAlquiler {
        try {
            if(itemDAO.load(id) == null){
                throw new ExcepcionServiciosAlquiler("No existe ese item.");
            }
            itemDAO.actualizarTarifa(id,tarifa);
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("Error al altualizar tarifa del item "+id, e);
        }
    }
    @Override
    public void registrarItem(Item i) throws ExcepcionServiciosAlquiler {
        try {
            itemDAO.save(i);
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("Error al registrar item "+i, e);
        }
    }

    @Override
    public void vetarCliente(long docu, boolean estado) throws ExcepcionServiciosAlquiler {
        try {
            if(clienteDAO.consultarCliente(docu)==null){
                throw new ExcepcionServiciosAlquiler("El cliente no existe");
            }
            clienteDAO.vetarCliente(docu, estado);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("No se pudo vetar al cliente", ex);
        }
    }
}