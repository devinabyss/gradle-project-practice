package dwuthk.search.external.api.search.kakao;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dwuthk.search.common.convert.json.LocalDateTimeOffsetDeserializer;
import dwuthk.search.external.api.search.common.model.BlogSearchResult;
import dwuthk.search.external.api.search.common.model.ExternalSearchService;
import dwuthk.search.external.api.search.kakao.model.Meta;

import java.time.LocalDateTime;
import java.util.List;


public record KakaoBlogSearchResult(
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) List<KakaoBlogSubject> documents,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) Meta meta) implements BlogSearchResult {


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
            @JsonProperty(value = "blogname") String blogName,
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String contents,
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
            @JsonDeserialize(using = LocalDateTimeOffsetDeserializer.class)
            LocalDateTime datetime,
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
        public LocalDateTime getPostDatetime() {
            return datetime();
        }
    }
}
