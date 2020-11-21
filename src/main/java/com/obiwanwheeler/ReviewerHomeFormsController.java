package com.obiwanwheeler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class ReviewerHomeFormsController {

    @FXML private Button startReviewButton;

    @FXML private void onStartReviewButtonPressed() throws IOException {
        App.setRoot(App.fxmlFiles.get("card_front"));
    }

}
