package org.eugene.core.common;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AzureStorageReader {
    public Path read(String connectStr, String containerName, String blobName){
        // Create a BlobServiceClient object which will be used to create a container client
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectStr).buildClient();
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        List<String> names = new ArrayList<>();
        for (BlobItem blobItem: blobContainerClient.listBlobs()){
            names.add(blobItem.getName());
        }
        if (!names.contains(blobName))
            return null;
        String suffix = ".parquet";
        if (blobName.toLowerCase().endsWith(".orc")){
            suffix = ".orc";
        }else if (blobName.toLowerCase().endsWith(".avro")){
            suffix = ".avro";
        }
        // Get a reference to a blob
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
        File tmp = null;
        try {
            tmp = File.createTempFile("v-tmp-", suffix);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        String tmpPath = tmp.getAbsolutePath();
        tmp.delete();
        blobClient.downloadToFile(tmpPath);
        return new Path(tmpPath);
    }
}
