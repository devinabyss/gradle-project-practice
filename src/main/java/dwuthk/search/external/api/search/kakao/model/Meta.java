package dwuthk.search.external.api.search.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record Meta(
        @JsonProperty("total_count")
        Integer totalCount,
        @JsonProperty("pageable_count")
        Integer pageableCount,
        @JsonProperty("is_end")
        Boolean isEnd
) implements Serializable {
}