package lojadebicicletas.v2.pkg0;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class LojadebicicletasV20 {
    
    public static void main(String[] args) throws ClassNotFoundException{
        ArrayList<Produto> produtos = new ArrayList<>();
        ArrayList<String> categorias = new ArrayList<>();
        try{
            ObjectInputStream oisprod = new ObjectInputStream(new FileInputStream("produtos.txt"));
            produtos = (ArrayList<Produto>) oisprod.readObject();
        } catch(FileNotFoundException e){
            System.out.println("file not found");
            System.out.println(e.getMessage());
        } catch(IOException e){
            System.out.println("something went wrong with produtos please contact gui it's his fault"); //TODO
            System.out.println(e.getMessage());
            System.exit(0);
        }
        try{
            ObjectInputStream oiscatg = new ObjectInputStream(new FileInputStream("categorias.txt"));
            categorias = (ArrayList<String>) oiscatg.readObject();
        } catch(FileNotFoundException e){
            System.out.println("file not found");
            System.out.println(e.getMessage());
        } catch(IOException e){
            System.out.println("something went wrong with categorias please contact gui it's his fault"); //TODO
            System.out.println(e.getMessage());
            System.exit(0);
        }
        
        System.out.println("All loaded");
        
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("1- registar produto, 2-exit");
            int i = sc.nextInt();
            int counter = 1;
            if (i == 1){
                System.out.println("    " + "Insira o nome");
                String nome = sc.next();
                System.out.println("    " + "insira a categoria");
                if(categorias != null)
                    for(counter=1;counter<categorias.size()+1;counter++)
                        System.out.println("    " + counter + "- " +categorias.get(counter-1));
                System.out.println("    " + (counter) + "- Nova Categoria");
                int choice = sc.nextInt();
                if(choice == categorias.size()+1){
                    System.out.println("    " + "    " + "Nome da nova categoria");
                }
                categorias.add(sc.next());
                System.out.println(categorias.get(0));
                System.out.println("    " + "stock inicial?");
                int stock = sc.nextInt();
                produtos.add(new Produto(nome, categorias.get(choice-1),  stock));
                System.out.println("Produto adicionado com sucesso");
            }
            if (i == 2){
                break;
            }
            if (i == 3){
            }
            if (i == 4){
            }
        }
        
        //produtos.add(new Produto("x91topbiek", APIExtension.categoria.Bicicletas.name(), 10)); //must test if not repeated
        //System.out.println(produtos.get(0).nome);
        try{
            if(!produtos.isEmpty()){
                ObjectOutputStream oosprod = new ObjectOutputStream(new FileOutputStream("produtos.txt"));
                oosprod.writeObject(produtos);
                System.out.println("produtos not null saving");
            }
        } catch (IOException e){
            System.out.println("something went wrong saving contact gui again");
            System.out.println(e.getMessage());
        }
        try{
            if(!categorias.isEmpty()){
                ObjectOutputStream ooscatg = new ObjectOutputStream(new FileOutputStream("categorias.txt"));
                ooscatg.writeObject(categorias);
                System.out.println("categorias not null saving");
            }
        } catch (IOException e){
            System.out.println("something went wrong saving contact gui again");
            System.out.println(e.getMessage());
        }
    }
}
