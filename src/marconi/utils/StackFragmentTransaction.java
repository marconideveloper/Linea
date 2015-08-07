/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 *
 * @author felipe
 */
public class StackFragmentTransaction {

    public static final int FADE_ANIMATION = 0;
    public static final int NO_ANIMATION = -1;
    private ObservableList<Node> stack;
    private final double OFF_SET = 100;
    private final double DURATION = 250;
    public SequentialTransition animation;
    private int animationMode;
    private SimpleIntegerProperty stackSize;

    public StackFragmentTransaction(Pane parent) {
        this(parent, NO_ANIMATION);
    }

    public StackFragmentTransaction(Pane parent, int animationMode) {
        stack = parent.getChildren();
        if (animationMode != NO_ANIMATION) {
            animation = new SequentialTransition();
        }
        this.animationMode = animationMode;
        stackSize = new SimpleIntegerProperty();
        stack.addListener((ListChangeListener.Change<? extends Node> c) -> {
            stackSize.set(c.getList().size());
        });
    }

    public void addTopView(Node node) {

//        if (homeImage != null) {
//            homeImage.setOpacity(0);
//            homeButton.setDisable(true);
//        }
//        if (animationMode != NO_ANIMATION) {
//            animation.getChildren().clear();
//            Timeline timeline = new Timeline();
//            KeyFrame keyFrame = null;
//            node.setCache(true);
//            node.setCacheHint(CacheHint.SPEED);
//
//            switch (animationMode) {
//                case FADE_ANIMATION:
//                    if (stack.size() > 0) {
//                        keyFrame = getFadeAnimation(stack.get(stack.size() - 1), node);
//                        stack.add(node);
//                    } else {
//                        stack.add(node);
//                    }
//                    break;
//
//            }
//            timeline.setOnFinished((ActionEvent event) -> {
//                stack.remove(0, stack.indexOf(node));
//            });
//
//            if (keyFrame != null) {
//                timeline.getKeyFrames().add(keyFrame);
//                animation.getChildren().add(timeline);
//                animation.play();
//            }
//        } else {
        stack.clear();
        stack.add(node);
      
    }

    public void addView(Node node) {
        if (animationMode != NO_ANIMATION) {
            node.setMouseTransparent(true);
            animation.getChildren().clear();
            Timeline timeline = new Timeline();
            KeyFrame keyFrame = null;
            node.setCache(true);
            node.setCacheHint(CacheHint.SPEED);
            switch (animationMode) {
                case FADE_ANIMATION:
                    if (stack.size() > 0) {
                        keyFrame = getFadeAnimation(stack.get(stack.size() - 1), node);
                        stack.add(node);
                    }
                    break;
            }
            timeline.getKeyFrames().add(keyFrame);
            animation.getChildren().add(timeline);
            if (keyFrame != null) {
                animation.play();
            }
            timeline.setOnFinished((ActionEvent event) -> {
                node.setMouseTransparent(false);
                if (stack.get(stack.indexOf(node) - 1) instanceof LifeCycle) {
                    ((LifeCycle) stack.get(stack.indexOf(node) - 1)).onStop();
                }

            });
        } else {
            stack.add(node);
            node.setMouseTransparent(false);
            if (stack.get(stack.indexOf(node) - 1) instanceof LifeCycle) {
                ((LifeCycle) stack.get(stack.indexOf(node) - 1)).onStop();
            }
        }
    }

    public void back() {
        Node currentNode = stack.get(stack.size() - 1);
        Node beforeNode = stack.get(stack.size() - 2);
        if (animationMode != NO_ANIMATION) {
            animation.getChildren().clear();
            Timeline timeline = new Timeline();
            KeyFrame keyFrame = null;
            switch (animationMode) {
                case FADE_ANIMATION:
                    keyFrame = getFadeAnimation(currentNode, beforeNode);
                    break;
            }
            timeline.getKeyFrames().add(keyFrame);
            animation.getChildren().add(timeline);
            animation.play();
            timeline.setOnFinished((ActionEvent event) -> {
                stack.remove(currentNode);
                if (beforeNode instanceof LifeCycle) {
                    ((LifeCycle) beforeNode).onResume();
                }

            });
        } else {
            stack.remove(currentNode);
            if (beforeNode instanceof LifeCycle) {
                ((LifeCycle) beforeNode).onResume();
            }

        }
    }

    public SimpleIntegerProperty stackSizeProperty() {
        return stackSize;
    }

    private KeyFrame getFadeAnimation(Node topViewCurrent, Node node) {
        node.setTranslateX(OFF_SET);
        node.setOpacity(0);
        KeyFrame keyFrame = new KeyFrame(new Duration(DURATION),
                new KeyValue(topViewCurrent.translateXProperty(), -(OFF_SET)),
                new KeyValue(topViewCurrent.opacityProperty(), 0),
                new KeyValue(node.translateXProperty(), 0),
                new KeyValue(node.opacityProperty(), 1)
        );
        return keyFrame;
    }

    public interface LifeCycle {

        public void onCreate();

        public void onCreateView(FXMLLoader layoutInflater);

        public void onResume();

        public void onStop();
    }
}
