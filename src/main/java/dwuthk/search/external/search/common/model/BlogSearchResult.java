package dwuthk.search.external.search.common.model;


import java.util.List;

public interface BlogSearchResult {

    ExternalSearchService getSearchSource();

    Integer getTotalCount();

    Integer getPageableCount();

    Boolean isContinuable();

    List<? extends BlogSubjectInfo> getSubjects();

    interface BlogSubjectInfo {

        String getSubjectTitle();

        String getSubjectUrl();

        String getBlogName();

        String getSummary();

        String getThumbnailUrl();

        String getPostDatetime();
    }
}
