/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils.widgets;

import com.sun.javafx.scene.control.skin.ButtonSkin;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import marconi.utils.ActionNavigation;
import marconi.utils.StackFragmentTransaction;

/**
 *
 * @author Felipe
 */
public class NavigationDrawer extends AnchorPane implements ActionNavigation{

    @FXML
    private AnchorPane navContent;
    @FXML
    private StackPane mainContent;
    @FXML
    private ToolBar toolbar;
    @FXML
    private Region navLayer;
    @FXML
    private RippleButton homeButton;
    private final StackFragmentTransaction fragmentTransaction;

    private static final String CLASS_ACTION_ITEM = "action-item";

    public NavigationDrawer() {
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("layout/navigation_drawer.fxml"));
            fXMLLoader.setController(this);
            fXMLLoader.setRoot(this);
            fXMLLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(NavigationDrawer.class.getName()).log(Level.SEVERE, null, ex);
        }
        fragmentTransaction = new StackFragmentTransaction(mainContent, StackFragmentTransaction.FADE_ANIMATION);
        navLayer.setOnMouseClicked((MouseEvent event) -> {
            slideNav();
        });
        initHomeButton();
    }

    private void initHomeButton() {
        homeButton.getStyleClass().add(CLASS_ACTION_ITEM);
        homeButton.toggled(true);
        homeButton.setOnAction((ActionEvent evt) -> {
            if (fragmentTransaction.stackSizeProperty().get() > 1) {
                fragmentTransaction.back();
            } else {
                slideNav();
            }
            evt.consume();
        });

        SVGPath home = new SVGPath(), leftArrow = new SVGPath();
        home.setContent("M3,6H21V8H3V6M3,11H21V13H3V11M3,16H21V18H3V16Z");
        home.setFill(Color.web("#FFF"));
        home.setScaleX(1.2);
        home.setScaleY(1.2);
        leftArrow.setContent("M20,11V13H8L13.5,18.5L12.08,19.92L4.16,12L12.08,4.08L13.5,5.5L8,11H20Z");
        leftArrow.setFill(Color.web("#FFF"));
        leftArrow.setScaleX(1.2);
        leftArrow.setScaleY(1.2);
        leftArrow.setOpacity(0);
        leftArrow.setRotate(-180);

        homeButton.createDefaultSkin();
        ButtonSkin buttonSkin = homeButton.getButtonSkin();
        buttonSkin.getChildren().addAll(leftArrow, home);
        DoubleBinding layoutX = new DoubleBinding() {
            {
                bind(homeButton.widthProperty(), home.boundsInLocalProperty());
            }

            @Override
            protected double computeValue() {
                return (homeButton.getWidth() / 2) - ((home.getBoundsInLocal().getWidth() * 1.2) / 2);
            }
        };
        DoubleBinding layoutY = new DoubleBinding() {
            {
                bind(homeButton.heightProperty(), home.boundsInLocalProperty());
            }

            @Override
            protected double computeValue() {
                return (homeButton.getHeight() / 2) - ((home.getBoundsInLocal().getHeight()));
            }
        };
        home.layoutXProperty().bind(layoutX);
        home.layoutYProperty().bind(layoutY);
        leftArrow.layoutXProperty().bind(layoutX);
        leftArrow.layoutYProperty().bind(layoutY);

        fragmentTransaction.stackSizeProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            Timeline homeButtonMorphing = new Timeline();
            KeyFrame frame1, frame2;
            if (newValue.intValue() > 1) {
                frame1 = new KeyFrame(new Duration(200), new KeyValue(leftArrow.rotateProperty(), 0),
                        new KeyValue(leftArrow.opacityProperty(), 1),
                        new KeyValue(home.rotateProperty(), 180)
                );
                frame2 = new KeyFrame(new Duration(80),
                        new KeyValue(home.opacityProperty(), 0)
                );
            } else {
                frame1 = new KeyFrame(new Duration(200), new KeyValue(leftArrow.rotateProperty(), -180),
                        new KeyValue(leftArrow.opacityProperty(), 0),
                        new KeyValue(home.rotateProperty(), 0)
                );
                frame2 = new KeyFrame(new Duration(80),
                        new KeyValue(home.opacityProperty(), 1)
                );
            }
            homeButtonMorphing.getKeyFrames().addAll(frame1, frame2);
            homeButtonMorphing.play();
        });
    }
    
    @Override
    public final void slideNav() {
        Timeline slideAnimation = new Timeline();
        KeyFrame keyFrame;
        navContent.setCacheHint(CacheHint.SPEED);
        navLayer.setCacheHint(CacheHint.SPEED);
        if (navContent.getTranslateX() != 0) {
            navLayer.setVisible(true);
            keyFrame = new KeyFrame(new Duration(200), new KeyValue(navContent.translateXProperty(), 0),
                    new KeyValue(navLayer.opacityProperty(), 1));
        } else {
            keyFrame = new KeyFrame(new Duration(200), new KeyValue(navContent.translateXProperty(), -226),
                    new KeyValue(navLayer.opacityProperty(), 0),
                    new KeyValue(navLayer.visibleProperty(), false));
        }
        slideAnimation.getKeyFrames().add(keyFrame);
        slideAnimation.play();
    }
    public void setNavigationContent(Node node){
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        navContent.getChildren().clear();
        navContent.getChildren().add(node);
        if(node instanceof ActionNavigationContent){
            ((ActionNavigationContent)node).setActionNavigation(this);
        }
    }
    public final StackFragmentTransaction getFragmeentTransaction(){
        return fragmentTransaction;
    }

    @Override
    public void changeMainContent(Node node) {
        if(node instanceof SlidingTabLayout ){
           toolbar.getStyleClass().add("with-tab");
        }else{
           toolbar.getStyleClass().remove("with-tab"); 
        }
        fragmentTransaction.addTopView(node);
    }
}
