package com.hz.preview.service.impl;

import com.hz.preview.model.FileAttribute;
import com.hz.preview.service.FilePreview;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import static com.hz.preview.service.FilePreview.CODE_FILE_PREVIEW_PAGE;

/**
 * @author kl (http://kailing.pub)
 * @since 2021/2/18
 */
@Component
public class CodeFilePreviewImpl implements FilePreview {

   private final SimTextFilePreviewImpl filePreviewHandle;

    public CodeFilePreviewImpl(SimTextFilePreviewImpl filePreviewHandle) {
        this.filePreviewHandle = filePreviewHandle;
    }

    @Override
    public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
         filePreviewHandle.filePreviewHandle(url, model, fileAttribute);
        return CODE_FILE_PREVIEW_PAGE;
    }
}
