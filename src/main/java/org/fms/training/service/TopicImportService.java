package org.fms.training.service;

import java.io.IOException;
import java.io.InputStream;

public interface ImportService {

    void importDataFromStream(InputStream inputStream, boolean confirmUpdate) throws IOException;
}
