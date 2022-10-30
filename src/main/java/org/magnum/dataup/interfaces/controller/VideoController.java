/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.magnum.dataup.interfaces.controller;

import lombok.extern.slf4j.Slf4j;
import org.magnum.dataup.domain.model.Video;
import org.magnum.dataup.domain.model.VideoStatus;
import org.magnum.dataup.domain.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import static org.magnum.dataup.VideoSvcApi.VIDEO_DATA_PATH;
import static org.magnum.dataup.VideoSvcApi.VIDEO_SVC_PATH;

@Slf4j
@RestController
public class VideoController {

	@Autowired
	private VideoService videoService;


	/**
	 * You will need to create one or more Spring controllers to fulfill the
	 * requirements of the assignment. If you use this file, please rename it
	 * to something other than "AnEmptyController"
	 * 
	 * 
		 ________  ________  ________  ________          ___       ___  ___  ________  ___  __       
		|\   ____\|\   __  \|\   __  \|\   ___ \        |\  \     |\  \|\  \|\   ____\|\  \|\  \     
		\ \  \___|\ \  \|\  \ \  \|\  \ \  \_|\ \       \ \  \    \ \  \\\  \ \  \___|\ \  \/  /|_   
		 \ \  \  __\ \  \\\  \ \  \\\  \ \  \ \\ \       \ \  \    \ \  \\\  \ \  \    \ \   ___  \  
		  \ \  \|\  \ \  \\\  \ \  \\\  \ \  \_\\ \       \ \  \____\ \  \\\  \ \  \____\ \  \\ \  \ 
		   \ \_______\ \_______\ \_______\ \_______\       \ \_______\ \_______\ \_______\ \__\\ \__\
		    \|_______|\|_______|\|_______|\|_______|        \|_______|\|_______|\|_______|\|__| \|__|
                                                                                                                                                                                                                                                                        
	 * 
	 */

	/**
	 * This endpoint in the API returns a list of the videos that have
	 * been added to the server. The Video objects should be returned as
	 * JSON.
	 * <p>
	 * To manually test this endpoint, run your server and open this URL in a browser:
	 * http://localhost:8080/video
	 *
	 * @return
	 */
	@GetMapping(VIDEO_SVC_PATH)
	public Collection<Video> getVideoList() {
		return videoService.getAllVideos();
	}

	/**
	 * This endpoint allows clients to add Video objects by sending POST requests
	 * that have an application/json body containing the Video object information.
	 *
	 * @param v
	 * @return
	 */
	@PostMapping(VIDEO_SVC_PATH)
	public Video addVideo(@RequestBody Video v) {
		videoService.addVideo(v);
		return v;
	}

	/**
	 * This endpoint allows clients to set the mpeg video data for previously
	 * added Video objects by sending multipart POST requests to the server.
	 * The URL that the POST requests should be sent to includes the ID of the
	 * Video that the data should be associated with (e.g., replace {id} in
	 * the url /video/{id}/data with a valid ID of a video, such as /video/1/data
	 * -- assuming that "1" is a valid ID of a video).
	 *
	 * @param id
	 * @param videoData
	 * @return
	 */
	@PostMapping(VIDEO_DATA_PATH)
	public ResponseEntity<VideoStatus> setVideoData(@PathVariable long id, @RequestParam("data") MultipartFile videoData) {
		HttpStatus httpStatus = videoService.setVideoData(id, videoData) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(new VideoStatus(VideoStatus.VideoState.READY), httpStatus);
	}

	/**
	 * This endpoint should return the video data that has been associated with
	 * a Video object or a 404 if no video data has been set yet. The URL scheme
	 * is the same as in the method above and assumes that the client knows the ID
	 * of the Video object that it would like to retrieve video data for.
	 * <p>
	 * This method uses Retrofit's @Streaming annotation to indicate that the
	 * method is going to access a large stream of data (e.g., the mpeg video
	 * data on the server). The client can access this stream of data by obtaining
	 * an InputStream from the Response as shown below:
	 * <p>
	 * VideoSvcApi client = ... // use retrofit to create the client
	 * Response response = client.getData(someVideoId);
	 * InputStream videoDataStream = response.getBody().in();
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(VIDEO_DATA_PATH)
	public void getData(@PathVariable long id, HttpServletResponse response) {
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		try {
			httpStatus = videoService.getData(id, response.getOutputStream()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		response.setStatus(httpStatus.value());
	}
}
