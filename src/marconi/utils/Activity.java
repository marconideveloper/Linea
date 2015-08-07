/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

import javafx.scene.layout.AnchorPane;
import marconi.utils.widgets.ToolBar;

/**
 *
 * @author Felipe
 */
public abstract class Activity extends AnchorPane implements StackActivityTransition.LifeCycleActivty,Context{
   
    private MaterialApplication application;
    private ToolBar toolbar;
    private final FragmentManagerImpl fragmentManager=new FragmentManagerImpl();
    
    public void setContentView(String path){
        getLayoutInflater().setContext(this);
        getLayoutInflater().inflate(path);
        AnchorPane.setLeftAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setBottomAnchor(this, 0d);
    }
    public LayoutInflater getLayoutInflater(){
        return application.getLayoutInflater();
    }
    
    public void setActionBar(ToolBar toolbar){
        this.toolbar=toolbar;
    }

    public ToolBar getToolbar() {
        return toolbar;
    }
    
    protected void setApplication(MaterialApplication application){
        this.application=application;
    }

    @Override
    public void onStart() {
        
    }
    @Override
    public void onResume() {
        
    }
    @Override
    public void onPause() {
        
    }
    @Override
    public void onStop() {
        
    }
    
    @Override
    public void onDestroy() {
       System.gc();
    }
    
    public FragmentManager getFragmentManager(){ return fragmentManager; }

    @Override
    public final void startActivity(Class<? extends Activity> activity, double width,double height) {
        application.startActivity(activity,width,height);
    }

    @Override
    public final void startActivity(Class<? extends Activity> activity) {
        startActivity(activity,0,0);
    }
    
    
    
    
}
