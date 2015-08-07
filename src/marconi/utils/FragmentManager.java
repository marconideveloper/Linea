/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

import java.util.ArrayList;

/**
 *
 * @author felipe
 */
public abstract class FragmentManager {
    public abstract FragmentTransaction beginTransaction();
    public abstract Fragment findFragmentByTag(String tag);
}
final class FragmentManagerImpl extends FragmentManager{
    
    boolean executingActions;
    boolean havePendingDeferredStart;
            
    int currentState=Fragment.INITIALIZING;
    
    private ArrayList<Fragment> added;
    
    @Override
    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    @Override
    public Fragment findFragmentByTag(String tag) {
        if(added!=null){
            for(Fragment f:added){
                if(f.tag.equals(tag)){
                return f;
                }
            }
        }
        
        return null;
    }
    
    public void performPendingDeferredStart(Fragment f){
        if(f.deferStart){
            if(executingActions){
                havePendingDeferredStart=true;
                return;
            }
            f.deferStart=false;
            moveToState(f, currentState, 0, 0, false);
        }   
    }
    
    void moveToState(Fragment f,int newState, int transit, int transitionStyle,
            boolean keepActive){
        if((!f.added|| !f.detached) && newState>Fragment.CREATED ){
            newState=Fragment.CREATED;
        }
        if(f.removing && newState > f.state){
            newState=f.state;
        }
        if(f.deferStart && f.state < Fragment.STARTED && newState>Fragment.STOPPED){
            newState=Fragment.STOPPED;
        }
        if(f.animatingAway!=null){
            f.animatingAway=null;
            moveToState(f, f.stateAfterAnimating, 0, 0, true);
        }
        switch(f.state){
            case Fragment.INITIALIZING:
                break;
        }
        
    }
    
    public void addFragment(Fragment fragment){
        if(added==null){
            added=new ArrayList<>();
        }
    }
    
}
