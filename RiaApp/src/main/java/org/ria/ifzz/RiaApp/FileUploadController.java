package org.ria.ifzz.RiaApp;

import org.ria.ifzz.RiaApp.storage.StorageService;
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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {


    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

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
        
        System.out.println("File content:\n========================================================");

        List<String> readStoreTxtFileList = readStoredTxtFile(file);
        cleanStoredTxtFile(readStoreTxtFileList);
        return "redirect:/";
    }

    private List<String> readStoredTxtFile(MultipartFile file) throws IOException {
        List<String> list;
        try (BufferedReader reader = Files.newBufferedReader(
                Paths.get("upload-dir" + "/" + file.getOriginalFilename()))) {
            list = reader.lines()
                    .skip(13)
                    .limit(500)
                    .collect(Collectors.toList());
        }
        return list;
    }

    private List<String> cleanStoredTxtFile(List<String> list) throws IOException {
        list.stream().filter(line->!line.startsWith("U")).collect(Collectors.toList());
        list.removeIf(line -> line.startsWith("P"));
        list.removeIf(line -> line.startsWith("C"));
        list.removeIf(line -> line.startsWith("A"));
        list.removeIf(line -> line.startsWith("E"));
        list.removeIf(line -> line.startsWith("T"));
        list.removeIf(line -> line.startsWith("="));
        list.removeIf(line -> line.startsWith("B"));
        list.removeIf(line -> line.startsWith("D"));
        list.removeIf(line -> line.startsWith(" \t1"));
        list.removeAll(Collections.singleton(null));

        list.forEach(System.out::println);
        return list;
    }

}



