package com.example.CloudStorage.repository;

import com.example.CloudStorage.entity.CloudFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Repository
public class CloudRepository {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, UserDetails> tokenStorage = new ConcurrentHashMap<>();

    @Value("${cloudKeeper.path.application-dir}")
    private String appPath;
    @Value("${cloudKeeper.path.upload-dir}")
    private String downloadsPath;
    @Value("${cloudKeeper.file.download.outputStream.size}")
    private int downloadFileOutputStreamSize;

    public CloudRepository() {
//        createDirIfNotExists(appPath);
//        createDirIfNotExists(downloadsPath);
    }

    public Optional<UserDetails> login(String authToken, UserDetails userPrincipal){
        return ofNullable(tokenStorage.put(authToken, userPrincipal));
    }

    public Optional<UserDetails> logout(String authToken) {
         return ofNullable(tokenStorage.remove(authToken));
    }

    public Optional<CloudFile> uploadFile(CloudFile cloudFile, String authToken) {
        Optional<Long> userId = getUserId(authToken);
        if (!userId.isEmpty()) {
            cloudFile.setUserId(userId.get());
            return ofNullable(fileRepository.save(cloudFile));
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Long> removeFile(String authToken, String fileName) {
        Optional<Long> userId = getUserId(authToken);
        if (!userId.isEmpty()) {
            Long fileId = fileRepository.removeByUserIdAndName(userId.get(), fileName);
            return ofNullable(fileId);
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<CloudFile> downloadFile(String authToken, String fileName) {
        Optional<Long> userId = getUserId(authToken);
        if (!userId.isEmpty()) {
            CloudFile cloudFile = fileRepository.findByUserIdAndName(userId.get(), fileName);
            String userDownloadsDirPath =  downloadsPath + File.separator + userRepository.getById(userId.get()).getUsername() + File.separator;

//            createDirIfNotExists(userDownloadsDirPath);
//            String absoluteFilePath = userDownloadsDirPath + File.separator + fileName;

//            try (var fis = new FileInputStream(absoluteFilePath);
//                 var bis = new BufferedInputStream(fis, downloadFileOutputStreamSize);
//                 var fos = new FileOutputStream(absoluteFilePath);
//                 var bos = new BufferedOutputStream(fos, downloadFileOutputStreamSize)
//            ) {
//                int i;
//                while ((i = bis.read()) != -1) {
//                    bos.write(i);
//                }
                return ofNullable(cloudFile);
//            } catch (IOException e) {
//                return Optional.empty();
//            }
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<List<CloudFile>> getFiles(String authToken, int limit) {
        Optional<Long> userId = getUserId(authToken);
        if (!userId.isEmpty()) {
            return ofNullable(fileRepository.findAllByUserId(userId.get()));
        }
        return Optional.empty();
    }

    private Optional<Long> getUserId(String authToken) {
        UserDetails user = tokenStorage.get(authToken.substring(7));
        return user != null ? ofNullable(userRepository.findByLogin(user.getUsername()).get().getId()) : Optional.empty();
    }

    private void createDirIfNotExists(String dirName) {
        if (!new File(dirName).isDirectory()) {
            // createDir
        };
    }
}
