package dwuthk.search.external.search.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import dwuthk.search.external.search.common.model.BlogSearchResult;
import dwuthk.search.external.search.common.model.ExternalSearchService;

import java.util.List;

public record NaverBlogSearchResult(
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String lastBuildDate,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) Integer total,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) Integer start,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) Integer display,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) List<NaverBlogSubject> items
) implements BlogSearchResult {


    @Override
    public ExternalSearchService getSearchSource() {
        return ExternalSearchService.NAVER;
    }

    @Override
    public Integer getTotalCount() {
        return total();
    }

    @Override
    public Integer getPageableCount() {
        return Math.min(total(), display() * 50);
    }

    @Override
    public Boolean isContinuable() {

        return start() < 50 && display() * start() < total();
    }

    @Override
    public List<NaverBlogSubject> getSubjects() {
        return items();
    }

    private record NaverBlogSubject(
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String title,
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String link,
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String description,
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "bloggername") String bloggerName,
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "bloggerlink") String bloggerLink,
            String postdate
    ) implements BlogSubjectInfo {


        @Override
        public String getSubjectTitle() {
            return title();
        }

        @Override
        public String getSubjectUrl() {
            return link();
        }

        @Override
        public String getBlogName() {
            return bloggerName();
        }

        @Override
        public String getSummary() {
            return description();
        }

        @Override
        public String getThumbnailUrl() {
            return "";
        }

        @Override
        public String getPostDatetime() {
            return postdate();
        }
    }
}
