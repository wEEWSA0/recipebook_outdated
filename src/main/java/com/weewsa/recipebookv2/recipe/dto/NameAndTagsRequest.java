package com.weewsa.recipebookv2.recipe.dto;

import com.weewsa.recipebookv2.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NameAndTagsRequest {
    private Set<TagRequest> tags;
    private String name;
}
