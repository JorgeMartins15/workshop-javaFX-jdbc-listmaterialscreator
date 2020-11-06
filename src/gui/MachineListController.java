package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Machine;

public class MachineListController implements Initializable {

	
	@FXML
	private TableView<Machine> tableViewMachine;
	
	@FXML
	private TableColumn<Machine, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Machine, String> tableColumnName;
	
	@FXML
	private TableColumn<Machine, String> tableColumnType;
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializedNodes();
	}

	private void initializedNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewMachine.prefHeightProperty().bind(stage.heightProperty());
	}

}
