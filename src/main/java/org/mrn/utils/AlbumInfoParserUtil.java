package org.mrn.utils;

import org.mrn.query.model.NewAlbumInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AlbumInfoParserUtil {

	public static NewAlbumInfo loadAlbumInfoFromJson(String jsonStr) throws JsonMappingException, JsonProcessingException {
		return new ObjectMapper().readValue(jsonStr, NewAlbumInfo.class);
	}
}
