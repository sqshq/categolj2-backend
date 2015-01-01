/*
 * Copyright (C) 2014 Toshiaki Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package am.ik.categolj2.api.entry;

import am.ik.categolj2.domain.model.Category;
import am.ik.categolj2.domain.validation.TagName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntryResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer entryId;
    @NotNull
    @Size(min = 1, max = 512)
    private String title;
    @NotNull
    @Size(min = 1, max = 65536)
    private String contents;
    @NotNull
    @Size(min = 1, max = 10)
    private String format;
    @NotNull
    private String categoryString;
    @Valid
    private Set<TagResource> tags = Sets.newTreeSet();

    private Long version;

    // input only
    private boolean published;
    private boolean updateLastModifiedDate;
    private boolean saveInHistory;

    // output only
    private List<String> categoryName;
    private DateTime createdDate;
    private DateTime lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;

    @JsonIgnore
    EntryResource setCategoryName(List<Category> category) {
        this.categoryName = category.stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());
        return this;
    }

    @JsonProperty
    public List<String> getCategoryName() {
        return this.categoryName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TagResource implements Comparable<TagResource> {
        @NotNull
        @TagName
        private String tagName;

        @Override
        public int compareTo(TagResource o) {
            return Objects.compare(this, o, Comparator.comparing(TagResource::getTagName));
        }
    }
}
