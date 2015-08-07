/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

/**
 *
 * @author felipe
 */
public abstract class FragmentTransaction {
    public abstract FragmentTransaction attach(Fragment f);
    public abstract FragmentTransaction add(String containerId,Fragment f,String tag);
}
