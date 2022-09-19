package dwuthk.search.external.search.kakao;


import com.fasterxml.jackson.annotation.JsonProperty;
import dwuthk.search.external.search.common.model.BlogSearchResult;
import dwuthk.search.external.search.common.model.ExternalSearchService;

import java.util.List;


public record KakaoBlogSearchResult(
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) List<KakaoBlogSubject> documents,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) KaKaoSearchApiClient.Meta meta) implements BlogSearchResult {


    @Override
    public ExternalSearchService getSearchSource() {
        return ExternalSearchService.KAKAO;
    }

    @Override
    public Integer getTotalCount() {
        return meta().totalCount();
    }

    @Override
    public Integer getPageableCount() {
        return meta().pageableCount();
    }

    @Override
    public Boolean isContinuable() {
        return !meta().isEnd();
    }

    @Override
    public List<KakaoBlogSubject> getSubjects() {
        return documents();
    }

    record KakaoBlogSubject(
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "blogname") String blogName,
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String contents,
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String datetime,
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String thumbnail,
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String title,
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String url
    ) implements BlogSubjectInfo {

        @Override
        public String getSubjectTitle() {
            return title();
        }

        @Override
        public String getSubjectUrl() {
            return url();
        }

        @Override
        public String getBlogName() {
            return blogName();
        }

        @Override
        public String getSummary() {
            return contents();
        }

        @Override
        public String getThumbnailUrl() {
            return thumbnail();
        }

        @Override
        public String getPostDatetime() {
            return datetime();
        }
    }
}
