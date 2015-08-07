/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

import javafx.scene.Node;

/**
 *
 * @author Felipe
 */
public interface ActionNavigation{
    public void slideNav();
    public void changeMainContent(Node node);
    
    static public interface ActionNavigationContent{
        public void setActionNavigation( ActionNavigation actionNavigation);
    }
}
