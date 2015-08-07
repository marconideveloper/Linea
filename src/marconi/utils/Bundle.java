/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

import java.util.HashMap;

/**
 *
 * @author Felipe
 */
public class Bundle extends HashMap<String,Object> {
  
   public void putValue(String key, Object value){
       put(key, value);
   }
   public int getInt(String key){
     return getInt(key, 0);
   }
   public int getInt(String key,int defaultValue){
       return (get(key) instanceof Integer? Integer.parseInt((String) get(key)):0);
   }
}
