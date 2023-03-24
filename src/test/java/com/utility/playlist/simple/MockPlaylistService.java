package com.utility.playlist.simple;

import com.utility.playlist.service.PlaylistService;

public class MockPlaylistService implements PlaylistService {

	@Override
	public void execute() {
		System.out.println("MockPlaylistService executed!!!");
	}
}
