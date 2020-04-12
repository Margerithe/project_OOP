package view;

import exception.WrongLoginException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Admin;

public class AdminSettings extends StackPane {

	private Admin admin;
	
	private AdminLogin adminLogin;
	private AdminMenu adminMenu;
	private AdminDelete adminDelete;
	private AdminAdd adminAdd;
	
	public AdminSettings() {
		this.getChildren().addAll(getAdminLogin(),getAdminMenu(),getAdminDelete(),getAdminAdd());
		this.showElement(getAdminLogin());
	}
	
	private void showElement(Node element) {
		this.getChildren().removeAll(this.getChildren());
		this.getChildren().add(element);
		element.setVisible(true);
	}
	
	public AdminDelete getAdminDelete() {
		if(adminDelete == null) {
			adminDelete = new AdminDelete();
		}
		return adminDelete;
	}
	public AdminLogin getAdminLogin() {
		if(adminLogin == null) {
			adminLogin = new AdminLogin();
		}
		return adminLogin;
	}
	public AdminMenu getAdminMenu() {
		if(adminMenu == null) {
			adminMenu = new AdminMenu();
		}
		return adminMenu;
	}
	public AdminAdd getAdminAdd() {
		if(adminAdd == null) {
			adminAdd = new AdminAdd();
		}
		return adminAdd;
	}
	
	
	/*
	 * *****************************
	 * 		  INNER CLASSES
	 * *****************************
	 */
	
	class AdminLogin extends GridPane {
		private Label lblTitre;
		
		private Label lblLog;
		private TextField txtLog;
		
		private Label lblPass;
		private PasswordField pwfPass;
		
		private Button btnOk;
		
		public AdminLogin() {
			
			this.setBackground(new Background(new BackgroundImage(
					new Image(IGraphicConst.URL_PATH_IMG + "background/background_admin_login.png", false), 
					BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
					BackgroundPosition.CENTER,  
					new BackgroundSize(IGraphicConst.WIDTH_BACKGROUND, IGraphicConst.HEIGHT_BACKGROUND, false, false, false, false))));
			
			this.setPadding(new Insets(10));
			this.setAlignment(Pos.CENTER);
			this.setHgap(5);
			this.setVgap(5);
			//this.setGridLinesVisible(true);
			
			GridPane.setHalignment(getLblTitre(),HPos.CENTER);
			this.add(getLblTitre(), 0, 0, 2, 1);
			GridPane.setHalignment(getLblLog(), HPos.LEFT);
			this.add(getLblLog(), 0, 1);
			GridPane.setHalignment(getTxtLog(), HPos.CENTER);
			this.add(getTxtLog(), 1, 1);
			GridPane.setHalignment(getLblPass(), HPos.LEFT);
			this.add(getLblPass(), 0, 2);
			GridPane.setHalignment(getPwfPass(), HPos.CENTER);
			this.add(getPwfPass(), 1, 2);
			GridPane.setHalignment(getBtnOk(), HPos.RIGHT);
			this.add(getBtnOk(), 0, 3, 2, 1);
		}
		
		public Label getLblTitre() {
			if(lblTitre==null) {
				lblTitre = new Label("ADMIN LOGIN");
			}
			return lblTitre;
		}
		
		public Label getLblLog() {
			if(lblLog==null) {
				lblLog = new Label("USER NAME : ");
			}
			return lblLog;
		}
		public TextField getTxtLog() {
			if(txtLog==null) {
				txtLog = new TextField();
				txtLog.setOnKeyPressed(new EventHandler<KeyEvent>() {

					@Override
					public void handle(KeyEvent event) {
						if(event.getCode() == KeyCode.ENTER) {
							getPwfPass().requestFocus();
						}
					}
				});
			}
			return txtLog;
		}
		public Label getLblPass() {
			if(lblPass==null) {
				lblPass = new Label("PASSWORD : ");
			}
			return lblPass;
		}
		public PasswordField getPwfPass() {
			if(pwfPass==null) {
				pwfPass = new PasswordField();
				pwfPass.setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent event) {
						if(event.getCode() == KeyCode.ENTER) {
							getBtnOk().fire();
						}
					}
				});
			}
			return pwfPass;
		}
		public Button getBtnOk() {
			if(btnOk==null) {
				btnOk = new Button("LOGIN");
				btnOk.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							admin = new Admin(getTxtLog().getText(), getPwfPass().getText());
						} catch (WrongLoginException e) {
							MsgBox.dispalyException(e);
							getTxtLog().clear();
							getPwfPass().clear();
							getTxtLog().requestFocus();
							return;
						}
						showElement(new AdminMenu());
					}
				});
			}
			return btnOk;
		}
	}

	class AdminMenu extends BorderPane{
		private Button btnAdd;
		private Button btnDelete;
		private Button btnImport;
		
		public AdminMenu() {
			
			this.setBackground(new Background(new BackgroundImage(
					new Image(IGraphicConst.URL_PATH_IMG + "background/background_user_settings.png", false), 
					BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
					BackgroundPosition.CENTER,  
					new BackgroundSize(IGraphicConst.WIDTH_BACKGROUND, IGraphicConst.HEIGHT_BACKGROUND, false, false, false, false))));
			
			VBox vb = new VBox();
			vb.getChildren().addAll(getBtnAdd(),getBtnDelete(),getBtnImport());
			vb.setSpacing(30);
			vb.setAlignment(Pos.CENTER);
			vb.setTranslateY(40);
			this.setCenter(vb);
		}
		
		public Button getBtnAdd() {
			if(btnAdd == null) {
				btnAdd = new Button("ADD QUESTIONS");
				btnAdd.setMinWidth(IGraphicConst.WIDTH_LARGE_BUTTON);
				
				btnAdd.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						showElement(new AdminAdd());
					}
				});
			}
			return btnAdd;
		}
		public Button getBtnDelete() {
			if(btnDelete == null) {
				btnDelete = new Button("DELETE QUESTIONS");
				btnDelete.setMinWidth(IGraphicConst.WIDTH_LARGE_BUTTON);
				
				btnDelete.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						showElement(new AdminDelete());
					}
				});
			}
			return btnDelete;
		}
		public Button getBtnImport() {
			if(btnImport == null) {
				btnImport = new Button("IMPORT QUESTIONS (json format)");
				btnImport.setMinWidth(IGraphicConst.WIDTH_LARGE_BUTTON);
			}
			return btnImport;
		}
		
		
	}
	class AdminDelete extends GridPane{
		private Label lblTheme;
		private Label lblRiddle;
		
		private TextField txtTheme;
		private TextField txtRiddle;
		
		private Button btnDelete;
		
		public AdminDelete() {
			
			this.setBackground(new Background(new BackgroundImage(
					new Image(IGraphicConst.URL_PATH_IMG + "background/background_user_settings.png", false), 
					BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
					BackgroundPosition.CENTER,  
					new BackgroundSize(IGraphicConst.WIDTH_BACKGROUND, IGraphicConst.HEIGHT_BACKGROUND, false, false, false, false))));
			
			this.setPadding(new Insets(10));
			this.setAlignment(Pos.CENTER);
			this.setHgap(5);
			this.setVgap(5);
			//this.setGridLinesVisible(true);
			
			GridPane.setHalignment(getLblTheme(), HPos.LEFT);
			this.add(getLblTheme(), 0, 0);
			GridPane.setHalignment(getTxtTheme(), HPos.CENTER);
			this.add(getTxtTheme(), 1, 0);
			GridPane.setHalignment(getLblRiddle(), HPos.LEFT);
			this.add(getLblRiddle(), 0, 1);
			GridPane.setHalignment(getTxtRiddle(), HPos.CENTER);
			this.add(getTxtRiddle(), 1, 1);
			GridPane.setHalignment(getBtnDelete(), HPos.RIGHT);
			this.add(getBtnDelete(), 0, 2, 2, 1);
		}

		public Label getLblTheme() {
			if(lblTheme == null) {
				lblTheme = new Label("SELECT A THEME : ");
			}
			return lblTheme;
		}

		public Label getLblRiddle() {
			if(lblRiddle == null) {
				lblRiddle = new Label("SELECT A RIDDLE : ");
			}
			return lblRiddle;
		}

		public TextField getTxtTheme() {
			if(txtTheme == null) {
				txtTheme = new TextField();
			}
			return txtTheme;
		}

		public TextField getTxtRiddle() {
			if(txtRiddle == null) {
				txtRiddle = new TextField();
			}
			return txtRiddle;
		}
		public Button getBtnDelete() {
			if(btnDelete == null) {
				btnDelete = new Button("DELETE");
			}
			return btnDelete;
		}
		
		
	}
	class AdminAdd extends GridPane{
		private Label lblTheme;
		private TextField txtTheme;
		private Label lblClue1;
		private TextField txtClue1;
		private Label lblClue2;
		private TextField txtClue2;
		private Label lblClue3;
		private TextField txtClue3;
		
		private Label lblInfo;
		private Button btnAdd;
		
		public AdminAdd() {
			
			this.setBackground(new Background(new BackgroundImage(
					new Image(IGraphicConst.URL_PATH_IMG + "background/background_user_settings.png", false), 
					BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
					BackgroundPosition.CENTER,  
					new BackgroundSize(IGraphicConst.WIDTH_BACKGROUND, IGraphicConst.HEIGHT_BACKGROUND, false, false, false, false))));
			
			this.setPadding(new Insets(10));
			this.setAlignment(Pos.CENTER);
			this.setHgap(5);
			this.setVgap(5);
			//this.setGridLinesVisible(true);
			
			GridPane.setHalignment(getLblTheme(), HPos.LEFT);
			this.add(getLblTheme(), 0, 0);
			GridPane.setHalignment(getTxtTheme(), HPos.CENTER);
			this.add(getTxtTheme(), 1, 0);
			
			GridPane.setHalignment(getLblClue1(), HPos.LEFT);
			this.add(getLblClue1(), 0, 1);
			GridPane.setHalignment(getTxtClue1(), HPos.CENTER);
			this.add(getTxtClue1(), 1, 1);
			
			GridPane.setHalignment(getLblClue2(), HPos.LEFT);
			this.add(getLblClue2(), 0, 2);
			GridPane.setHalignment(getTxtClue2(), HPos.CENTER);
			this.add(getTxtClue2(), 1, 2);
			
			GridPane.setHalignment(getLblClue3(), HPos.LEFT);
			this.add(getLblClue3(), 0, 3);
			GridPane.setHalignment(getTxtClue3(), HPos.CENTER);
			this.add(getTxtClue3(), 1, 3);
			
			GridPane.setHalignment(getLblInfo(), HPos.CENTER);
			this.add(getLblInfo(), 0, 4, 2, 1);
			
			GridPane.setHalignment(getBtnAdd(),HPos.RIGHT);
			this.add(getBtnAdd(), 1, 5, 1, 1);
		}

		public Label getLblTheme() {
			if(lblTheme == null) {
				lblTheme = new Label("SELECT A THEME : ");
			}
			return lblTheme;
		}

		public TextField getTxtTheme() {
			if(txtTheme == null) {
				txtTheme = new TextField();
			}
			return txtTheme;
		}
		public Label getLblClue1() {
			if(lblClue1 == null) {
				lblClue1 = new Label("CLUE 1 : ");
			}
			return lblClue1;
		}

		public TextField getTxtClue1() {
			if(txtClue1 == null) {
				txtClue1 = new TextField("");
			}
			return txtClue1;
		}

		public Label getLblClue2() {
			if(lblClue2 == null) {
				lblClue2 = new Label("CLUE 2 : ");
			}
			return lblClue2;
		}

		public TextField getTxtClue2() {
			if(txtClue2 == null) {
				txtClue2 = new TextField("");
			}
			return txtClue2;
		}

		public Label getLblClue3() {
			if(lblClue3 == null) {
				lblClue3 = new Label("CLUE 3 : ");
			}
			return lblClue3;
		}

		public TextField getTxtClue3() {
			if(txtClue3 == null) {
				txtClue3 = new TextField("");
			}
			return txtClue3;
		}

		public Label getLblInfo() {
			if(lblInfo == null) {
				lblInfo = new Label("It is necessary to have a minimum of 10 questions per theme for it to be playable");
			}
			return lblInfo;
		}

		public Button getBtnAdd() {
			if(btnAdd == null) {
				btnAdd = new Button("ADD");
			}
			return btnAdd;
		}
		
	}
}
