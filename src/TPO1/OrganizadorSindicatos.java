/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TPO1;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Soy un organizador de sindicatos
 * aqui crean sus piquetes, sin pisarse
 * Para que dos sindicatos no esten en el mismo lugar
 * @author jerexio
 */
public class OrganizadorSindicatos {
    
    private Piquteros piqueteros;
    private Lista lugaresParaPiquete;
    private Mapa mapa;
    
    public OrganizadorSindicatos(Piquteros piqueteros, Mapa mapa){
        this.piqueteros = piqueteros;
        this.mapa = mapa;
        
        lugaresParaPiquete = new Lista();
        lugaresParaPiquete.insertar(new Ubicacion("Ruta 1",1,7), 1);
        lugaresParaPiquete.insertar(new Ubicacion("Ruta 3",3,4), 1);
        lugaresParaPiquete.insertar(new Ubicacion("Ruta 3",3,6), 1);
        lugaresParaPiquete.insertar(new Ubicacion("Ruta 3",3,8), 1);
        lugaresParaPiquete.insertar(new Ubicacion("Ruta 3",3,10), 1);
        lugaresParaPiquete.insertar(new Ubicacion("Ruta 5",5,5), 1);
    }
    
    public boolean mandarPiquete(Piquete piquete){
        return piqueteros.realizarPiquete(crearTareaPiquete(piquete));
    }
    
    public synchronized void finalizarPiquete(Ubicacion ubicacion){
        lugaresParaPiquete.insertar(ubicacion, 1);
    }
    
    private synchronized Callable crearTareaPiquete(Piquete nuevoPiquete){
        Callable piquete = null;
        if(nuevoPiquete != null){
            piquete = new Callable() {
                @Override
                public Object call() {
                    boolean exito = false;
                    Ubicacion lugar = nuevoPiquete.getUbicacion();
                    int posX = lugar.getPosX(),
                        posY = lugar.getPosY();
                    
                    if(mapa.establecerPiquete(posX, posY)){
                        try {
                            Thread.sleep(nuevoPiquete.getDuracionMillis());
                        } catch (InterruptedException ex) {
                            System.out.println("Error Organizador de sindicatos - Linea 56");
                        }
                        mapa.destablecerPiquete(posX, posY);
                        exito = new Random().nextBoolean();
                    }
                    
                    return exito;
                }
            };
        }
        
        return piquete;
    }
    
    public synchronized Piquete crearPiquete(){
        Random random = new Random();
        Piquete nuevoPiquete = null;
        
        if(!lugaresParaPiquete.esVacia()){
            int num = (random.nextInt(lugaresParaPiquete.longitud()))+1;
            
            Ubicacion ubic = (Ubicacion)lugaresParaPiquete.recuperar(num);
            
            lugaresParaPiquete.eliminar(num);

            long duracion = (random.nextInt(10)+1) * 1000;
            nuevoPiquete = new Piquete(ubic, duracion);
        }
        
        return nuevoPiquete;
    }
}
