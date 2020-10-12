package edu.eci.cvds.sampleprj.dao.mybatis.mappers;

import java.util.Date;
import java.util.List;

import edu.eci.cvds.samples.entities.ItemRentado;
import org.apache.ibatis.annotations.Param;

import edu.eci.cvds.samples.entities.Cliente;

/**
 *
 * @author 2106913
 */
public interface ClienteMapper {
    
    Cliente consultarCliente(@Param("idcli") int id);
    
    /**
     * Registrar un nuevo item rentado asociado al cliente identificado
     * con 'idc' y relacionado con el item identificado con 'idi'
     * @param id
     * @param idit
     * @param fechainicio
     * @param fechafin 
     */
    void agregarItemRentadoACliente(@Param("idcli") long id,
                                           @Param("idit")int idit,
                                           @Param("fechainicio")Date fechainicio,
                                           @Param("fechafin")Date fechafin);


    /**
     * Consultar todos los clientes
     * @return 
     */
    public List<Cliente> consultarClientes();

    void registrarCliente(@Param("cliente") Cliente c);

    void vetarCliente(long docu, boolean estado);

    List<ItemRentado> consultarItems(@Param("idcliente") long id);

    
}
