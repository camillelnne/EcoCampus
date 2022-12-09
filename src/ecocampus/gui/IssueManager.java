package ecocampus.gui;

import ecocampus.Status;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

/**
 * Displays the issue
 */
public class IssueManager {

    private final VBox vBox;
    private final Bean bean;
    private final Font TITLEFONT = new Font("Helvetica", 16);
    private final Font RESULTFONT = new Font("Helvetica",12);
    private final Font LOGINFONT = Font.font("Helvetica", FontWeight.BOLD, 12);
    private final Stage mainStage;

    /**
     * Constructor
     *
     */
    public IssueManager(Bean bean, Stage mainStage) throws FileNotFoundException {

        this.mainStage = mainStage;
        this.bean = bean;
        vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPrefWidth(250);
        vBox.setStyle("-fx-background-color: #bfdcb2;");
        Insets insets = new Insets(10,10,10,10);
        vBox.setPadding(insets);
        if (bean.getObservedissue().get().isEmpty() || bean.getCurrentUserId() == bean.getObservedissue().get().getAuthor()) initTextAndLabel();
        else showText();

    }

    /**
     * Initializes the text area to fill and their labels
     */
    private void initTextAndLabel() throws FileNotFoundException {
        Issue issue = bean.getObservedissue().get();

        Label connectedAs = new Label("Logged in as " + bean.getCurrentUserName());
        connectedAs.setFont(LOGINFONT);
        connectedAs.setWrapText(true);
        vBox.getChildren().add(connectedAs);

        Label title = new Label("Title of the issue");
        title.setFont(TITLEFONT);
        TextField textFieldTitle = new TextField();
        textFieldTitle.setText(issue.getTitle());
        vBox.getChildren().addAll(title, textFieldTitle);

        Label location = new Label("Location of the issue");
        TextField textFieldLocation = new TextField();
        location.setFont(TITLEFONT);
        textFieldLocation.setText(issue.getLocation());
        vBox.getChildren().addAll(location, textFieldLocation);

        Label description = new Label("Description");
        description.setFont(TITLEFONT);
        TextArea areaDescr = new TextArea();
        areaDescr.setWrapText(true);
        areaDescr.setText(issue.getDescription());
        vBox.getChildren().addAll(description, areaDescr);

        textFieldLocation.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    bean.getObservedissue().get().setEverything(textFieldTitle.getText(), areaDescr.getText(), textFieldLocation.getText());
                    showText();
                }
            }});

        areaDescr.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    bean.getObservedissue().get().setEverything(textFieldTitle.getText(), areaDescr.getText(), textFieldLocation.getText());
                    showText();
                }
            }});

        //Creating the image view
        ImageView imageView = new ImageView();
        vBox.getChildren().add(imageView);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(200);
        imageView.setFitHeight(100);
        Button chooseFile = new Button("Choose File");
        vBox.getChildren().add(chooseFile);

        File prev_file = new File("resources/" + issue.getId() + ".png");
        if (prev_file.exists() && prev_file.canRead() && prev_file.length() != 0) {
            FileInputStream stream = new FileInputStream(prev_file);
            Image image = new Image(stream);
            imageView.setImage(image);
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        fileChooser.setInitialDirectory(new File("."));


        chooseFile.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(mainStage);
            if (file != null) {
                if (file.exists() && file.canRead() && file.length() != 0) {
                    try {
                        FileInputStream stream = new FileInputStream(file);
                        Image image = new Image(stream);
                        imageView.setImage(image);

                        InputStream in = new FileInputStream(file);
                        OutputStream out = new FileOutputStream("resources/"+bean.getObservedissue().get().getId()+".png");

                        // Transfer all byte from in to out
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("File doesn't exist or can't be read");
                }
            }
        });


        try{
            Label imageLabel = new Label("Image of the issue");
            imageLabel.setFont(TITLEFONT);
        }catch (Exception e){
            System.out.println(e);
        }
        Button enter = new Button("Submit");
        enter.setTextAlignment(TextAlignment.CENTER);
        vBox.getChildren().add(enter);
        enter.setOnAction(e -> {
            bean.getObservedissue().get().setEverything(textFieldTitle.getText(), areaDescr.getText(), textFieldLocation.getText());
            showText();
        });


        Button cancel = new Button("Cancel");
        cancel.setTextAlignment(TextAlignment.CENTER);
        vBox.getChildren().add(cancel);
        cancel.setOnAction(e -> {
            vBox.getChildren().clear();
            if (bean.getObservedissue().get().isEmpty()) {
                bean.removeLastWaypoint();
            }
            bean.getObservedissue().set(null);
        });
    }



    /**
     * Displays the labels and text fields to fill
     */
    public void showText(){
        vBox.getChildren().clear();

        Label connectedAs = new Label("Logged in as " + bean.getCurrentUserName());
        connectedAs.setFont(LOGINFONT);
        connectedAs.setWrapText(true);
        vBox.getChildren().add(connectedAs);

        Issue issue = bean.getObservedissue().get();
        Label title = new Label("Title of the issue:");
        title.setFont(TITLEFONT);
        Label textFieldTitle = new Label(issue.getTitle());
        textFieldTitle.setFont(RESULTFONT);
        vBox.getChildren().addAll(title, textFieldTitle);
        Label location = new Label("Location:");
        location.setFont(TITLEFONT);
        Label locationText = new Label(issue.getLocation());
        locationText.setFont(RESULTFONT);
        vBox.getChildren().addAll(location, locationText);
        Label description = new Label("Description:");
        description.setFont(TITLEFONT);
        Label areaDescr = new Label(issue.getDescription());

        areaDescr.setWrapText(true);
        areaDescr.setMinHeight(50);
        areaDescr.setFont(RESULTFONT);
        vBox.getChildren().addAll(description, areaDescr);
        Label upVotesLabel = new Label("Number of UpVotes: " + bean.getObservedissue().get().getUpVotes());
        upVotesLabel.setFont(TITLEFONT);
        vBox.getChildren().addAll(upVotesLabel);
        //image:
        //Creating the image view
        ImageView imageView = new ImageView();
        vBox.getChildren().add(imageView);
        try {
            InputStream stream = new FileInputStream("resources/"+bean.getObservedissue().get().getId()+".png");
            Image image = new Image(stream);
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
        }catch (Exception e){
            System.out.println("cassé");
        }

        Button edit = new Button("Edit issue");
        edit.setTextAlignment(TextAlignment.CENTER);
        if (bean.getCurrentUserId() == bean.getObservedissue().get().getId()){
            vBox.getChildren().add(edit);
            edit.setOnAction(e -> {
                vBox.getChildren().clear();
                try {
                    initTextAndLabel();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }


        //can't upvote if the issue is resolved
        if(issue.getStatus() == Status.UNRESOLVED){
        try {
            InputStream stream = new FileInputStream("resources/upvote.png");

            Image thumbsUp = new Image(stream);
            ImageView upvote = new ImageView(thumbsUp);
            upvote.setPreserveRatio(true);
            upvote.setFitWidth(50);

            upvote.setOnMousePressed(event -> {
                if (!bean.getUsers().get(bean.getCurrentUserId()).hasAlreadyVoted(bean.getObservedissue().get().getId())){
                    issue.setUpVotes(issue.getUpVotes()+1);
                    bean.drawProperty().set(!bean.drawProperty().get());
                    bean.getUsers().get(bean.getCurrentUserId()).addUpvote(bean.getObservedissue().get().getId());
                    showText();
                }
            });
            vBox.getChildren().add(upvote);
        }
        catch (Exception e){

        }
        }
    }
    /**
     * @return the vbox
     */
    public VBox getVBox() {
        return vBox;
        
    }
}
