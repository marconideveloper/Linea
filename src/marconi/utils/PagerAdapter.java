/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

import javafx.scene.layout.Pane;

/**
 *
 * @author felipe
 */
public abstract class PagerAdapter {

    public PagerAdapter() {
    }
    
    public abstract int getCount();
    public abstract Object instantiateItem(Pane container, int position);
    
}
