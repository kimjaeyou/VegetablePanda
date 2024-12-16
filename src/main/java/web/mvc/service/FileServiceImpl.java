package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.File;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.StockException;
import web.mvc.repository.FileRepository;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public File save(File file) {
        return fileRepository.save(file);
    }

    @Override
    public File findById(long id) {
        return fileRepository.findById(id).orElseThrow(()-> new StockException(ErrorCode.FILE_NOTFOUND));
    }
}
