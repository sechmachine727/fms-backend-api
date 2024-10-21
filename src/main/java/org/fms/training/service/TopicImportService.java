package org.fms.training.service;

import java.io.IOException;
import java.io.InputStream;

public interface TopicImportService {

    void importDataFromStream(InputStream inputStream, boolean confirmUpdate) throws IOException;
}
