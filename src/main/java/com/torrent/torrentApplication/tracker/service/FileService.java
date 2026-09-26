package com.torrent.torrentApplication.tracker.service;

import com.torrent.torrentApplication.auth.Model.User;
import com.torrent.torrentApplication.auth.Repository.UserRepository;
import com.torrent.torrentApplication.tracker.dto.FileListPerson;
import com.torrent.torrentApplication.tracker.model.FileMetadata;
import com.torrent.torrentApplication.tracker.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public FileService(FileRepository fileRepository,UserRepository userRepository){
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    public List<FileListPerson> getFileByEmail(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Long userId = user.getUser_id();

        List<FileMetadata> files =
                fileRepository.findByUploaderUserId(userId);

        List<FileListPerson> result = new ArrayList<>();

        for (FileMetadata file : files) {

            FileListPerson dto = new FileListPerson();

            dto.setFileId(file.getFile_id());
            dto.setFileName(file.getFileName());
            dto.setFileSize(file.getFileSize());
            dto.setShareCode(file.getShareCode());
            dto.setTotalNoOfPieces(file.getTotalPieces());
            dto.setPieceSize(file.getPieceSize());

            result.add(dto);
        }

        return result;
    }

    public FileMetadata RegisterFile(FileMetadata file){

        Optional<FileMetadata> existing =
                fileRepository.findByFileHash(file.getFileHash());

        if(existing.isPresent()){
            return existing.get();
        }

        file.setShareCode(
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase()
        );

        return fileRepository.save(file);
    }
    public FileMetadata getFileByShareCode(String shareCode) {
        return fileRepository.findByShareCode(shareCode);
    }
    public FileMetadata save(FileMetadata file) {
        return fileRepository.save(file);
    }
}
