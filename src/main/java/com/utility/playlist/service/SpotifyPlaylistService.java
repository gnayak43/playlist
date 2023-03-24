package com.utility.playlist.service;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;

@Component
public class SpotifyPlaylistService implements PlaylistService {

	@Value("${clientId}")
	private String clientId;

	@Value("${clientSecret}")
	private String clientSecret;

	@Value("${userId}")
	private String userId;

	private SpotifyApi spotifyApi;

	@Override
	public void execute() {
		try {
			spotifyApi = new SpotifyApi.Builder().setClientId(clientId).setClientSecret(clientSecret).build();
			ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();

			ClientCredentials clientCredentials = clientCredentialsRequest.execute();
			spotifyApi.setAccessToken(clientCredentials.getAccessToken());
			System.out.println("Expires in: " + clientCredentials.getExpiresIn());

			GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest = spotifyApi.getListOfUsersPlaylists(userId)
					.build();
			getUsersPlaylists(getListOfUsersPlaylistsRequest);

		} catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Error: " + e);
		}
	}

	private void getUsersPlaylists(GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest) {
		try {
			final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfUsersPlaylistsRequest.execute();
			for (PlaylistSimplified playlistSimplified : playlistSimplifiedPaging.getItems()) {
				System.out.println("======PlayList Name : ==========" + playlistSimplified.getName());
				GetPlaylistsItemsRequest getPlaylistsItemsRequest = spotifyApi
						.getPlaylistsItems(playlistSimplified.getId()).build();
				Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsItemsRequest.execute();
				showTrackDetails(playlistTrackPaging);
			}
		} catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void showTrackDetails(Paging<PlaylistTrack> playlistTrackPaging) {
		for (PlaylistTrack playlistTrack : playlistTrackPaging.getItems()) {
			if (playlistTrack.getTrack() instanceof Track) {
				Track track = (Track) playlistTrack.getTrack();
				System.out.println(track.getName() + " by " + track.getArtists()[0].getName() + " from "
						+ track.getAlbum().getName());
			}
		}
	}
}