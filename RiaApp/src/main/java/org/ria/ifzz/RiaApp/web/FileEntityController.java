package org.ria.ifzz.RiaApp.web;

import org.ria.ifzz.RiaApp.domain.*;
import org.ria.ifzz.RiaApp.repository.GraphCurveRepository;
import org.ria.ifzz.RiaApp.service.FileValidator;
import org.ria.ifzz.RiaApp.repository.FileEntityRepository;
import org.ria.ifzz.RiaApp.repository.ResultRepository;
import org.ria.ifzz.RiaApp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:3000")
public class FileEntityController {

    private final StorageService storageService;
    private final FileEntityRepository fileEntityRepository;
    private final ResultService resultService;
    private final ResultRepository resultRepository;
    private final GraphCurveService graphCurveService;
    private final FileValidator fileValidator;
    private final MapValidationErrorService errorService;
    private final GraphCurveRepository graphCurveRepository;

    @Autowired
    public FileEntityController(StorageService storageService,
                                FileEntityRepository fileEntityRepository,
                                ResultService resultService,
                                ResultRepository resultRepository,
                                GraphCurveService graphCurveService,
                                FileValidator fileValidator,
                                MapValidationErrorService errorService,
                                GraphCurveRepository graphCurveRepository) {

        this.storageService = storageService;
        this.fileEntityRepository = fileEntityRepository;
        this.resultService = resultService;
        this.resultRepository = resultRepository;
        this.graphCurveService = graphCurveService;
        this.fileValidator = fileValidator;
        this.errorService = errorService;
        this.graphCurveRepository = graphCurveRepository;
    }

    @InitBinder
    protected void initBinderFileModel(WebDataBinder binder) {
        binder.setValidator(fileValidator);
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
    public ResponseEntity<?> handleFileUpload(@Valid FileModel file,
                                              BindingResult result,
                                              RedirectAttributes redirectAttributes,
                                              Principal principal) throws IOException {
        int newId = 0;
        ResponseEntity<?> errorMap = errorService.MapValidationService(result);
        if (errorMap != null) return errorMap;

        Backlog backlog = new Backlog();
        FileEntity fileEntity = storageService.storeAndSaveFileEntity(file.getFile(), backlog, redirectAttributes, principal.getName(), newId);
        Backlog currentBacklog = fileEntity.getBacklog();

        // Get data from uploaded file
        List<String> cleanedList = resultService.getFileData(file.getFile());

        // Result && Graph Curve
        List<Result> results = resultService.setDataToResult(file.getFile(), cleanedList, currentBacklog, fileEntity);
        resultRepository.saveAll(results);

        List<GraphCurve> graphCurveList =graphCurveService.setGraphCurveFileName(file.getFile(), fileEntity, currentBacklog);
        graphCurveRepository.saveAll(graphCurveList);

        return new ResponseEntity<>(fileEntity, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        storageService.delete(id);
        return new ResponseEntity<>("File with ID: '" + id + "' was deleted", HttpStatus.OK);
    }
}
