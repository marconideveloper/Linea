/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 *
 * @author Felipe
 */
public class LayoutInflater {
    private final Class<?> appClass;
    private final FXMLLoader loader=new FXMLLoader();
    
    LayoutInflater(Class<?> appClass) {
        this.appClass=appClass;
      
    }
    protected void setContext(Activity activity){
       loader.setController(activity);
       loader.setRoot(activity);
    }
    
    public Node inflate(String path){
        
        loader.setLocation(appClass.getResource(path));
        try {
            return  loader.load();
        } catch (IOException ex) {
            Logger.getLogger(LayoutInflater.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    protected FXMLLoader getLoader(){
        return loader;
    }
   
}
