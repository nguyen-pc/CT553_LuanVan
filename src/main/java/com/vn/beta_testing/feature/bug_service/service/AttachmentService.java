package com.vn.beta_testing.feature.bug_service.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

import com.vn.beta_testing.domain.Attachment;
import com.vn.beta_testing.domain.BugReport;
import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.Survey;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.feature.bug_service.DTO.AttachmentDTO;
import com.vn.beta_testing.feature.bug_service.repository.AttachmentRepository;
import com.vn.beta_testing.feature.survey_service.repository.FileRepository;
import com.vn.beta_testing.util.SecurityUtil;
import com.vn.beta_testing.util.error.StorageException;

import jakarta.transaction.Transactional;

@Service
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    @Value("${beta_testing.upload-file.base-uri}")
    private String baseURI;

    @Value("${beta_testing.upload-file.base-url}")
    private String baseURL;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    public void createUploadFolder(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (!tmpDir.isDirectory()) {
            try {
                Files.createDirectory(tmpDir.toPath());
                System.out.println("<<<<<< Create new directory successful, path = " + tmpDir.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("<<<<<< Skip making directory already exists");
        }
    }

    public String store(MultipartFile file, String folder, Campaign campaign, User uploader)
            throws URISyntaxException, IOException {
        // create unique filename
        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        URI uri = new URI(baseURI + folder + "/" + finalName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }
        // Lưu vào database
        Attachment newFile = new Attachment();
        newFile.setFileName(finalName);
        newFile.setFileType(file.getContentType());
        newFile.setFileSize(file.getSize());
        newFile.setCampaign(campaign);
        newFile.setUploader(uploader);
        newFile.setCreatedBy(SecurityUtil.getCurrentUserLogin().orElse(""));

        attachmentRepository.save(newFile);
        return finalName;
    }

    public String storeBug(MultipartFile file, String folder, BugReport bug, User uploader)
            throws URISyntaxException, IOException {
        // create unique filename
        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        URI uri = new URI(baseURI + folder + "/" + finalName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }
        // Lưu vào database
        Attachment newFile = new Attachment();
        newFile.setFileName(finalName);
        newFile.setFileType(file.getContentType());
        newFile.setFileSize(file.getSize());
        newFile.setBugReport(bug);
        newFile.setUploader(uploader);
        newFile.setCreatedBy(SecurityUtil.getCurrentUserLogin().orElse(""));

        attachmentRepository.save(newFile);
        return finalName;
    }

    public String storeSingle(MultipartFile file, String folder) throws URISyntaxException,
            IOException {
        // create unique filename
        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        URI uri = new URI(baseURI + folder + "/" + finalName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        return finalName;
    }

    public long getFileLength(String fileName, String folder) throws URISyntaxException {
        URI uri = new URI(baseURI + folder + "/" + fileName);
        Path path = Paths.get(uri);

        File tmpDir = new File(path.toString());

        // file khong ton tai, hoac file la 1 directory => return 0
        if (!tmpDir.exists() || tmpDir.isDirectory()) {
            System.out.println("<<<<<< File not found or is a directory");
            return 0;
        }
        return tmpDir.length();
    }

    public InputStreamResource getResource(String fileName, String folder)
            throws URISyntaxException, FileNotFoundException {
        URI uri = new URI(baseURI + folder + "/" + fileName);
        Path path = Paths.get(uri);

        File file = new File(path.toString());
        return new InputStreamResource(new FileInputStream(file));
    }

    public void deleteFile(String fileName, String folder) throws URISyntaxException {
        URI uri = new URI(baseURI + folder + "/" + fileName);
        Path path = Paths.get(uri);

        File file = new File(path.toString());
        if (file.exists()) {
            file.delete();
        }
    }

    public List<String> listFiles(String folder) throws StorageException {
        if (folder.contains(",")) {
            throw new StorageException("Invalid folder name: " + folder);
        }

        String folderPath = Paths.get(baseURL, folder).toString();
        System.out.println("Checking folder path: " + folderPath); // Debug

        File dir = new File(folderPath);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new StorageException("Folder does not exist or is not a directory: " + folderPath);
        }

        File[] files = dir.listFiles();
        List<String> fileNames = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }

        return fileNames;
    }

    // public List<String> getFilesBySurveyId(Long surveyId) {
    // List<com.project.formhub.domain.File> files =
    // fileRepository.findBySurvey_SurveyId(surveyId);
    // return
    // files.stream().map(com.project.formhub.domain.File::getFileName).collect(Collectors.toList());
    // }

    // public List<Attachment> getFilesBySurveyId(Long surveyId) {
    // List<Attachment> files =
    // attachmentRepository.findBySurvey_SurveyId(surveyId);
    // if (files == null) {
    // return null;
    // }
    // return files;
    // }

    private AttachmentDTO toDTO(Attachment entity) {
        AttachmentDTO dto = new AttachmentDTO();
        dto.setId(entity.getId());
        dto.setFileName(entity.getFileName());
        dto.setFileUrl(entity.getFileUrl());
        dto.setFileType(entity.getFileType());
        dto.setFileSize(entity.getFileSize());
        dto.setUploadedAt(entity.getUploadedAt());

        if (entity.getUploader() != null) {
            dto.setUploaderId(entity.getUploader().getId());
            dto.setUploaderName(entity.getUploader().getName());
            dto.setUploaderEmail(entity.getUploader().getEmail());
        }

        if (entity.getCampaign() != null) {
            dto.setCampaignId(entity.getCampaign().getId());
            dto.setCampaignName(entity.getCampaign().getTitle());
        }

        return dto;
    }

    public List<AttachmentDTO> getVideoFilesByCampaignId(Long campaignId) {
        List<String> videoTypes = Arrays.asList("video/mp4", "video/webm");

        List<Attachment> files = attachmentRepository.findByCampaign_IdAndFileTypeIn(campaignId, videoTypes);

        // Nếu backend của bạn lưu fileType không chuẩn MIME, có thể thay thế bằng
        // endsWith:
        // List<Attachment> files = attachmentRepository.findByCampaign_Id(campaignId)
        // .stream()
        // .filter(f -> f.getFileName().toLowerCase().endsWith(".mp4") ||
        // f.getFileName().toLowerCase().endsWith(".webm"))
        // .collect(Collectors.toList());

        return files.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AttachmentDTO> getAttachmentsByBugId(Long bugId) {
        List<Attachment> attachments = attachmentRepository.findByBugReport_Id(bugId);
        return attachments.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public void deleteFileRecord(Attachment file) {
        attachmentRepository.delete(file);
    }

    public Attachment getFileRecord(String fileName, String folder) {
        return attachmentRepository.findByFileName(fileName)
                .orElse(null);
    }

}
