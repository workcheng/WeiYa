package com.workcheng.weiya.common.domain;

import lombok.Data;
import org.h2.jdbc.JdbcClob;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;

/**
 * @author chenghui
 * @date 2021/8/13 13:07
 */
@Data
@Entity
public class ImageData implements Serializable {
    @Id
    private String path;
    @Lob
    @Basic(fetch = FetchType.LAZY) @Column(columnDefinition = "text")
    private String data;
}
