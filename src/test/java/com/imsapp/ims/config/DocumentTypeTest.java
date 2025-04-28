package com.imsapp.ims.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DocumentTypeTest {

    @Test
    void testDocumentTypeValues() {
        DocumentType[] values = DocumentType.values();
        assertEquals(3, values.length);
        assertEquals(DocumentType.POLICY_DOCUMENT, values[0]);
        assertEquals(DocumentType.CLAIM_FORM, values[1]);
        assertEquals(DocumentType.CUSTOMER_SUPPORT_INFORMATION, values[2]);
    }

    @Test
    void testDocumentTypeValueOf() {
        assertEquals(DocumentType.POLICY_DOCUMENT, DocumentType.valueOf("POLICY_DOCUMENT"));
        assertEquals(DocumentType.CLAIM_FORM, DocumentType.valueOf("CLAIM_FORM"));
        assertEquals(DocumentType.CUSTOMER_SUPPORT_INFORMATION, DocumentType.valueOf("CUSTOMER_SUPPORT_INFORMATION"));
    }

    @Test
    void testDocumentTypeNotNull() {
        for (DocumentType documentType : DocumentType.values()) {
            assertNotNull(documentType);
        }
    }
}
