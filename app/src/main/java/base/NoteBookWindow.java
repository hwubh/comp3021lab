package base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

import org.checkerframework.checker.index.qual.NonNegative;

import base.Folder;
import base.Note;
import base.NoteBook;
import base.TextNote;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 * NoteBook GUI with JAVAFX
 * 
 * COMP 3021
 * 
 * 
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {
	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 * 
	 * Combobox for selecting the folder
	 * 
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";

	Stage stage;

	/**
	 * current note selected by the user
	 */

	String currentNote = "";

	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		loadNoteBook();
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(addGridPane());

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();

		this.stage = stage;
	}

	/**
	 * This create the top section
	 * 
	 * @return
	 */
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		Button buttonLoad = new Button("Load");
		buttonLoad.setPrefSize(100, 20);
		buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose A File That Contians A Notebook Object.");

				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
						"Serialized Object File (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showOpenDialog(stage);

				if (file != null) {
					loadNoteBook(file);
					currentFolder = "";
					currentNote = "";
					currentSearch = "";
					foldersComboBox.getItems().clear();
					for (Folder folder : noteBook.getFolders()) {
						foldersComboBox.getItems().add(folder.getName());
					}
					updateListView();
				}

			}
		});
		// buttonLoad.setDisable(false);
		Button buttonSave = new Button("Save");
		buttonSave.setPrefSize(100, 20);
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose A File That Contians A Notebook Object.");

				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
						"Serialized Object File (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showOpenDialog(stage);

				if (file != null) {
					saveNoteBook(file, noteBook);
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Successfully saved");
					alert.setContentText("You file has been saved to file " + file.getName());
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});

				}

			}
		});

		hbox.getChildren().addAll(buttonLoad, buttonSave);

		return hbox;
	}

	/**
	 * this create the section on the left
	 * 
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes

		// TODO: This line is a fake folder list. We should display the folders in
		// noteBook variable! Replace this with your implementation
		for (Folder file : noteBook.getFolders()) {
			foldersComboBox.getItems().add(file.getName());
		}

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 != null) {
					currentFolder = t1.toString();
				}
				// this contains the name of the folder selected
				// TODO update listview
				updateListView();

			}

		});

		foldersComboBox.setValue("-----");

		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null) {
					currentNote = "";
					textAreaNote.setText("");
					return;
				}
				String title = t1.toString();
				currentNote = title;

				// This is the selected title
				// TODO load the content of the selected note in
				Note note = noteBook.searchNotes(title).get(0);
				if (note instanceof TextNote) {
					TextNote tNote = (TextNote) note;
					String content = tNote.getContent();
					textAreaNote.setText(content);
				}

			}
		});

		Button addNewFolder = new Button("Add a Folder");
		addNewFolder.setPrefSize(100, 20);
		addNewFolder.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				TextInputDialog dialog = new TextInputDialog("Add a Folder");
				dialog.setTitle("Input");
				dialog.setHeaderText("Add a new folder for your notebook:");
				dialog.setContentText("Please enter the name you want to create:");

				// Traditional way to get the response value.
				java.util.Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					for (Folder f1 : noteBook.getFolders()) {
						if (result.get().isEmpty()) {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Warning");
							alert.setContentText("Please input a valid folder name");
							alert.showAndWait().ifPresent(rs -> {
								if (rs == ButtonType.OK) {
									System.out.println("Pressed OK.");
								}
							});
						} else if (f1.getName().equals(result.get())) {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Warning");
							alert.setContentText("Your already have a folder named " + result.get());
							alert.showAndWait().ifPresent(rs -> {
								if (rs == ButtonType.OK) {
									System.out.println("Pressed OK.");
								}
							});
						}
					}
					addFolder(result.get());
					currentFolder = "";
					currentNote = "";
					currentSearch = "";
					foldersComboBox.getItems().clear();
					for (Folder folder : noteBook.getFolders()) {
						foldersComboBox.getItems().add(folder.getName());
					}
					updateListView();
				}
			}
		});

		Button addNewNote = new Button("Add a Note");
		addNewNote.setPrefSize(100, 20);
		addNewNote.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Traditional way to get the response value.
				if (noteBook == null) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setContentText("Please choose a folder first");
				} else {
					TextInputDialog dialog = new TextInputDialog("Add a Note");
					dialog.setTitle("Input");
					dialog.setHeaderText("Add a new Note for your folder:");
					dialog.setContentText("Please enter the name of your note: ");

					java.util.Optional<String> result = dialog.showAndWait();
					if (result.isPresent()) {
						for (Folder file : noteBook.getFolders()) {
							if (file.getName().compareTo(currentFolder) == 0) {
								for (Note note : file.getNotes()) {
									if (result.get().isEmpty()) {
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("Warning");
										alert.setContentText("Please input a valid note name");
										alert.showAndWait().ifPresent(rs -> {
											if (rs == ButtonType.OK) {
												System.out.println("Pressed OK.");
											}
										});
									} else if (note.getTitle().equals(result.get())) {
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("Warning");
										alert.setContentText("Your already have a note named " + result.get());
										alert.showAndWait().ifPresent(rs -> {
											if (rs == ButtonType.OK) {
												System.out.println("Pressed OK.");
											}
										});
									}
								}
								file.addNote(new TextNote(result.get()));
							}
						}
					}
				}
				updateListView();
			}
		});

		vbox.getChildren().add(new Label("Choose folder: "));
		HBox hbox = new HBox(8); // spacing = 8
		hbox.getChildren().addAll(foldersComboBox, addNewFolder);
		vbox.getChildren().add(hbox);
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);
		vbox.getChildren().add(addNewNote);

		return vbox;
	}

	public void addFolder(String folderName) {
		noteBook.getFolders().add(new Folder(folderName));
	}

	private void updateListView() {
		ArrayList<String> list = new ArrayList<String>();

		// TODO populate the list object with all the TextNote titles of the
		for (Folder file : noteBook.getFolders()) {
			if (file.getName().compareTo(currentFolder) == 0) {
				for (Note note : file.getNotes()) {
					if (note instanceof TextNote) {
						if (file.searchNote(currentSearch).contains(note)) {
							list.add(note.getTitle());
						}
					}
				}
			}
		}

		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");
	}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private GridPane addGridPane() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		GridPane subGrid = new GridPane();
		subGrid.setHgap(10);
		subGrid.setVgap(10);

		TextField textBox = new TextField();
		Button searchButton = new Button("Search");
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				currentSearch = textBox.getText();
				textBox.setText(currentSearch);
				updateListView();
			}
		});
		Button clearSearchButton = new Button("Clear Search");
		clearSearchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				currentSearch = "";
				textBox.setText("");
				updateListView();
			}
		});
		Label label = new Label("Search : ");
		subGrid.add(searchButton, 2, 0);
		subGrid.add(clearSearchButton, 3, 0);
		subGrid.add(textBox, 1, 0);
		subGrid.add(label, 0, 0);
		grid.add(subGrid, 0, 0);

		subGrid.setHgap(10);
		subGrid.setVgap(10);
		ImageView saveView = new ImageView(new Image(
				new File("C:\\Users\\admin\\Desktop\\comp3021\\lab\\comp3021lab\\save.png").toURI().toString()));
		saveView.setFitHeight(18);
		saveView.setFitWidth(18);
		saveView.setPreserveRatio(true);

		Button saveNoteText = new Button("Save note");
		saveNoteText.setPrefSize(100, 20);
		saveNoteText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (currentFolder == null || currentNote == null) {
					System.out.println("here workss");
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setContentText("Please select a folder and a note");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				} else {
					for (Folder folder : noteBook.getFolders()) {
						if (folder.getName().compareTo(currentFolder) == 0)
							for (Note note : folder.getNotes()) {
								if (note.getTitle().compareTo(currentNote) == 0) {
									TextNote textnote = (TextNote) note;
									textnote.content = textAreaNote.getText();
									Alert alert = new Alert(AlertType.INFORMATION);
									alert.setTitle("Successfully saved");
									alert.setContentText("You file has been sucessfully saved ");
									alert.showAndWait().ifPresent(rs -> {
										if (rs == ButtonType.OK) {
											System.out.println("Pressed OK.");
										}
									});

								}
							}
					}
				}
			}
		});

		Button deleteNoteText = new Button("Delete note");
		deleteNoteText.setPrefSize(100, 20);
		deleteNoteText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currentFolder == null || currentNote == null) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setContentText("Please select a folder and a note");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				} else {
					for (Folder folder : noteBook.getFolders()) {
						if (folder.getName().compareTo(currentFolder) == 0) {
							if (folder.removeNotes(currentNote)) {
								currentNote = "";
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Successfully deleted");
								alert.setContentText("You file has been successfully deleted");
								alert.showAndWait().ifPresent(rs -> {
									if (rs == ButtonType.OK) {
										System.out.println("Pressed OK.");
									}
								});
							}
						}
					}
					updateListView();
				}
			}
		});

		GridPane subGrid2 = new GridPane();
		subGrid2.setHgap(10);
		subGrid2.setVgap(10);
		subGrid2.add(saveView, 0, 0);
		subGrid2.add(saveNoteText, 1, 0);
		subGrid2.add(deleteNoteText, 2, 0);
		grid.add(subGrid2, 0, 1);

		textAreaNote.setEditable(false);
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);
		textAreaNote.setEditable(true);
		// 0 0 is the position in the grid
		grid.add(textAreaNote, 0, 2);

		return grid;
	}

	private void saveNoteBook(File file, NoteBook nb) {
		String address = file.getAbsolutePath();
		// System.out.println(address);
		nb.save(address);
		// System.out.println(nb.getFolders().toString());
	}

	private void loadNoteBook(File file) {
		String address = file.getPath();
		// System.out.println(address);
		NoteBook nb = new NoteBook(address);
		noteBook = nb;
		// System.out.println(nb.getFolders().toString());
	}

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3021", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3021", "Lab requirement",
				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called “the most shocking play in NFL history” and the Washington Redskins dubbed the “Throwback Special”: the November 1985 play in which the Redskins’ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Award–winning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything—until it wasn’t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant—a part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether’s Daddy Was a Number Runner and Dorothy Allison’s Bastard Out of Carolina, Jacqueline Woodson’s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood—the promise and peril of growing up—and exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;

	}

}
