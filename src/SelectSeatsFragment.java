
import utils.SeatShape;
import utils.BusPlace;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import marconi.utils.Fragment;
import utils.FadeTransitionForm;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Felipe
 */
public class SelectSeatsFragment extends Fragment {

    private static final int NUM_ROWS = 4;
    @FXML
    private BusPlace busPlace;

    @FXML
    private TableView seatsTable;
    @FXML
    private ComboBox<String> typeSelector;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vbox;
    @FXML
    private GridPane dataClientForm;
    @FXML
    private Button saveData;

    static Stage stage;
    int cont = 0;

    @Override
    public void onCreateView(FXMLLoader layoutInflater) {
        try {
            layoutInflater.setLocation(Main.class.getResource("layout/select_seats_fragment.fxml"));
            layoutInflater.load();
        } catch (IOException ex) {
            Logger.getLogger(SelectTravelFragment.class.getName()).log(Level.SEVERE, null, ex);
        }

        vbox.prefWidthProperty().bind(scrollPane.widthProperty().subtract(24));

        typeSelector.getItems().addAll("Normal", "Niño", "Tercera Edad", "Estudiante");
        busPlace.setOnSeatChange((ActionEvent event) -> {

            if (event.getSource() instanceof SeatShape) {
                SeatShape seat = ((SeatShape) event.getSource());
                if (seat.isSelected()) {
                    seat.setType(typeSelector.getSelectionModel().getSelectedIndex());
                    seatsTable.getItems().add(seat);
                } else {
                    seatsTable.getItems().remove(seat);
                }
                typeSelector.getSelectionModel().selectFirst();
            }

        });
        typeSelector.getSelectionModel().selectFirst();

        ((TableColumn<SeatShape, String>) seatsTable.getColumns().get(0)).setCellValueFactory((CellDataFeatures<SeatShape, String> param) -> new ReadOnlyStringWrapper(busPlace.getSeatsList().indexOf(param.getValue()) + 1 + ""));
        ((TableColumn<SeatShape, String>) seatsTable.getColumns().get(1)).setCellValueFactory((CellDataFeatures<SeatShape, String> param) -> new ReadOnlyStringWrapper(typeSelector.getItems().get(param.getValue().getType())));
        initTableTransition();
    }

    @Override
    public void onResume() {
        super.onResume();
        createBusSeats(40);
        seatsTable.getItems().clear();
    }

    private void initTableTransition() {
        MenuItem openDataForm=new MenuItem("Agregar datos");
        ContextMenu menu=new ContextMenu(openDataForm);
        seatsTable.setContextMenu(menu);
        new FadeTransitionForm(dataClientForm, seatsTable, openDataForm,saveData);
    }

    private void createBusSeats(int numSeats) {
        cont++;
        int numColumns = ((numSeats % NUM_ROWS) != 0 ? (numSeats / NUM_ROWS) + 1 : (numSeats / NUM_ROWS));

        busPlace.initSeats(numSeats, numColumns);
        BusPlace otherSreenBus = null;
        if (stage == null) {
            stage = new Stage();
            otherSreenBus = new BusPlace();
            otherSreenBus.setScaleX(2);
            otherSreenBus.setScaleY(2);
            Scene scene = new Scene(otherSreenBus, 500, 500);
            otherSreenBus = new BusPlace();
            if (Screen.getScreens().size() == 2) {
                Screen screen = Screen.getScreens().get(1);
                stage.setX(screen.getBounds().getMinX());
                stage.setY(screen.getBounds().getMinY());
                stage.setFullScreen(true);
            }
            stage.setScene(scene);
            stage.show();
        }
        if (stage.getScene().getRoot() instanceof BusPlace) {
            otherSreenBus = ((BusPlace) stage.getScene().getRoot());
        }
        if (otherSreenBus != null) {
            otherSreenBus.initSeats(numSeats, numColumns);
            otherSreenBus.syncSeats(busPlace);
        }
    }

}
