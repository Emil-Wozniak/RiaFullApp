package org.ria.ifzz.RiaApp;

import io.micrometer.core.instrument.util.IOUtils;
import org.ria.ifzz.RiaApp.storage.StorageFileNotFoundException;
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

import java.io.*;
import java.util.Objects;
import java.util.Scanner;
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

        ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
        String content =  readFromInputStream(stream);
//        content.trim();

        String startWord = "C:\\mbw\\results\\A16_225.txtRUN INFORMATION:================Counting protocol no: 16                                  Tue  8-Jan-2019  0:47Name: COPY_OF_H-3_KORTYZOL_5_MINCPM normalization protocol no:  2*** DETECTORS NOT NORMALIZEDCOLUMNS:======== \tSAMPLE\tPOS\tCCPM1\tCCPM1%\t \t";

        System.out.println(startWord.length() + "\n");

        System.out.println(content);

        return "redirect:/";
    }


    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                resultStringBuilder.append(line);
                resultStringBuilder.replace(0, 13, "").toString();
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}


