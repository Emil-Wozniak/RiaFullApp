package org.ria.ifzz.RiaApp.controller;

import org.ria.ifzz.RiaApp.domain.Backlog;
import org.ria.ifzz.RiaApp.domain.FileEntity;
import org.ria.ifzz.RiaApp.domain.Result;
import org.ria.ifzz.RiaApp.repositorie.BacklogRepository;
import org.ria.ifzz.RiaApp.repositorie.FileEntityRepository;
import org.ria.ifzz.RiaApp.repositorie.ResultRepository;
import org.ria.ifzz.RiaApp.service.ResultService;
import org.ria.ifzz.RiaApp.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:3000")
public class FileUploadController {

    private final StorageService storageService;
    private final FileEntityRepository fileEntityRepository;
    private final ResultService resultService;
    private final BacklogRepository backlogRepository;
    private final ResultRepository resultRepository;

    @Autowired
    public FileUploadController(StorageService storageService,
                                FileEntityRepository fileEntityRepository,
                                ResultService resultService,
                                BacklogRepository backlogRepository,
                                ResultRepository resultRepository) {

        this.storageService = storageService;
        this.fileEntityRepository = fileEntityRepository;
        this.resultService = resultService;
        this.backlogRepository = backlogRepository;
        this.resultRepository = resultRepository;
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> getFile() {
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

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        if (file.exists()) {
            HttpHeaders headers = new HttpHeaders();
            //instructing web browser how to treat downloaded file
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"");
            //allowing web browser to read additional headers from response
            headers.add("Access-Control-Expose-Headers", HttpHeaders.CONTENT_DISPOSITION + "," + HttpHeaders.CONTENT_LENGTH);

            //put headers and file within response body
            return ResponseEntity.ok().headers(headers).body(file);
        }
        //in case requested file does not exists
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{dataId}")
    public ResponseEntity<FileEntity> getFileEntityById(@PathVariable String dataId) throws FileNotFoundException {
        FileEntity fileEntity = storageService.getByDataId(dataId);
        return new ResponseEntity<>(fileEntity, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<FileEntity> getAllFiles() {
        return storageService.loadAll();
    }

    /**
     * it is responsible for handle a file upload and generate database tables,
     * then assign values from file to appropriate variables.
     *
     * @param file               which will be handle
     * @param redirectAttributes message shown if upload goes well
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity<Void> handleFileUpload(@NotNull @RequestParam("file") MultipartFile file,
                                                 RedirectAttributes redirectAttributes) throws IOException {

        FileEntity fileEntity = new FileEntity(file.getOriginalFilename(), file.getContentType(),
                file.getBytes());

        storageService.store(file, redirectAttributes);

//        redirectAttributes.addFlashAttribute("message",
//                "You successfully uploaded " + file.getOriginalFilename() + "!");

        fileEntityRepository.save(fileEntity);
        fileEntity.setDataId(fileEntity.getFileName() + "_" + fileEntity.getId());
        fileEntityRepository.save(fileEntity);

        Backlog backlog = new Backlog();
        backlog.setFileEntity(fileEntity);
        backlog.setFileName(fileEntity.getFileName());
        backlog.setDataId(fileEntity.getDataId());
        backlog.setContentType(fileEntity.getContentType());

        backlogRepository.save(backlog);

        fileEntity.setBacklog(backlog);
        fileEntityRepository.save(fileEntity);

        List<String> cleanedList = resultService.getFileData(file);

        resultService.setResultFromColumnsLength(cleanedList, file, backlog);
        Result result = resultService.assignDataToResult(cleanedList, file, fileEntity);
        resultRepository.save(result);

        result = resultService.assignNgPerMl(file, cleanedList);
        resultRepository.save(result);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).build();
    }

}



