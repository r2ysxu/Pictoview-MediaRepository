package org.mrn.jpa.model.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class SearchQuery {

	private static final String SPACE_DELIMITER = " ";
	private static final String CATEGORY_DELIMITER = "::";
	private static final String RATING_DELIMITER = "^^";

	private String name;
	private Map<String, List<String>> tags;
	private Integer ratingRangeLower;
	private Integer ratingRangeUpper;

	public SearchQuery() {
		tags = new HashMap<>();
	}

	public static SearchQuery parse(String query) {
		Pattern splitSpaceRegex = Pattern.compile(" (?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
		String[] tokens = query.split(splitSpaceRegex.pattern());
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
		List<String> names = new ArrayList<>();

		for (String token : tokens) {
			if (StringUtils.isBlank(token))
				continue;
			if (token.startsWith(CATEGORY_DELIMITER)) {
				String[] categoryToken = token.substring(2).split(CATEGORY_DELIMITER);
				if (categoryToken.length == 1) {
					if (tags.get(Categories.Tags) == null)
						tags.put(Categories.Tags, new ArrayList<>());
					tags.get(Categories.Tags).add(removeQuotes(categoryToken[0]).toLowerCase());
				} else if (categoryToken.length == 2) {
					if (!tags.containsKey(categoryToken[0])) tags.put(categoryToken[0], new ArrayList<>());
					tags.get(removeQuotes(categoryToken[0])).add(removeQuotes(categoryToken[1]).toLowerCase());
				}
			} if (token.startsWith(RATING_DELIMITER)) {
				String[] ratingRangeToken = token.substring(2).split("-");
				if (ratingRangeToken.length == 2) {
					try {
						ratingRangeLower = Integer.parseInt(ratingRangeToken[0]);
						ratingRangeUpper = Integer.parseInt(ratingRangeToken[1]);
					} catch(NumberFormatException ex) {}
				}
			} else {
				names.add(token);
			}
		}
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

	public Integer getRatingRangeLower() {
		return ratingRangeLower;
	}

	public void setRatingRangeLower(Integer ratingRangeLower) {
		this.ratingRangeLower = ratingRangeLower;
	}

	public Integer getRatingRangeUpper() {
		return ratingRangeUpper;
	}

	public void setRatingRangeUpper(Integer ratingRangeUpper) {
		this.ratingRangeUpper = ratingRangeUpper;
	}
}
