package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Machine;
import model.services.MachineServices;

public class MachineListController implements Initializable {

	private MachineServices service;
	
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
	
	private ObservableList<Machine> obsList;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	public void setMachineService(MachineServices service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializedNodes();
	}

	private void initializedNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("machineId"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewMachine.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Machine> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewMachine.setItems(obsList);
	}

}
