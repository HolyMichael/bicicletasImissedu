package lojadebicicletas.v2.pkg0;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import static java.rmi.server.RemoteServer.getClientHost;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;

public class Cliente{
    /*String nome;
    String categoria;
    String IP;
    
    Cliente(String n,String c, String ip){
        this.nome = n;
        this.categoria = c;
        this.IP = ip;
    }*/
    static String ip = "";
    static int port;
    static InetAddress host; // IP do Cliente
    static RMIServerInterface serverObject;
    static ArrayList<String> list = new ArrayList<>();
    //static ServerSocket serversocketa = null;
    //static Socket Cliente = new Socket (), Server = new Socket ();
    
    
    public static void main(String[] argv) {
        String serverName = "";
        System.setSecurityManager(new SecurityManager());
        if (argv.length != 1) {
            try {
                serverName = java.net.InetAddress.getLocalHost().getHostName();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        else {
            serverName = argv[0];
        }
        if (serverName.equals("")){
            System.out.println("usage: java RMIClient < host running RMI server>");
            System.exit(0);
        }
        try {
            //bind server object to object in client
            serverObject = (RMIServerInterface) Naming.lookup("//"+serverName+"/RMIImpl");
            //invoke method on server object
            //Date d = serverObject.getDate();
            System.out.println("RMI connection successful");
            // Comentar daqui
            /*host = InetAddress.getLocalHost();
            byte ipaux [] = host.getAddress();
            for(int n= 0;n<ipaux.length;n++){
                if (n>0) 
                    ip+=".";
                ip+=(ipaux[n] & 0xff);
            }*/
            // aqui e meter
            System.out.println("Seu ip:");
            ip = Ler.umaString();
            System.out.println("Seu portji:");
            port = Ler.umInt();
            System.out.println("Machine IP identified.");
            ClientLoop();
        }
        catch(Exception e) {
            System.out.println("Exception occured: " + e);
            System.exit(0);
        }
    }
    
    public void printOnClient (String s) throws java.rmi.RemoteException{
        System.out.println ("Message from server: " + s);
    }
    
    private static void ClientLoop(){
        ArrayList<Produto> produtos = new ArrayList<>();
        ArrayList<String> categorias = new ArrayList<>();
        File temp = new File("../../SavedFiles");
        if(!temp.exists()){
            temp.mkdir();
            System.out.println("Created Saved Files.");
        }
        try{
            ObjectInputStream oisprod = new ObjectInputStream(new FileInputStream("../../SavedFiles/produtos.txt"));
            produtos = (ArrayList<Produto>) oisprod.readObject();
        } catch(FileNotFoundException e){
            System.out.println(e.getMessage());
            System.out.println("File will be created when you create your first product");
        } catch(IOException e){
            System.out.println(e.getMessage());
        } catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            ObjectInputStream oiscatg = new ObjectInputStream(new FileInputStream("../../SavedFiles/categorias.txt"));
            categorias = (ArrayList<String>) oiscatg.readObject();
        } catch(FileNotFoundException e){
            System.out.println(e.getMessage());
            System.out.println("File will be created when you create your first product");
        } catch(IOException e){
            System.out.println(e.getMessage());
            System.exit(0);
        } catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        System.out.println("All loaded");
        int i;
        try{
            if(serverObject.registerClient(ip))
                System.out.println("Registered with " + ip);
            else
                System.out.println("Logged in with " + ip);
        }
        catch(RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("xd");
        }
        do{
            System.out.println("1 - Registar produto, 2 - Comprar produto, 3 - Produtos à venda, 4 - Comunicar com outro cara, 5 - exit");
            i = Ler.umInt();
            int counter = 1;
            switch(i){
                case 1:
                    System.out.println("    " + "Insira o nome:");
                    String nome = Ler.umaString();
                    System.out.println("    " + "insira a categoria:");
                    
                    if(categorias != null) //Mostrar Categorias
                        for(counter=1;counter<categorias.size()+1;counter++)
                            System.out.println("    " + counter + "- " +categorias.get(counter-1));
                    System.out.println("    " + (counter) + "- Nova Categoria");
                    
                    int choice = Ler.umInt();
                    if(choice == categorias.size()+1){ //Nova Categoria
                        System.out.println("    " + "    " + "Nome da nova categoria");
                        categorias.add(Ler.umaString());
                    }
                    System.out.println("    " + "stock inicial?");
                    int stock = Ler.umInt();
                    
                    
                    
                    produtos.add(new Produto(nome, categorias.get(choice-1), stock, ip)); //Adição do Produto
                    System.out.println("Produto adicionado com sucesso");
                    try {
                        if(serverObject.registerCategory(ip , categorias.get(choice-1)))
                            System.out.println("Nice");
                        else
                            System.out.println("Não nice");
                    } 
                    catch (RemoteException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 2:
                    System.out.println("Escreva a sua categoria:");
                    String op = Ler.umaString();
                    try {
                        ArrayList <String> cat = serverObject.getClientsSellingCategory(op,ip);
                        if(cat == null)
                            System.out.println("Categoria não encontrada foi adicionada à lista de memes");
                        else
                            for(int n = 0; n<cat.size();n++)
                                System.out.println(cat.get(n));
                    } catch (RemoteException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 3:
                    int j;
                    System.out.println("Produtos Disponíveis:");
                    for(j=0;j<produtos.size();j++)
                        System.out.println((j+1) + " - " + produtos.get(j));
                    System.out.println("Categorias disponíveis:");
                    for(j=0;j<categorias.size();j++)
                        System.out.println((j+1) + " - " + categorias.get(j));
                    break;
                case 4:
                    /*
                    String opc;
                    String Lere;
                    do{
                        System.out.println("Espera comunicação de alguém? [Y/N]");
                        opc = Ler.umaString();
                    }while(!opc.equals("Y") && !opc.equals("N"));
                    if(opc.equals("Y")){
                        System.out.println("Introduza as credeencias do outro cara.");
                        String ipamigo = Ler.umaString();
                        int portamigo = Ler.umInt();
                        try {
                            serversocketa = new ServerSocket(portamigo);
                            Server = serversocketa.accept();
                        }
                        catch (IOException e){
                            System.out.println(e.getMessage());
                        }
                        try {
                            ObjectInputStream iss = new ObjectInputStream(Server.getInputStream());
                            ObjectOutputStream oss = new ObjectOutputStream (Server.getOutputStream());
                            System.out.println("!exit para sair.");
                            while(true){
                                System.out.println("Escrever para o Cliente " + ipamigo + " :");
                                Lere = Ler.umaString();
                                if (Lere.equals("!exit"))
                                    break;
                                oss.writeObject(Lere);
                                System.out.println("Mensagem do Cliente " + ipamigo + " :");
                                System.out.println((String) iss.readObject());
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                        }  
                    }
                    else{
                        try { 
                            Cliente = new Socket ("127.0.0.1", port);
                        }
                        catch (IOException e){
                            System.out.println("O Cliente para o qual ligou neste momento não se encontra disponível. Por favor, tente mais tarde.");
                        }
                        try {
                            ObjectOutputStream oss = new ObjectOutputStream (Server.getOutputStream());
                            ObjectInputStream iss = new ObjectInputStream(Server.getInputStream());
                            System.out.println("!exit para sair.");
                            while(true){
                                System.out.println("Mensagem do outro cara:");
                                System.out.println((String) iss.readObject());
                                System.out.println("Escrever para o outro cara:");
                                Lere = Ler.umaString();
                                if (Lere.equals("!exit"))
                                    break;
                                oss.writeObject(Lere);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }*/
                break;
            }
        }while (i!=5);
        //produtos.add(new Produto("x91topbiek", APIExtension.categoria.Bicicletas.name(), 10)); //must test if not repeated
        //System.out.println(produtos.get(0).nome);
        try{
            if(!produtos.isEmpty()){
                ObjectOutputStream oosprod = new ObjectOutputStream(new FileOutputStream("../../SavedFiles/produtos.txt"));
                oosprod.writeObject(produtos);
                oosprod.flush();
                System.out.println("produtos not null saving");
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        try{
            if(!categorias.isEmpty()){
                ObjectOutputStream ooscatg = new ObjectOutputStream(new FileOutputStream("../../SavedFiles/categorias.txt"));
                ooscatg.writeObject(categorias);
                ooscatg.flush();
                System.out.println("categorias not null saving");
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}

