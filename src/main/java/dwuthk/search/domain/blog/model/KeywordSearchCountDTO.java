package dwuthk.search.domain.blog.model;

import java.io.Serializable;


public record KeywordSearchCountDTO(String keyword, Long count) implements Serializable {

}
