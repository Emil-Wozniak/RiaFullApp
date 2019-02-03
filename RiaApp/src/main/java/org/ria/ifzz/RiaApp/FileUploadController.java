package org.ria.ifzz.RiaApp;

import org.ria.ifzz.RiaApp.storage.StorageService;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {


    private final StorageService storageService;
    private final CustomFileReader customFileReader;

    @Autowired
    public FileUploadController(StorageService storageService,
                                CustomFileReader customFileReader) {
        this.storageService = storageService;
        this.customFileReader = customFileReader;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) {

        String removePart = "http://localhost:8080/files/";

        model.addAttribute("trim", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString().replace(removePart, ""))
                .collect(Collectors.toList()));

        return "index";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        System.out.println(customFileReader.getUploadComment());
        List<String> readStoreTxtFileList = customFileReader.readStoredTxtFile(file);
        List<String> cleanedList = customFileReader.cleanStoredTxtFile(readStoreTxtFileList);

        List index = customFileReader.getIndex(cleanedList, 1);
        System.out.println("Index: " + index);
        List position = customFileReader.getMatchingStrings(cleanedList, 2);
        System.out.println("Position: " + position);
        List ccpm = customFileReader.getMatchingStrings(cleanedList, 3);
        System.out.println("CCPM: " + ccpm);

        return "redirect:/";
    }
}



