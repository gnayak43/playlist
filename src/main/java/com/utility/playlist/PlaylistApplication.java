
package com.utility.playlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.utility.playlist.service.PlaylistService;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class PlaylistApplication implements CommandLineRunner {

	@Autowired
	private PlaylistService playlistService;

	@Override
	public void run(String... args) {
		playlistService.execute();
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(PlaylistApplication.class, args);
	}
}
