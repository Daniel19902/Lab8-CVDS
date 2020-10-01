/*
 * Copyright (C) 2015 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.cvds.samples.services.client;



import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ItemMapper;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ItemRentadoMapper;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.TipoItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 *
 * @author hcadavid
 */
public class MyBatisExample {

    /**
     * Método que construye una fábrica de sesiones de MyBatis a partir del
     * archivo de configuración ubicado en src/main/resources
     *
     * @return instancia de SQLSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }

    /**
     * Programa principal de ejempo de uso de MyBATIS
     * @param args
     * @throws SQLException 
     */
    public static void main(String args[]) throws SQLException, ParseException {
        SqlSessionFactory sessionfact = getSqlSessionFactory();
        SqlSession sqlss = sessionfact.openSession();
        ClienteMapper cm = sqlss.getMapper(ClienteMapper.class);
        ItemMapper ir = sqlss.getMapper(ItemMapper.class);
        ItemRentadoMapper im = sqlss.getMapper(ItemRentadoMapper.class);

        System.out.println("Consultar Clientes");

        System.out.println(cm.consultarClientes());

        System.out.println("Consultar cliente");
        System.out.println(cm.consultarCliente(2));

        cm.agregarItemRentadoACliente(1,2 ,new SimpleDateFormat("yyyy/MM/dd").parse("2022/09/28"),new SimpleDateFormat("yyyy/MM/dd").parse("2022/10/28"));
        System.out.println("item rentado agregado");

        System.out.println("Consultar item agregados a clientes");
        System.out.println(cm.consultarCliente(1));

        TipoItem tipoItem = new TipoItem(1,"Batman");
        ir.insertarItem(new Item(tipoItem,97804578,"Batman cvds","batman prueba",new SimpleDateFormat("yyy-MM-dd").parse("2020-10-01"),80,"cvd","accion"));
        System.out.println("Item insertado");

        System.out.println("consultar item 45612");
        System.out.println(ir.consultarItem(97804578));

        System.out.println("consultar items");
        System.out.println(ir.consultarItems());

        sqlss.commit();
        sqlss.close();

    }


}
