/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Felipe
 */
public class MaterialApplication extends Application implements Context{

    private StackActivityTransition backStack;
    private static LayoutInflater layoutInflater;
    private Stage stage;
    protected double width,height;
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage=primaryStage;
        backStack = new StackActivityTransition(this);
        Scene scene = new Scene(backStack);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("widgets/styles/controls.css").toExternalForm());
        String styleUser=Class.forName("MainActivity").getResource("style/styles.css").toExternalForm();
        if(styleUser!=null){
        scene.getStylesheets().add(styleUser);
        }
        setMainActivity((Class<? extends Activity>) Class.forName("MainActivity"));
        primaryStage.show();
    }

    public void setMainActivity(Class<? extends Activity> activity) {
        backStack.addTopActivity(activity);
    }

    public LayoutInflater getLayoutInflater() {
        if (layoutInflater == null) {
            try {
                layoutInflater = new LayoutInflater(Class.forName("Main"));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MaterialApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return layoutInflater;
    }
    protected Stage getStage(){
        return stage;
    };
    
    @Override
    public void startActivity(Class<? extends Activity> activity, double width,double height){
        backStack.addActivity(activity);
        this.width=width;
        this.height=height;
    } 

    @Override
    public void startActivity(Class<? extends Activity> activity) {
        startActivity(activity,0,0);
    }

}
