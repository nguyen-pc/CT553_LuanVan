package com.vn.beta_testing.feature.bug_service.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vn.beta_testing.domain.Attachment;
import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.File;
import com.vn.beta_testing.domain.Survey;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.domain.response.file.ResUploadFileDTO;
import com.vn.beta_testing.feature.auth_service.service.UserService;
import com.vn.beta_testing.feature.bug_service.DTO.AttachmentDTO;
import com.vn.beta_testing.feature.bug_service.service.AttachmentService;
import com.vn.beta_testing.feature.company_service.service.CampaignService;
import com.vn.beta_testing.feature.survey_service.service.FileService;
import com.vn.beta_testing.feature.survey_service.service.SurveyService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.util.error.StorageException;

@RestController
@RequestMapping("/api/v1")
public class AttachmentController {
    private final AttachmentService attachmentService;
    private final CampaignService campaignService;
    private final UserService userService;

    @Value("${beta_testing.upload-file.base-uri}")
    private String baseURI;

    @Value("${beta_testing.upload-file.base-url}")
    private String baseURL;

    public AttachmentController(AttachmentService attachmentService, CampaignService campaignService,
            UserService userService) {
        this.attachmentService = attachmentService;
        this.campaignService = campaignService;
        this.userService = userService;
    }

    @PostMapping("/attachment")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> upload(@RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder, @RequestParam("uploader") String uploader)
            throws URISyntaxException, IOException, StorageException {

        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty. Please upload a file");
        }

        long campaignId = Long.parseLong(folder);
        Long uploaderId = Long.parseLong(uploader);
        Campaign campaign = campaignService.fetchCampaignById(campaignId);
        User uploaderUser = userService.fetchUserById(uploaderId);

        this.attachmentService.createUploadFolder(baseURI + folder);

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx", "mp4", "mp3",
                "webm");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));

        if (!isValid) {
            throw new StorageException("Invalid file extension. Only allow" + allowedExtensions);
        }

        // Store files
        String uploadFile = this.attachmentService.store(file, folder, campaign, uploaderUser);
        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());
        return ResponseEntity.ok().body(res);

    }

    @PostMapping("/attachment/single")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> upload(@RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {

        // skip validate
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty. Please upload a file");
        }

        // create a directory if not exist
        this.attachmentService.createUploadFolder(baseURI + folder);

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));

        if (!isValid) {
            throw new StorageException("Invalid file extension. Only allow" + allowedExtensions);
        }
        // store file
        String uploadFile = this.attachmentService.storeSingle(file, folder);

        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/attachment")
    public ResponseEntity<Resource> download(@RequestParam(name = "folder", required = false) String folder,
            @RequestParam(name = "fileName", required = false) String fileName)
            throws URISyntaxException, FileNotFoundException, StorageException {

        // check file exist (and not a directory)
        long fileLength = this.attachmentService.getFileLength(fileName, folder);
        if (fileLength == 0) {
            throw new StorageException("File not found or is a directory");
        }

        // download file from storage
        InputStreamResource resource = this.attachmentService.getResource(fileName, folder);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(fileLength).contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    // @GetMapping("/files/{surveyId}/list")
    // public ResponseEntity<List<File>> listFiles(
    // @PathVariable("surveyId") long surveyId) throws StorageException {

    // List<File> files = attachmentService.getFilesBySurveyId(surveyId);
    // return ResponseEntity.ok(files);
    // }

    @DeleteMapping("/attachment")
    public ResponseEntity<String> deleteFile(
            @RequestParam(name = "folder") String folder,
            @RequestParam(name = "fileName") String fileName) throws URISyntaxException, StorageException {

        if (fileName == null || folder == null) {
            throw new StorageException("Missing required parameters");
        }

        // Kiểm tra file có tồn tại trong CSDL không
        Attachment fileRecord = attachmentService.getFileRecord(fileName, folder);
        System.out.println(">>><<<<<<<<<<<<<<" + fileRecord);
        if (fileRecord == null) {
            throw new StorageException("File not found in database");
        }

        // Kiểm tra file có tồn tại trong hệ thống không
        long fileLength = this.attachmentService.getFileLength(fileName, folder);
        if (fileLength == 0) {
            throw new StorageException("File not found or is a directory");
        }

        // Xóa file trên hệ thống
        this.attachmentService.deleteFile(fileName, folder);

        // Xóa file trong CSDL
        this.attachmentService.deleteFileRecord(fileRecord);

        return ResponseEntity.ok("File deleted successfully");
    }

    @GetMapping("/attachment/campaign/{campaignId}/videos")
    @ApiMessage("Get all video attachments by campaign ID successfully")
    public ResponseEntity<List<AttachmentDTO>> getVideoAttachmentsByCampaignId(
            @PathVariable("campaignId") long campaignId) {

        List<AttachmentDTO> videoFiles = attachmentService.getVideoFilesByCampaignId(campaignId);
        return ResponseEntity.ok(videoFiles);
    }
}
