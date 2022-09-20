package dwuthk.search.external.api.search.common.model;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public interface BlogSearchResult extends Serializable {

    ExternalSearchService getSearchSource();

    Integer getTotalCount();

    Integer getPageableCount();

    Boolean isContinuable();

    List<? extends BlogSubjectInfo> getSubjects();

    interface BlogSubjectInfo extends Serializable {

        String getSubjectTitle();

        String getSubjectUrl();

        String getBlogName();

        String getSummary();

        String getThumbnailUrl();

        LocalDateTime getPostDatetime();
    }
}
