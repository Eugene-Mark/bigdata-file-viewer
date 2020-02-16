# bigdata-file-viewer
A cross-platform (Windows, MAC, Linux) desktop application to view common bigata binary format like Parquet, ORC, AVRO, etc. 

## Feature List
 - Open and view Parquet, ORC and AVRO at local directory
 - Convert binary format data to text format data like CSV
 - Support complex data type like array, map, struct, etc
 - Suport multiple platforms like Windows, MAC and Linux
 - Code is extensible to involve other data format
 
## Usage
 - Download runnable jar from [release page][1] or run from directory by `mvn exec:java -Dexec.mainClass=org.eugene.App`
 - Invoke it by `java -jar BigdataFileViewer-1.1-SNAPSHOT-jar-with-dependencies.jar`
 - Open binary format file by "File" -> "Open". Currently, it can open file with parquet suffix, orc suffix and avro suffix. If no suffix specified, the tool will try to extract the it as Parquet file
 - Set the maximum rows of each page by "View" -> `Input maximum row number` -> "Go"
 - Set visible properties by "View" -> "Add/Remove Properties"
 - Convert to CSV file by "File" -> "Save as" -> "CSV"
 - Check schema information by unfolding "Schema Information" panel
 
 [Click here for live demo][2]
 
 ## Build 
 - To build an all-in-one runnable jar, use `mvn clean compile assembly:single`
 - Java 1.8 or higher is required
 - Make sure the Java has javafx bound. For example, I installed openjdk 1.8 on Ubuntu 18.04 and it has no javafx bound, I installed it following guide [here][3]. 
 
 ## Screenshots
 
 ![Main page](resources/main-page.png)
 




[1]: https://github.com/Eugene-Mark/bigdata-file-viewer/releases
[2]: https://github.com/Eugene-Mark/bigdata-file-viewer/tree/master/resources/demo.gif
[3]: https://stackoverflow.com/a/56166582/3378204

