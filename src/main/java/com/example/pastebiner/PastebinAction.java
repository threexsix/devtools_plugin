package com.example.pastebiner;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PastebinAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        Project project = anActionEvent.getProject();
        Editor editor = anActionEvent.getRequiredData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR);
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();

        if (selectedText != null && !selectedText.isEmpty()) {
            try {
                // URL request
                URL url = new URL("https://pastebin.com/api/api_post.php");
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);

                // API params
                String data =
                            "api_dev_key=" + "H35oPSlab6RscwFIGnfK-rQoG5rxCS37" +
                            "&api_option=paste" +
                            "&api_paste_code=" + java.net.URLEncoder.encode(selectedText, StandardCharsets.UTF_8);

                byte[] out = data.getBytes(StandardCharsets.UTF_8);

                OutputStream stream = http.getOutputStream();
                stream.write(out);

                java.util.Scanner scanner = new java.util.Scanner(http.getInputStream()).useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";

                http.disconnect();

                // URL response
                Messages.showMessageDialog(project, "Successfully posted: " + response, "Success", Messages.getInformationIcon());
            } catch (Exception e) {
                e.printStackTrace();
                Messages.showErrorDialog(project, "Error : " + e.getMessage(), "ERROR");
            }
        }
        else {
            Messages.showErrorDialog(project, "Nothing selected", "ERROR");
        }
    }
}