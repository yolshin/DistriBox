package com.distribox.fss.services;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class DeleteService {

    public boolean deleteFile(String file) {
        // Delete file.
        String username = ""; // Will be needed since empty directories cannot be deleted at this point. // TODO: Fill the empty string in with username!
        String path = ""; // includes username (first part) // TODO: Fill this in!
        // Save to disk.
        File filePath = new File("data" + File.separator + path);
        if (filePath.exists()) {
            return filePath.delete();
//            // Delete all empty parent directories until the "username" or the "data" folder,
//            // which is not allowed to be deleted unless the user calls "deleteFile(username)".
//            File parentDir;
//            while ((parentDir = filePath.getParentFile()) != null
//                    && parentDir.isDirectory()
//                    && !(parentDir.getName().equals(username))
//                    && !(parentDir.getName().equals("data"))
//                    && Objects.requireNonNull(parentDir.list()).length == 0) {
//                parentDir.delete();
//            }
        }
        return false;
    }

}
