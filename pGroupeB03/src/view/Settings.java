package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class Settings extends StackPane {
	private MenuSettings menuSettings;
	private UserSettings userSettings;
	private AdminSettings adminSettings;
	
	
	public Settings() {
		this.getChildren().addAll(getMenuSettings(),getUserSettings());
		this.showElement(getMenuSettings());
	}
	
	public MenuSettings getMenuSettings() {
		if(menuSettings == null) {
			menuSettings = new MenuSettings();
		}
		return menuSettings;
	}
	public UserSettings getUserSettings() {
		if(userSettings == null) {
			userSettings = new UserSettings();
		}
		return userSettings;
	}
	public AdminSettings getAdminSettings() {
		if(adminSettings == null) {
			adminSettings = new AdminSettings();
		}
		return adminSettings;
	}
	public void hideVisible() {
		for(Node n : this.getChildren()) {
			if(n.isVisible()) {
				n.setVisible(false);
			}
		}
	}
	private void showElement(Node element) {
		hideVisible();
		element.setVisible(true);
	}
	
	/*
	 * *****************************
	 * 		  INNER CLASSES
	 * *****************************
	 */
	class MenuSettings extends GridPane{
		private Button btnUser;
		private Button btnAdmin;
		
		public MenuSettings() {
			this.setPadding(new Insets(10));
			this.setAlignment(Pos.CENTER);
			this.setHgap(5);
			this.setVgap(5);
			
			GridPane.setHalignment(getBtnUser(), HPos.CENTER);
			this.add(getBtnUser(), 0, 0);
			GridPane.setHalignment(getBtnAdmin(), HPos.CENTER);
			this.add(getBtnAdmin(), 0, 1);
		}

		public Button getBtnUser() {
			if(btnUser == null) {
				btnUser = new Button("USER SETTINGS");
				
				btnUser.minWidth(IGraphicConst.WIDTH_LARGE_BUTTON);
				
				btnUser.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						hideVisible();
						getUserSettings().setVisible(true);
					}
				});
			}
			return btnUser;
		}

		public Button getBtnAdmin() {
			if(btnAdmin == null) {
				btnAdmin = new Button("ADMIN SETTINGS");
				
				btnAdmin.minWidth(IGraphicConst.WIDTH_LARGE_BUTTON);
				
				btnAdmin.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						hideVisible();
						Settings.this.showElement(getAdminSettings()); //TODO NE S'AFFICHE PAS
					}
				});
			}
			return btnAdmin;
		}
		
	}
	class UserSettings extends GridPane{
		private Label lblNbRound;
		private TextField txtNbRound;
		private Label lblTime;
		private TextField txtTime;
		private Button btnSave;
		
		public UserSettings() {
			this.setPadding(new Insets(10));
			this.setAlignment(Pos.CENTER);
			this.setHgap(5);
			this.setVgap(5);
			
			GridPane.setHalignment(getLblNbRound(),HPos.LEFT);
			this.add(getLblNbRound(), 0, 0);
			
			GridPane.setHalignment(getTxtNbRound(),HPos.CENTER);
			this.add(getTxtNbRound(), 1, 0);
			
			GridPane.setHalignment(getLblTime(),HPos.LEFT);
			this.add(getLblTime(), 0, 1);
			
			GridPane.setHalignment(getTxtTime(),HPos.CENTER);
			this.add(getTxtTime(), 1, 1);
			
			GridPane.setHalignment(getBtnSave(),HPos.RIGHT);
			this.add(getBtnSave(), 2, 2);
			
		}

		public Label getLblNbRound() {
			if( lblNbRound == null) {
				lblNbRound = new Label("NUMBER OF ROUNDS :");
			}
			return lblNbRound;
		}

		public TextField getTxtNbRound() {
			if(txtNbRound == null) {
				txtNbRound = new TextField("1");
			}
			return txtNbRound;
		}

		public Label getLblTime() {
			if(lblTime == null) {
				lblTime = new Label("ROUND TIME (second) :");
			}
			return lblTime;
		}

		public TextField getTxtTime() {
			if( txtTime == null) {
				txtTime = new TextField("time");
			}
			return txtTime;
		}

		public Button getBtnSave() {
			if( btnSave== null) {
				btnSave = new Button("Save");
			}
			return btnSave;
		}
		
		
		
	}
	
}
