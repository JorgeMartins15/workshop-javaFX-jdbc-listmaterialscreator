package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Machine;
import model.services.MachineService;

public class MachineFormController implements Initializable{
	
	private Machine entity;
	
	private MachineService service;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtType;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorType;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setMachine(Machine entity) {
		this.entity = entity;
	}
	
	public void setMachineService(MachineService service) {
		this.service = service;
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			Utils.currentStage(event).close();
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	
	}
	
	private Machine getFormData() {
		Machine obj = new Machine();
		obj.setMachineId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		obj.setType(txtType.getText());
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldMaxLength(txtType, 20);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getMachineId()));
		txtName.setText(entity.getName());
		txtType.setText(entity.getType());
	}

}
