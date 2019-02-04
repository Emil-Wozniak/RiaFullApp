package org.ria.ifzz.RiaApp;

import org.ria.ifzz.RiaApp.storage.FileEntity;
import org.ria.ifzz.RiaApp.storage.FileEntityRepository;
import org.ria.ifzz.RiaApp.storage.StorageService;
import org.ria.ifzz.RiaApp.utils.CustomFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequestMapping("/api/files")
@CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
public class FileUploadController {


    private final StorageService storageService;
    private final CustomFileReader customFileReader;
    private final FileEntityRepository fileEntityRepository;

    @Autowired
    public FileUploadController(StorageService storageService,
                                CustomFileReader customFileReader, FileEntityRepository fileEntityRepository) {
        this.storageService = storageService;
        this.customFileReader = customFileReader;
        this.fileEntityRepository = fileEntityRepository;
    }

    @GetMapping
    public ResponseEntity<byte[]> getRandomFile() {
        long amountOfFiles = fileEntityRepository.count();
        Long randomPrimaryKey;

        if (amountOfFiles == 0) {
            return ResponseEntity.ok(new byte[0]);
        } else if (amountOfFiles == 1) {
            randomPrimaryKey = 1L;
        } else {
            randomPrimaryKey = ThreadLocalRandom.current().nextLong(1, amountOfFiles + 1);
        }

        FileEntity fileEntity = fileEntityRepository.findById(randomPrimaryKey).get();

        HttpHeaders header = new HttpHeaders();

        header.setContentType(MediaType.valueOf(fileEntity.getContentType()));
        header.setContentLength(fileEntity.getData().length);
        header.set("Content-Disposition", "attachment; filename=" + fileEntity.getFileName());

        return new ResponseEntity<>(fileEntity.getData(), header, HttpStatus.OK);
    }

//    @GetMapping("/")
//    public String listUploadedFiles(Model model) {
//
//        String removePart = "http://localhost:8080/files/";
//
//        model.addAttribute("trim", storageService.loadAll().map(
//                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//                        "serveFile", path.getFileName().toString()).build().toString().replace(removePart, ""))
//                .collect(Collectors.toList()));
//
//        return "index";
//    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping
    public ResponseEntity<Void> handleFileUpload(@NotNull @RequestParam("file") MultipartFile file,
                                                   RedirectAttributes redirectAttributes) throws IOException {

        FileEntity fileEntity = new FileEntity(file.getOriginalFilename(), file.getContentType(),
                file.getBytes());

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
        fileEntityRepository.save(fileEntity);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping("/upload-file")
    public void uploadFile (MultipartHttpServletRequest multipartHttpServletRequest) {
        //save on server logic comes here
    }
}



