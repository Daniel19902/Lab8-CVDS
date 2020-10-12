package edu.eci.cvds.sampleprj.dao;

public class PersistenceException extends Exception{
    public PersistenceException(String mensaje,Exception e){
        super(mensaje,e);
    }
    public PersistenceException(String mensaje){
        super(mensaje);
    }
}
