package web.mvc.service;

import web.mvc.domain.File;

public interface FileService {
    /**
     * 파일 저장
     */
    public File save(File file);
}
