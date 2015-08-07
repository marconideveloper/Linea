/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils.widgets;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import marconi.utils.Fragment;
import marconi.utils.StackFragmentTransaction;

/**
 *
 * @author Felipe
 */
public class SlidingTabLayout extends AnchorPane {

    private static final int INDEX_TAB_CONTAINER = 0;
    private static final double DEFAULT_HEIGHT_TAB_CONTAINER = 46;
    private static final String CLASS_ACTION_ITEM = "action-item";
    private final StackPane mainContainer;

    private final SlidingTab slidingTab;

    public SlidingTabLayout() {
        mainContainer = new StackPane();
        AnchorPane.setTopAnchor(mainContainer, DEFAULT_HEIGHT_TAB_CONTAINER);
        AnchorPane.setLeftAnchor(mainContainer, 0d);
        AnchorPane.setRightAnchor(mainContainer, 0d);
        AnchorPane.setBottomAnchor(mainContainer, 0d);
        getChildren().add(INDEX_TAB_CONTAINER, mainContainer);
        getChildren().add(INDEX_TAB_CONTAINER + 1, (slidingTab = new SlidingTab(new StackFragmentTransaction(mainContainer))));

    }

    public void addTab(String title, Class<?> fragment) {
        slidingTab.tabs.add(new SlidingTab.Tab(title, fragment));
    }

    public void addTab(String title, Class<?> fragment, HashMap arguments) {
        slidingTab.tabs.add(new SlidingTab.Tab(title, fragment, arguments));
    }

    public void changeTab(int index) {
        ((RippleButton) slidingTab.tabContainer.getChildren().get(index)).fire();
    }

    static private class SlidingTab extends VBox {

        private final Color BASELINE_COLOR_FOCUSED = Color.web("#FFFF8D");
        protected final ObservableList<SlidingTab.Tab> tabs;
        protected final HBox tabContainer;
        private final StyleableProperty<Color> tabline;
        private final static StyleablePropertyFactory<SlidingTab> STYLEABLE_PROPERTY_FACTORY = new StyleablePropertyFactory<>(VBox.getClassCssMetaData());

        public SlidingTab(final StackFragmentTransaction mainContainer) {

            tabContainer = new HBox();
            tabs = FXCollections.observableArrayList();
            getStyleClass().add("tab-layout");
            setPrefSize(USE_COMPUTED_SIZE, DEFAULT_HEIGHT_TAB_CONTAINER);
            AnchorPane.setTopAnchor(this, 0d);
            AnchorPane.setLeftAnchor(this, 0d);
            AnchorPane.setRightAnchor(this, 0d);

            final Line line = new Line(0, 0, 100, 0);
            line.setStrokeWidth(3);

            getChildren().add(INDEX_TAB_CONTAINER, tabContainer);
            getChildren().add(INDEX_TAB_CONTAINER + 1, line);

            tabs.addListener(new ListChangeListener<Tab>() {

                @Override
                public void onChanged(ListChangeListener.Change<? extends Tab> c) {
                    while (c.next()) {
                        for (Tab tab : c.getAddedSubList()) {
                            final RippleButton tabControl = new RippleButton();
                            tabControl.setText(tab.title);
                            tabControl.getStyleClass().add(CLASS_ACTION_ITEM);
                            tabControl.toggled(true);
                            tabControl.setPrefHeight(DEFAULT_HEIGHT_TAB_CONTAINER - 3);

                            tabControl.setOnAction(new EventHandler<ActionEvent>() {

                                @Override
                                public void handle(ActionEvent event) {

                                    double off_set = ((RippleButton) event.getSource()).getLayoutX() - line.getLayoutX();
                                    Timeline timeline = new Timeline(new KeyFrame(new Duration(150),
                                            new KeyValue(line.translateXProperty(), off_set)));
                                    double width = ((RippleButton) event.getSource()).getWidth();
                                    line.setEndX(width);
                                    timeline.play();
                                    if (tab.getInstance() == null) {
                                        tab.newInstance();
                                    }
                                    /*Support not Fragment objects, but inherent Node objects*/
                                    if (tab.getInstance() instanceof Fragment) {
                                        mainContainer.addTopView(((Fragment) (tab.getInstance())));
                                    } else {
                                        mainContainer.addTopView(((Node) tab.getInstance()));
                                    }

                                    if (tab.getInstance() instanceof StackFragmentTransaction.LifeCycle) {
                                        ((StackFragmentTransaction.LifeCycle) tab.getInstance()).onResume();
                                    }
                                }
                            });
                            tabContainer.getChildren().add(tabControl);
                            if (tabContainer.getChildren().indexOf(tabControl) == 0) {
                                tabControl.widthProperty().addListener(new ChangeListener<Number>() {

                                    @Override
                                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                        if (newValue.doubleValue() > 0) {
                                            tabControl.fire();
                                            tabControl.widthProperty().removeListener(this);
                                        }
                                    }
                                });
                            }
                        }

                    }

                }
            });

            tabline = STYLEABLE_PROPERTY_FACTORY.createStyleableColorProperty(this, "tabline", "-fx-tabline-background", (SlidingTab st) -> st.tabline, BASELINE_COLOR_FOCUSED);
            line.strokeProperty().bind((ObservableValue<Color>) tabline);
        }

        @Override
        public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
            return STYLEABLE_PROPERTY_FACTORY.getCssMetaData();
        }

        static public class Tab {

            private final String title;
            private final Class<?> fragment;
            private final HashMap arguments;
            private Object instance;

            public Tab(String title, Class<?> fragment) {
                this(title, fragment, null);
            }

            public Tab(String title, Class<?> Fragment, HashMap arguments) {
                this.title = title;
                this.fragment = Fragment;
                this.arguments = arguments;
            }

            public Class<?> getFragment() {
                return fragment;
            }

            public String getTitle() {
                return title;
            }

            public HashMap getArguments() {
                return arguments;
            }

            public void newInstance() {
                try {
                    this.instance = this.fragment.newInstance();
                    if (arguments != null && this.instance instanceof Fragment) {
                        ((Fragment) this.instance).setArguments(arguments);
                    }
                } catch (SecurityException | IllegalArgumentException | InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(SlidingTabLayout.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            public Object getInstance() {
                return instance;
            }
        }

    }

}
