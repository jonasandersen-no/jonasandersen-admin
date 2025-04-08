package no.jonasandersen.admin.adapter.in.web;

import java.util.List;
import no.jonasandersen.admin.application.port.SaveFiles;
import no.jonasandersen.admin.domain.SaveFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/save-file")
public class SaveFileController {

  private final SaveFiles saveFiles;

  public SaveFileController(SaveFiles saveFiles) {
    this.saveFiles = saveFiles;

    // Temporary until we create it via UI
    saveFiles.create("A save file name");
    saveFiles.create("Another save file name");
  }

  @GetMapping
  String listAllSaveFiles(Model model) {
    List<SaveFile> files = saveFiles.findAll();
    List<SaveFileRecord> records =
        files.stream().map(saveFile -> new SaveFileRecord(saveFile.getName())).toList();

    model.addAttribute("files", records);

    return "saveFile/index";
  }

  record SaveFileRecord(String name) {}
}
