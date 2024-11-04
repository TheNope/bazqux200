package com.thenope.bazqux200.views;

import com.thenope.bazqux200.condense.Condenser;
import com.thenope.bazqux200.config.ConfigLoader;
import com.thenope.bazqux200.config.classes.AppConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.util.Objects;

public class CondenseViewController {
    @FXML
    private Button condenseButton;

    @FXML
    private ProgressBar copyProgressBar;

    @FXML
    private Label copiedLabel;

    @FXML
    private Label existingLabel;

    @FXML
    private Label notFoundLabel;

    @FXML
    private Label removedLabel;

    @FXML
    protected void onCondenseButtonClick() {
        condenseButton.setDisable(true);
        AppConfig appConfig = Objects.requireNonNull(ConfigLoader.getConfig(AppConfig.class));
        Condenser condenser = new Condenser(appConfig.getLibraryConfig().getLocation(), appConfig.getCondenseConfig());
        condenser.setOnSucceeded(_ -> condenseButton.setDisable(false));

        copyProgressBar.progressProperty().bind(condenser.copyProgressProperty());
        copiedLabel.textProperty().bind(condenser.countCopiedProperty().asString());
        existingLabel.textProperty().bind(condenser.countExistingProperty().asString());
        notFoundLabel.textProperty().bind(condenser.countNotFoundProperty().asString());
        removedLabel.textProperty().bind(condenser.countRemovedProperty().asString());

        Thread condenseThread = new Thread(condenser);
        condenseThread.setDaemon(true);
        condenseThread.start();
    }
}
