package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Machine;
import model.services.MachineService;

public class MachineListController implements Initializable, DataChangeListener {

	private MachineService service;
	
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
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Machine obj = new Machine();
		createDialogForm(obj,"/gui/MachineForm.fxml", parentStage);
	}
	
	public void MachineService(MachineService service) {
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

	
	private void createDialogForm(Machine obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			MachineFormController controller = loader.getController();
			controller.setMachine(obj);
			controller.setMachineService(new MachineService());
		    controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Machine data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		}

		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
