package com.itmuch.contentcenter.dao.content;

import com.itmuch.contentcenter.domain.enums.AuditStatusEnum;
import lombok.Data;

@Data
public class ShareAuditDTO {

    private AuditStatusEnum auditStatusEnum;
    private String reason;
}
