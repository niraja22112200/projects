package com.imsapp.ims.dto.resources;

import com.imsapp.ims.config.DocumentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceResponse {
    private Long id;
    private String name;
    private DocumentType type;
    private String url;

}
