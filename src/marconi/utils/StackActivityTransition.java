/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import marconi.utils.widgets.ToolBar;

/**
 *
 * @author Felipe
 */
public class StackActivityTransition extends AnchorPane {

    private final ObservableList<Activity> stack = FXCollections.observableArrayList();
    private final ObservableList<Node> content;
    private final MaterialApplication application;
    private final SimpleObjectProperty<ToolBar> actionBar = new SimpleObjectProperty<>();
    private final SimpleIntegerProperty stacksize = new SimpleIntegerProperty();

    public StackActivityTransition(MaterialApplication application) {
        this.application = application;
        this.getStyleClass().add("window");

        stack.addListener((ListChangeListener.Change<? extends Activity> c) -> {
            stacksize.setValue(c.getList().size());
        });

        AnchorPane stackPane = new AnchorPane();
        AnchorPane.setLeftAnchor(stackPane, 0d);
        AnchorPane.setRightAnchor(stackPane, 0d);
        AnchorPane.setTopAnchor(stackPane, 0d);
        AnchorPane.setBottomAnchor(stackPane, 0d);

        actionBar.addListener((ObservableValue<? extends ToolBar> observable, ToolBar oldValue, ToolBar newValue) -> {
            ParallelTransition transition = new ParallelTransition();
            if (newValue != null) {
                FadeTransition fadeNew = new FadeTransition(new Duration(350), newValue);
                fadeNew.setFromValue(0);
                fadeNew.setToValue(1);
                transition.getChildren().add(fadeNew);
                
                getChildren().add(0, newValue);
                newValue.toFront();
                if(newValue.getStackActivityTrasition()==null){
                newValue.setStackActivityTransition(this);
                }
                AnchorPane.setTopAnchor(stackPane, 56d);
            } 
            if (oldValue != null) {
                    FadeTransition fadeOld = new FadeTransition(new Duration(350), oldValue);
                    fadeOld.setFromValue(1);
                    fadeOld.setToValue(0);
                    fadeOld.setOnFinished((ActionEvent event) -> {
                        getChildren().remove(oldValue);
                    });
                    transition.getChildren().add(fadeOld);
            }
            transition.play();
        });

        content = stackPane.getChildren();
        getChildren().add(stackPane);
    }

    public void addTopActivity(Class<? extends Activity> topActivity) {
        Iterator<Activity> iterator = stack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            activity.onDestroy();
            content.clear();
            iterator.remove();
        }
        Activity activity = instanceAcivity(topActivity);
        activity.setApplication(application);
        stack.add(activity);
        activity.onCreate(null);
        if (activity.getToolbar() != null) {
            actionBar.setValue(activity.getToolbar());
        }
        content.add(activity);
        activity.onStart();
        activity.onResume();

    }

    public void addActivity(Class<? extends Activity> newActivity) {
        Activity beforeActivity = stack.get(stack.size() - 1);
        beforeActivity.onPause();
        Activity activity = instanceAcivity(newActivity);
        activity.onCreate(null);
        if (activity.getToolbar() != null) {
            actionBar.setValue(activity.getToolbar());
        }
        stack.add(activity);
        activity.onStart();
        activity.onResume();
        Animation transition = createAnimateTransition(beforeActivity, activity);
        transition.play();

    }

    public void back() {
        Activity activity = stack.get(stacksize.getValue() - 1);
        Activity beforeActivity = stack.get(stacksize.getValue() - 2);
        activity.onPause();
        beforeActivity.onResume();
//        content.add(beforeActivity);
        Animation transition=createAnimateTransition(activity, beforeActivity);
        transition.play();
        if (beforeActivity.getToolbar() != null) {
            actionBar.setValue(beforeActivity.getToolbar());
        } else {
            actionBar.setValue(null);
        }
        activity.onStop();
//        content.remove(activity);
        activity.onDestroy();
        stack.remove(activity);
    }

    private Animation createAnimateTransition(Activity beforeActivity, Activity activity) {
        content.add(activity);
        activity.setOpacity(0);
        activity.setTranslateX(25);
        FadeTransition fadeBefore = new FadeTransition(new Duration(175), beforeActivity);
        fadeBefore.setToValue(0);
        fadeBefore.setOnFinished((ActionEvent event) -> {
            if (application.width != 0 && application.height != 0) {
                application.getStage().close();
                application.getStage().setWidth(application.width);
                application.getStage().setHeight(application.height);
                application.getStage().centerOnScreen();
                application.getStage().show();
            }

        });
        Timeline fadeNew = new Timeline(new KeyFrame(new Duration(150), 
                new KeyValue(activity.opacityProperty(), 1),
                new KeyValue(activity.translateXProperty(),0)
        ));
        
        fadeNew.setOnFinished((ActionEvent event) -> {
            content.remove(beforeActivity);
            beforeActivity.onStop();
            beforeActivity.onDestroy();
        });
        
        SequentialTransition animation = new SequentialTransition(fadeBefore, fadeNew);
        return animation;
    }

    private Activity instanceAcivity(Class<? extends Activity> newActivity) {
        try {
            Activity activity = newActivity.newInstance();
            activity.setApplication(application);
            return activity;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(StackActivityTransition.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public SimpleIntegerProperty stackSizeProperty() {
        return stacksize;
    }

    static protected interface LifeCycleActivty {

        public void onCreate(Bundle saveInstanceState);

        public void onStart();

        public void onResume();

        public void onPause();

        public void onStop();

        public void onDestroy();
    }
}
