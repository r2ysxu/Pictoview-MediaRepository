package org.pvrn.jpa.model.tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class SearchQuery {

	private static final String SPACE_DELIMITER = " ";
	private static final String CATEGORY_DELIMITER = "::";

	private String name;
	private Map<String, List<String>> tags;

	public SearchQuery() {
		tags = new HashMap<>();
	}

	public static SearchQuery parse(String query) {
		Pattern splitSpaceRegex = Pattern.compile(" (?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
		String[] tokens = query.split(splitSpaceRegex.pattern());
		System.out.println(Arrays.asList(tokens));
		return new SearchQuery(tokens);
	}

	private String removeQuotes(String str) {
		if (!StringUtils.isEmpty(str) && str.startsWith("\"") && str.endsWith("\"")) {
			return str.substring(1, Math.max(1, str.length() - 1));
		}
		return str;
	}

	private SearchQuery(String[] tokens) {
		tags = new HashMap<>();
		tags.put(Categories.Tags, new ArrayList<>());
		List<String> names = new ArrayList<>();

		for (String token : tokens) {
			if (StringUtils.isBlank(token))
				continue;
			if (token.startsWith(CATEGORY_DELIMITER)) {
				String[] categoryToken = token.substring(2).split(CATEGORY_DELIMITER);
				if (categoryToken.length == 1) {
					tags.get(Categories.Tags).add(removeQuotes(categoryToken[0]).toLowerCase());
				} else if (categoryToken.length == 2) {
					if (!tags.containsKey(categoryToken[0]))
						tags.put(categoryToken[0], new ArrayList<>());
					tags.get(removeQuotes(categoryToken[0])).add(removeQuotes(categoryToken[1]).toLowerCase());
				}
			} else {
				names.add(token);
			}
		}
		System.out.println("map " + tags);
		this.name = String.join(SPACE_DELIMITER, names);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, List<String>> getTags() {
		return tags;
	}

	public void setTags(Map<String, List<String>> tags) {
		this.tags = tags;
	}
}
