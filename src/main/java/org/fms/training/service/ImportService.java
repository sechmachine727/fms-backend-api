package org.fms.training.service;

import java.io.File;
import java.io.IOException;

public interface ImportService {

    void importDataFromFile(File file) throws IOException;
}
