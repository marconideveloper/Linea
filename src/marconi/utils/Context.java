/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

/**
 *
 * @author Felipe
 */
public interface Context {
    public void startActivity(Class<? extends Activity> activity, double width,double height);
    public void startActivity(Class<? extends Activity> activity);
}
