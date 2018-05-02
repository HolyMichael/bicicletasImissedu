package lojadebicicletas.v2.pkg0;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ClientObj implements Serializable{
    String ip;
    ArrayList<String> categorias;
    RMIClientInterface cli;
    
    ClientObj(String ip, RMIClientInterface cli){
        this.ip = ip;
        categorias = new ArrayList<>();
        this.cli=cli;
    }

    ClientObj(String ip, String categoria){
        this.ip = ip;
        categorias = new ArrayList<>();
        categorias.add(categoria);
    }
    
    public String getIp(){
        return ip;
    }

    public ArrayList getCategory(){
        return categorias;
    }

    public void insertCategory(String category) {
        categorias.add(category);
    }

    @Override
    public String toString() {
        return "ClientObj{" + "ip=" + ip + ", categorias=" + categorias + '}';
    }
}