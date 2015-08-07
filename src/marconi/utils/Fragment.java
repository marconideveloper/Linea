/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

import java.util.HashMap;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author felipe
 */
public abstract class Fragment extends AnchorPane implements StackFragmentTransaction.LifeCycle {

    static final int INITIALIZING = 0;
    static final int CREATED = 1;
    static final int ACTIVITY_CREATED = 2;
    static final int STOPPED = 3;
    static final int STARTED = 4;
    static final int RESUMED = 5;

    private HashMap arguments;
    String tag;
    String fragmentId;
    String containerid;

    int state = INITIALIZING;
    int stateAfterAnimating;
    
    boolean userVisibleHint;
    //Es verdadero si tiene que esperar su incio hasta que terminen otros fragmentos
    boolean deferStart;
    //Es verdadero cuando esta en la lista de fragmentos agregados
    boolean added;
    boolean detached;
    boolean removing;
    
    Timeline animatingAway;

    FragmentManagerImpl fragmentManager;

    public Fragment() {

        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(this);
        loader.setController(this);
        onCreate();
        this.parentProperty().addListener(new ChangeListener<Parent>() {
            @Override
            public void changed(ObservableValue<? extends Parent> observable, Parent oldValue, Parent newValue) {
                if (newValue != null) {
                    onCreateView(loader);
                    onResume();
                    Fragment.this.parentProperty().removeListener(this);
                }
            }
        });

    }

    /*Methods same android*/
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!userVisibleHint && isVisibleToUser && state < STARTED) {
                fragmentManager.performPendingDeferredStart(this);
        }
        userVisibleHint = isVisibleToUser;
        deferStart = !isVisibleToUser;
    }
    
    void initState(){
        added=false;
        detached=false;
    }

    public final Node findViewByID(String id) {
        return this.lookup("#" + id);
    }

    public void setArguments(HashMap arguments) {
        this.arguments = arguments;
    }

    public HashMap getArguments() {
        return arguments;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onCreate() {

    }

}
