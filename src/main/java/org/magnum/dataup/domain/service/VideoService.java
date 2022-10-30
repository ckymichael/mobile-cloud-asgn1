package org.magnum.dataup.domain.service;

import org.magnum.dataup.domain.model.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Optional;

public interface VideoService {

    /**
     * get all video
     *
     * @return list of all video
     */
    Collection<Video> getAllVideos();

    /**
     * get video by id
     *
     * @param id id
     * @return video
     */
    Optional<Video> getVideo(long id);

    /**
     *  add video
     *
     * @param v video
     */
    void addVideo(Video... v);

    /**
     * write video data into file system
     *
     * @param id        id
     * @param videoData video data from user
     * @return
     */
    boolean setVideoData(long id, MultipartFile videoData);

    /**
     * get video data from file system
     *
     * @param id           id
     * @param outputStream
     * @return video data
     */
    boolean getData(long id, OutputStream outputStream);
}
