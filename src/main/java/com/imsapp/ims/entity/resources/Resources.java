package com.imsapp.ims.entity.resources;

import com.imsapp.ims.config.DocumentType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Resources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private DocumentType type;
    private String url;

}

