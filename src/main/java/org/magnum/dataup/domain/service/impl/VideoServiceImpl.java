package org.magnum.dataup.domain.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.magnum.dataup.domain.model.NewVideoProcessor;
import org.magnum.dataup.domain.model.Video;
import org.magnum.dataup.domain.repository.VideoRepository;
import org.magnum.dataup.domain.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    VideoFileManager videoFileManager;

    @Autowired
    VideoRepository videoRepository;

    /**
     * get all video
     *
     * @return list of all video
     */
    @Override
    public Collection<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    /**
     * get video by id
     *
     * @param id id
     * @return video
     */
    @Override
    public Optional<Video> getVideo(long id) {
        return videoRepository.findById(id);
    }

    /**
     * add video
     *
     * @param v video
     */
    @Override
    public void addVideo(Video... v) {
        List<Video> videos = Arrays.stream(v)
                .peek(new NewVideoProcessor())
                .collect(Collectors.toList());
        videoRepository.saveAll(videos);
    }

    /**
     * write video data into file system
     *
     * @param id        id
     * @param videoData video data from user
     * @return video is found and set
     */
    @Override
    public boolean setVideoData(long id, MultipartFile videoData) {
        Optional<Video> video = getVideo(id);
        try {
            if (video.isPresent()) {
                videoFileManager.saveVideoData(video.get(), videoData.getInputStream());
                return true;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * get video data from file system
     *
     * @param id           id
     * @param outputStream
     * @return video data
     */
    @Override
    public boolean getData(long id, OutputStream outputStream) {
        Optional<Video> video = getVideo(id);
        try {
            if (video.isPresent()) {
                videoFileManager.copyVideoData(video.get(), outputStream);
                return true;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return false;
    }
}
