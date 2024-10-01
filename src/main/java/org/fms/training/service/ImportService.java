package org.fms.training.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface ImportService {

    void importDataFromStream(InputStream inputStream) throws IOException;
}
