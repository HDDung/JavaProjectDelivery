package utils;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

/**
 * source:
 * http://stackoverflow.com/questions/13841884/redirecting-system-out-to-a-textarea-in-javafx
 * Created by dung on 25.12.16.
 */
public class Console extends OutputStream {
    private TextArea console;

    public Console(TextArea console) {
        this.console = console;
    }

    public void appendText(String valueOf) {
        Platform.runLater(() -> console.appendText(valueOf));
    }

    public void write(int b) throws IOException {
        appendText(String.valueOf((char) b));
    }
}
