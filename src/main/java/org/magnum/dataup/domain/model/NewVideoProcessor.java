package org.magnum.dataup.domain.model;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
public class NewVideoProcessor implements Consumer<Video> {
    private static final AtomicInteger idAtomic = new AtomicInteger(0);
    /**
     * Performs this operation on the given argument.
     *
     * @param video the input argument
     */
    @Override
    public void accept(Video video) {
        long id = idAtomic.incrementAndGet();
        String url = "http://localhost:8080/video/" + id + "/data";
        video.setId(id);
        video.setDataUrl(url);
        log.info("Processed new video, id={}, url={}", id, url);
    }
}
