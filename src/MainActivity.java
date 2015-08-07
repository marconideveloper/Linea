/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import marconi.utils.Activity;
import marconi.utils.Bundle;
import marconi.utils.widgets.EditText;
import marconi.utils.widgets.ToolBar;

/**
 *
 * @author Felipe
 */
public class MainActivity extends Activity{
    @FXML 
    EditText username,password;
    @FXML
    Button login;
    @FXML
    ToolBar toolbar;
    @Override
    public void onCreate(Bundle saveInstanceState) {
        setContentView("layout/login_activity.fxml");
        setActionBar(toolbar);
        login.setOnAction((ActionEvent event) -> {
            System.out.println(username.getText()+" "+password.getText());
            startActivity(SalesActivity.class);
        });
    }
    
}
