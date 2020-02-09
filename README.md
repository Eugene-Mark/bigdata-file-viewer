# bigdata-file-viewer
A cross-platform (Windows, MAC, Linux) desktop application to view common bigata binary format like Parquet, ORC, etc. 

## Feature List
 - Open and view Parquet and ORC
 - Convert binary format data to text format data like CSV
 - Support complex data type like array, map, struct, etc
 - Suport multiple platforms like Windows, MAC and Linux
 - Code is extensible to involve more binary format like AVRO
 
## Usage
 - Download runnable jar from [release page][1]
 - Invoke it by `java -jar BigdataFileViewer-1.0-SNAPSHOT-jar-with-dependencies.jar`
 - Open binary format file by "File" -> "Open". Currently, it can open file with .parquet suffix and .orc suffix. If no suffix specified, the tool will try to extract the it as Parquet file
 - Set the maximum rows of each page by "View" -> `Input maximum row number` -> "Go"
 - Set visible properties by "View" -> "Add/Remove Properties"
 - Convert to CSV file by "File" -> "Save as" -> "CSV"
 - Check schema information by unfolding "Schema Information" panel
 
 ## Build 
 - To build an all-in-one runnable jar, use `mvn clean compile assembly:single`
 
 ## Screenshots
 
 ![Demo GIF](resources/demo.gif)
 




[1]: https://github.com/Eugene-Mark/bigdata-file-viewer/releases

